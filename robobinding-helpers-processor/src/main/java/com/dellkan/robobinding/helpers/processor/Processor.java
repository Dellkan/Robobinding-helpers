package com.dellkan.robobinding.helpers.processor;

import com.dellkan.robobinding.helpers.modelgen.AddToData;
import com.dellkan.robobinding.helpers.modelgen.DependsOnStateOf;
import com.dellkan.robobinding.helpers.modelgen.Get;
import com.dellkan.robobinding.helpers.modelgen.GetSet;
import com.dellkan.robobinding.helpers.modelgen.IncludeModel;
import com.dellkan.robobinding.helpers.modelgen.ItemPresentationModel;
import com.dellkan.robobinding.helpers.modelgen.ListItems;
import com.dellkan.robobinding.helpers.modelgen.PresentationMethod;
import com.dellkan.robobinding.helpers.modelgen.PresentationModel;
import com.dellkan.robobinding.helpers.modelgen.SkipMethod;
import com.dellkan.robobinding.helpers.modelgen.TwoStateGetSet;
import com.dellkan.robobinding.helpers.processor.descriptors.AddToDataDescriptor;
import com.dellkan.robobinding.helpers.processor.descriptors.GetSetDescriptor;
import com.dellkan.robobinding.helpers.processor.descriptors.IncludeModelDescriptor;
import com.dellkan.robobinding.helpers.processor.descriptors.ListItemsDescriptor;
import com.dellkan.robobinding.helpers.processor.descriptors.MethodDescriptor;
import com.dellkan.robobinding.helpers.processor.descriptors.ModelDescriptor;
import com.dellkan.robobinding.helpers.processor.descriptors.SubValidateDescriptor;
import com.dellkan.robobinding.helpers.processor.descriptors.ValidateDescriptor;
import com.dellkan.robobinding.helpers.validation.ValidateType;
import com.dellkan.robobinding.helpers.validation.ValidationProcessor;
import com.dellkan.robobinding.helpers.validation.processors.ValidateBooleanProcessor;
import com.dellkan.robobinding.helpers.validation.processors.ValidateLengthProcessor;
import com.dellkan.robobinding.helpers.validation.processors.ValidateListProcessor;
import com.dellkan.robobinding.helpers.validation.processors.ValidatePatternProcessor;
import com.dellkan.robobinding.helpers.validation.processors.ValidateSelectedProcessor;
import com.dellkan.robobinding.helpers.validation.validators.Validate;
import com.dellkan.robobinding.helpers.validation.validators.ValidateBoolean;
import com.dellkan.robobinding.helpers.validation.validators.ValidateLengthMax;
import com.dellkan.robobinding.helpers.validation.validators.ValidateLengthMin;
import com.dellkan.robobinding.helpers.validation.validators.ValidateLengthRange;
import com.dellkan.robobinding.helpers.validation.validators.ValidateList;
import com.dellkan.robobinding.helpers.validation.validators.ValidateMax;
import com.dellkan.robobinding.helpers.validation.validators.ValidateMin;
import com.dellkan.robobinding.helpers.validation.validators.ValidatePattern;
import com.dellkan.robobinding.helpers.validation.validators.ValidateRange;
import com.dellkan.robobinding.helpers.validation.validators.ValidateSelected;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Compiles together a bunch of utilities usable on models marked with {@link com.dellkan.robobinding.helpers.modelgen.PresentationModel PresentationModel},
 * for validation and usage through robobinding
 */
@SupportedAnnotationTypes({
        "com.dellkan.robobinding.helpers.modelgen.PresentationModel",
        "com.dellkan.robobinding.helpers.modelgen.ItemPresentationModel",
        "com.dellkan.robobinding.helpers.validation.ValidationType"
})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class Processor extends AbstractProcessor {
    private Messager messager;
    private Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
    private Map<TypeElement, List<String>> customValidators = new HashMap<>();
    private Map<Element, ModelDescriptor> descriptors = new HashMap<>();
    private static boolean globalLookupFileWritten = false;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        // Logger
        messager = processingEnv.getMessager();

        // Freemarker config
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateLoader(new ClassTemplateLoader(this.getClass(), "/templates"));
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // Retrieve custom validators
        for (Element element : roundEnv.getElementsAnnotatedWith(ValidateType.class)) {
            if (element.getKind().isClass()) {
                AnnotationValue annotationValue = null;
                for (AnnotationMirror mirror : element.getAnnotationMirrors()) {
                    for(Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : mirror.getElementValues().entrySet()) {
                        if("value".equals(entry.getKey().getSimpleName().toString())) {
                            annotationValue = entry.getValue();
                            break;
                        }
                    }
                }
                if (annotationValue != null) {
                    List<String> annotationClasses = new ArrayList<>();
                    if (annotationValue.getValue() instanceof List) {
                        for (AnnotationValue classValue : (List<? extends AnnotationValue>)annotationValue.getValue()) {
                            annotationClasses.add(classValue.getValue().toString());
                        }
                    } else {
                        annotationClasses.add(annotationValue.getValue().toString());
                    }
                    customValidators.put((TypeElement) element, annotationClasses);
                }
            }
        }
        // Add premade validators
        addCustomValidator(ValidationProcessor.class, Validate.class);
        addCustomValidator(ValidateBooleanProcessor.class, ValidateBoolean.class);
        addCustomValidator(ValidatePatternProcessor.class, ValidatePattern.class);
        addCustomValidator(ValidateLengthProcessor.class,
                ValidateMin.class,
                ValidateMax.class,
                ValidateRange.class,
                ValidateLengthMin.class,
                ValidateLengthMax.class,
                ValidateLengthRange.class
        );
        addCustomValidator(ValidateSelectedProcessor.class, ValidateSelected.class);
        addCustomValidator(ValidateListProcessor.class, ValidateList.class);

        // Create generated viewmodel helpers
        Set<Element> elements = new HashSet<>();
        elements.addAll(roundEnv.getElementsAnnotatedWith(PresentationModel.class));
        elements.addAll(roundEnv.getElementsAnnotatedWith(ItemPresentationModel.class));

        // Descriptors
        for (Element element : elements) {

            // Check whether the element has serialize enabled. Missing serializable is a very common mistake
            TypeMirror serializableType = processingEnv.getElementUtils().getTypeElement(Serializable.class.getCanonicalName()).asType();
            if (!processingEnv.getTypeUtils().isAssignable(element.asType(), serializableType)) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Missing Serializable interface", element);
            }

            if (!descriptors.containsKey(element)) {
                // Create an empty descriptor
                ModelDescriptor descriptor = new ModelDescriptor(element, messager, processingEnv);

                // Add it to the global list
                descriptors.put(element, descriptor);

                // Populate it
                traverseChildren(descriptor);
            }
        }

        // Flatten structure
        for (Map.Entry<Element, ModelDescriptor> entry : descriptors.entrySet()) {
            flattenStructure(entry.getValue(), null, null);
        }

        // Prepare output
        for (Map.Entry<Element, ModelDescriptor> entry : descriptors.entrySet()) {
            Element element = entry.getKey();
            ModelDescriptor descriptor = entry.getValue();
            if (descriptor.isWrittenToFile()) {
                continue;
            }

            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, String.format("Generating %s", Util.elementToString(element) + "$$Helper"));

            descriptor.setWrittenToFile(true);

            Map<String, Object> input = new HashMap<>();
            input.put("accessors", descriptor.getAccessors());
            input.put("methods", descriptor.getMethods());
            input.put("validators", descriptor.getValidators());
            input.put("listItems", descriptor.getListItems());
            input.put("dataItems", descriptor.getDataItems());
            input.put("descriptor", descriptor);

            // Fill in variables
            input.put("className", element.getSimpleName());
            input.put("packageName", Util.getPackage(element));

            try {
                // Create target file
                JavaFileObject file = null;

                // Get template
                Template template = null;

                if (element.getAnnotation(PresentationModel.class) != null) {
                    template = cfg.getTemplate("PresentationModel.ftl");
                    file = processingEnv.getFiler().createSourceFile(Util.elementToString(element) + "$$Helper");

                    Writer out = file.openWriter();

                    // Process template
                    template.process(input, out);

                    // Write
                    out.flush();
                    out.close();
                }

                if (element.getAnnotation(ItemPresentationModel.class) != null) {
                    template = cfg.getTemplate("ItemPresentationModel.ftl");
                    file = processingEnv.getFiler().createSourceFile(Util.elementToString(element) + "$$ItemHelper");

                    Writer out = file.openWriter();

                    // Process template
                    template.process(input, out);

                    // Write
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TemplateException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!globalLookupFileWritten) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Creating global lookup file");
            try {
                // Write global lookup file
                Map<String, Object> input = new HashMap<>();
                input.put("models", descriptors);

                // Create target file
                JavaFileObject file = processingEnv.getFiler().createSourceFile("com.dellkan.robobinding.helpers.GlobalPresentationModelLookup");

                // Get template
                Template template = cfg.getTemplate("GlobalPresentationModelLookup.ftl");

                Writer out = file.openWriter();

                // Process template
                template.process(input, out);

                // Write
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TemplateException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            globalLookupFileWritten = true;
        }

        return false;
    }

    @SafeVarargs
    private final void addCustomValidator(Class<? extends ValidationProcessor> processor, Class<? extends Annotation>... annotations) {
        List<String> annotationClasses = new ArrayList<>();
        for (Class<? extends Annotation> annotationClass : annotations) {
            annotationClasses.add(annotationClass.getCanonicalName());
        }
        customValidators.put(
                processor != null ? processingEnv.getElementUtils().getTypeElement(processor.getCanonicalName()) : null,
                annotationClasses
        );
    }

    private void flattenStructure(ModelDescriptor child, ModelDescriptor parent, IncludeModelDescriptor includeDescriptor) {
        for (IncludeModelDescriptor includeModelDescriptor : child.getIncludeItems()) {
            ModelDescriptor descriptor = descriptors.get(includeModelDescriptor.getFieldClassElement());
            if (descriptor != null) {
                flattenStructure(descriptor, child, includeModelDescriptor);
            }
        }

        if (parent != null && !includeDescriptor.isProcessed()) {
            // Add accessors
            for (GetSetDescriptor item : child.getAccessors()) {
                if (!includeDescriptor.shouldSkipField(item.getName())) {
                    GetSetDescriptor proxy = new GetSetDescriptor(parent, item.getField(), item.isGetter(), item.isSetter(), item.isTwoState());
                    proxy.setClassPrefix(includeDescriptor.getField().getSimpleName().toString() + "." + item.getClassPrefix());
                    proxy.setPrefixes(item.getPrefixes());
                    proxy.addPrefix(includeDescriptor.getPrefix());
                    parent.getAccessors().add(proxy);
                }
            }

            // Add methods
            for (MethodDescriptor item : child.getMethods()) {
                if (!includeDescriptor.shouldSkipField(item.getName())) {
                    MethodDescriptor proxy = new MethodDescriptor(parent, item.getMethod());
                    proxy.setClassPrefix(includeDescriptor.getField().getSimpleName().toString() + "." + item.getClassPrefix());
                    proxy.setPrefixes(item.getPrefixes());
                    proxy.addPrefix(includeDescriptor.getPrefix());
                    parent.getMethods().add(proxy);
                }
            }

            // Add List items
            for (ListItemsDescriptor item : child.getListItems()) {
                if (!includeDescriptor.shouldSkipField(item.getName())) {
                    ListItemsDescriptor proxy = new ListItemsDescriptor(parent, item.getField());
                    proxy.setClassPrefix(includeDescriptor.getField().getSimpleName().toString() + "." + item.getClassPrefix());
                    proxy.setPrefixes(item.getPrefixes());
                    proxy.addPrefix(includeDescriptor.getPrefix());
                    parent.getListItems().add(proxy);
                }
            }

            // Add validators
            for (ValidateDescriptor item : child.getValidators()) {
                if (!includeDescriptor.shouldSkipField(item.getName())) {
                    ValidateDescriptor proxy = new ValidateDescriptor(parent, item.getField());
                    for (SubValidateDescriptor subValidator : item.getValidators()) {
                        SubValidateDescriptor subProxy = new SubValidateDescriptor(parent, item.getField(), proxy, subValidator.getAnnotationType(), subValidator.getProcessor());
                        subProxy.setClassPrefix(includeDescriptor.getField().getSimpleName().toString() + "." + item.getClassPrefix());
                        subProxy.setPrefixes(subValidator.getPrefixes());
                        subProxy.addPrefix(includeDescriptor.getPrefix());
                        proxy.addValidator(subProxy);
                    }
                    proxy.setClassPrefix(includeDescriptor.getField().getSimpleName().toString() + "." + item.getClassPrefix());
                    proxy.setPrefixes(item.getPrefixes());
                    proxy.addPrefix(includeDescriptor.getPrefix());
                    parent.getValidators().add(proxy);
                }
            }

            // Add data items
            for (AddToDataDescriptor item : child.getDataItems()) {
                if (!includeDescriptor.shouldSkipField(item.getName())) {
                    AddToDataDescriptor proxy = new AddToDataDescriptor(parent, item.getField(), item.getAnnotation());
                    proxy.setClassPrefix(includeDescriptor.getField().getSimpleName().toString() + "." + item.getClassPrefix());
                    proxy.setPrefixes(item.getPrefixes());
                    proxy.addPrefix(includeDescriptor.getPrefix());
                    parent.getDataItems().add(proxy);
                }
            }
            includeDescriptor.setProcessed(true);
        }
    }

    private void traverseChildren(ModelDescriptor descriptor) {
        for (Element child : processingEnv.getElementUtils().getAllMembers((TypeElement) descriptor.getModel())) {

            ValidateDescriptor validationDescriptor = null;

            // Check if methods data output should be included in getData
            AddToData addToDataAnnotation = null;
            if ((addToDataAnnotation = child.getAnnotation(AddToData.class)) != null) {
                descriptor.getDataItems().add(new AddToDataDescriptor(
                        descriptor,
                        child,
                        addToDataAnnotation
                ));
            }

            IncludeModel includeModelAnnotation = null;
            if ((includeModelAnnotation = child.getAnnotation(IncludeModel.class)) != null) {
                descriptor.getIncludeItems().add(new IncludeModelDescriptor(includeModelAnnotation, child, processingEnv.getTypeUtils().asElement(child.asType())));
            }

            // Create list of existing methods
            if (child.getKind() == ElementKind.METHOD) {
                // Check if method is used as part of validation
                Validate validateAnnotation = child.getAnnotation(Validate.class);
                if (validateAnnotation != null) {
                    validationDescriptor = validationDescriptor == null ? new ValidateDescriptor(descriptor, child) : validationDescriptor;
                    validationDescriptor.addValidator(new SubValidateDescriptor(
                            descriptor,
                            child,
                            validationDescriptor,
                            Validate.class.getCanonicalName(),
                            null
                    ));
                }

                // TODO: Remove @SkipMethod, and warning below when related projects are stabilized
                if (child.getAnnotation(SkipMethod.class) != null) {
                    continue;
                }

                if (child.getAnnotation(PresentationMethod.class) == null && child.getAnnotation(DependsOnStateOf.class) == null) {
                    if (!child.getModifiers().contains(Modifier.PRIVATE)) {
                        // messager.printMessage(Diagnostic.Kind.WARNING, "Skipping unannotated method " + child.getSimpleName(), child);
                    }
                    continue;
                }

                ExecutableElement method = (ExecutableElement) child;
                descriptor.getMethods().add(new MethodDescriptor(descriptor, method));
            }

            // Create list of getters, setters
            else if (child.getKind() == ElementKind.FIELD) {
                Annotation childAnnotation;
                if ((childAnnotation = child.getAnnotation(Get.class)) != null) {
                    Get get = (Get) childAnnotation;
                    descriptor.getAccessors().add(new GetSetDescriptor(descriptor, child, true, false, false));
                } else if ((childAnnotation = child.getAnnotation(GetSet.class)) != null) {
                    GetSet getSet = (GetSet) childAnnotation;
                    descriptor.getAccessors().add(new GetSetDescriptor(descriptor, child, true, true, false));
                } else if ((childAnnotation = child.getAnnotation(com.dellkan.robobinding.helpers.modelgen.Set.class)) != null) {
                    descriptor.getAccessors().add(new GetSetDescriptor(descriptor, child, true, true, false));
                } else if ((childAnnotation = child.getAnnotation(TwoStateGetSet.class)) != null) {
                    if (!Util.typeToString(child.asType()).equals("java.lang.Boolean")) {
                        messager.printMessage(Diagnostic.Kind.ERROR, "TwoStateGetSet only supports boolean fields!", child);
                    } else {
                        TwoStateGetSet twoStateGetSet = (TwoStateGetSet) childAnnotation;
                        descriptor.getAccessors().add(new GetSetDescriptor(descriptor, child, true, true, true));
                    }
                } else if ((childAnnotation = child.getAnnotation(ListItems.class)) != null) {
                    descriptor.getListItems().add(new ListItemsDescriptor(descriptor, child));
                }

                // Validation
                for (Map.Entry<TypeElement, List<String>> markerAnnotation : customValidators.entrySet()) {
                    for (AnnotationMirror mirror : child.getAnnotationMirrors()) {
                        for (String annotationClass : markerAnnotation.getValue()) {
                            if (Util.typeToString(mirror.getAnnotationType()).equals(annotationClass)) {
                                validationDescriptor = validationDescriptor == null ? new ValidateDescriptor(descriptor, child) : validationDescriptor;
                                validationDescriptor.addValidator(new SubValidateDescriptor(
                                        descriptor,
                                        child,
                                        validationDescriptor,
                                        annotationClass,
                                        markerAnnotation.getKey()
                                ));
                            }
                        }
                    }
                }

                if (validationDescriptor != null && !validationDescriptor.getValidators().isEmpty()) {
                    descriptor.getValidators().add(validationDescriptor);
                }
            }
        }
    }
}
