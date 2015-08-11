package com.dellkan.robobinding.helpers.processor;

import com.dellkan.robobinding.helpers.model.ListContainer;
import com.dellkan.robobinding.helpers.modelgen.AddToData;
import com.dellkan.robobinding.helpers.modelgen.DependsOnStateOf;
import com.dellkan.robobinding.helpers.modelgen.Get;
import com.dellkan.robobinding.helpers.modelgen.GetSet;
import com.dellkan.robobinding.helpers.modelgen.ItemPresentationModel;
import com.dellkan.robobinding.helpers.modelgen.ListItems;
import com.dellkan.robobinding.helpers.modelgen.PresentationModel;
import com.dellkan.robobinding.helpers.modelgen.SkipMethod;
import com.dellkan.robobinding.helpers.modelgen.Stringify;
import com.dellkan.robobinding.helpers.modelgen.TwoStateGetSet;
import com.dellkan.robobinding.helpers.validation.ValidateIf;
import com.dellkan.robobinding.helpers.validation.ValidateType;
import com.dellkan.robobinding.helpers.validation.ValidationProcessor;
import com.dellkan.robobinding.helpers.validation.processors.ValidateBooleanProcessor;
import com.dellkan.robobinding.helpers.validation.processors.ValidateLengthProcessor;
import com.dellkan.robobinding.helpers.validation.processors.ValidatePatternProcessor;
import com.dellkan.robobinding.helpers.validation.processors.ValidateSelectedProcessor;
import com.dellkan.robobinding.helpers.validation.validators.ValidateBoolean;
import com.dellkan.robobinding.helpers.validation.validators.ValidateLengthMax;
import com.dellkan.robobinding.helpers.validation.validators.ValidateLengthMin;
import com.dellkan.robobinding.helpers.validation.validators.ValidateLengthRange;
import com.dellkan.robobinding.helpers.validation.validators.ValidateMax;
import com.dellkan.robobinding.helpers.validation.validators.ValidateMin;
import com.dellkan.robobinding.helpers.validation.validators.ValidatePattern;
import com.dellkan.robobinding.helpers.validation.validators.ValidateRange;
import com.dellkan.robobinding.helpers.validation.validators.ValidateSelected;

import java.io.IOException;
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
import javax.lang.model.element.VariableElement;
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

        Set<Element> elements = new HashSet<>();
        elements.addAll(roundEnv.getElementsAnnotatedWith(PresentationModel.class));
        elements.addAll(roundEnv.getElementsAnnotatedWith(ItemPresentationModel.class));
        for (Element element : elements) {
            Map<String, Object> input = new HashMap<>();

            List<MethodDescriptor> methods = new ArrayList<>();
            List<GetSetDescriptor> accessors = new ArrayList<>();
            List<ValidateDescriptor> validators = new ArrayList<>();
            List<ListItemsDescriptor> listItems = new ArrayList<>();
            List<AddDataDescriptor> dataItems = new ArrayList<>();

            for (Element child : element.getEnclosedElements()) {
                // Create list of existing methods
                if (child.getKind() == ElementKind.METHOD && child.getModifiers().contains(Modifier.PUBLIC)) {
                    if (child.getAnnotation(SkipMethod.class) != null) {
                        continue;
                    }
                    if (child.getModifiers().contains(Modifier.STATIC)) {
                        continue;
                    }
                    ExecutableElement method = (ExecutableElement) child;
                    DependsOnStateOf annotation = child.getAnnotation(DependsOnStateOf.class);

                    methods.add(new MethodDescriptor(method, annotation != null ? annotation.value() : null));
                }

                // Create list of getters, setters
                else if (child.getKind() == ElementKind.FIELD) {
                    Annotation childAnnotation;
                    if ((childAnnotation = child.getAnnotation(Get.class)) != null) {
                        Get get = (Get) childAnnotation;
                        accessors.add(new GetSetDescriptor(methods, true, false, false, child, get.dependsOn()));
                    } else if ((childAnnotation = child.getAnnotation(GetSet.class)) != null) {
                        GetSet getSet = (GetSet) childAnnotation;
                        accessors.add(new GetSetDescriptor(methods, true, true, false, child, getSet.dependsOn()));
                    } else if ((childAnnotation = child.getAnnotation(com.dellkan.robobinding.helpers.modelgen.Set.class)) != null) {
                        accessors.add(new GetSetDescriptor(methods, true, true, false, child, null));
                    } else if ((childAnnotation = child.getAnnotation(TwoStateGetSet.class)) != null) {
                        if (!Util.typeToString(child.asType()).equals("java.lang.Boolean")) {
                            messager.printMessage(Diagnostic.Kind.ERROR, "TwoStateGetSet only supports boolean fields!", child);
                        } else {
                            TwoStateGetSet twoStateGetSet = (TwoStateGetSet) childAnnotation;
                            accessors.add(new GetSetDescriptor(methods, true, true, true, child, twoStateGetSet.dependsOn()));
                        }
                    } else if ((childAnnotation = child.getAnnotation(ListItems.class)) != null) {
                        listItems.add(new ListItemsDescriptor(methods, child));
                    }

                    // Validation
                    for (Map.Entry<TypeElement, List<String>> markerAnnotation : customValidators.entrySet()) {
                        for (AnnotationMirror mirror : child.getAnnotationMirrors()) {
                            for (String annotationClass : markerAnnotation.getValue()) {
                                if (Util.typeToString(mirror.getAnnotationType()).equals(annotationClass)) {
                                    validators.add(new ValidateDescriptor(
                                            methods,
                                            annotationClass,
                                            markerAnnotation.getKey(),
                                            (VariableElement) child,
                                            child.getAnnotation(ValidateIf.class),
                                            processingEnv.getTypeUtils().isAssignable(
                                                    child.asType(),
                                                    processingEnv.getTypeUtils().getDeclaredType(
                                                            processingEnv.getElementUtils().getTypeElement(ListContainer.class.getCanonicalName()),
                                                            processingEnv.getTypeUtils().getWildcardType(null, null)
                                                    )
                                            )
                                    ));
                                    break;
                                }
                            }
                        }
                    }
                }
                AddToData addToDataAnnotation = null;
                if ((addToDataAnnotation = child.getAnnotation(AddToData.class)) != null) {
                    dataItems.add(new AddDataDescriptor(methods, accessors, child, addToDataAnnotation));
                }
            }

            input.put("accessors", accessors);
            input.put("methods", methods);
            input.put("validators", validators);
            input.put("listItems", listItems);
            input.put("dataItems", dataItems);

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
        return false;
    }

    @SafeVarargs
    private final void addCustomValidator(Class<? extends ValidationProcessor> processor, Class<? extends Annotation>... annotations) {
        List<String> annotationClasses = new ArrayList<>();
        for (Class<? extends Annotation> annotationClass : annotations) {
            annotationClasses.add(annotationClass.getCanonicalName());
        }
        customValidators.put(processingEnv.getElementUtils().getTypeElement(processor.getCanonicalName()), annotationClasses);
    }
}
