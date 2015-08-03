package com.dellkan.robobinding.helpers.processor;

import com.dellkan.robobinding.helpers.modelgen.Get;
import com.dellkan.robobinding.helpers.modelgen.GetSet;
import com.dellkan.robobinding.helpers.modelgen.PresentationModel;
import com.dellkan.robobinding.helpers.validation.ValidateType;

import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
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
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
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
@SupportedAnnotationTypes({"com.dellkan.robobinding.helpers.modelgen.PresentationModel", "com.dellkan.robobinding.helpers.validation.ValidationType"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class Processor extends AbstractProcessor {
    private Messager messager;
    private Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);

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
        Map<String, TypeElement> customValidators = new HashMap<>();
        for (Element element : roundEnv.getElementsAnnotatedWith(ValidateType.class)) {
            if (element.getKind().isClass()) {
                ValidateType annotation = element.getAnnotation(ValidateType.class);
                String annotationClass = "";
                try {
                     annotationClass = annotation.value().getCanonicalName();
                } catch (MirroredTypeException e) {
                    annotationClass = Util.typeToString(e.getTypeMirror());
                }

                customValidators.put(annotationClass, (TypeElement) element);
                messager.printMessage(Diagnostic.Kind.NOTE, "Validatorprocessor[" + annotationClass + "] added", element);
            }
        }
        for (Element element : roundEnv.getElementsAnnotatedWith(PresentationModel.class)) {
            Map<String, Object> input = new HashMap<>();

            List<MethodDescriptor> methods = new ArrayList<>();
            List<GetSetDescriptor> accessors = new ArrayList<>();
            List<ValidateDescriptor> validators = new ArrayList<>();

            for (Element child : element.getEnclosedElements()) {
                // Create list of existing methods
                if (child.getKind() == ElementKind.METHOD && child.getModifiers().contains(Modifier.PUBLIC)) {
                    Set<Modifier> modifiers = child.getModifiers();
                    ExecutableElement method = (ExecutableElement) child;

                    methods.add(new MethodDescriptor(method));
                }

                // Create list of getters, setters
                else if (child.getKind() == ElementKind.FIELD) {
                    Annotation childAnnotation;
                    if ((childAnnotation = child.getAnnotation(Get.class)) != null) {
                        accessors.add(new GetSetDescriptor(false, child, null));
                    }
                    if ((childAnnotation = child.getAnnotation(GetSet.class)) != null) {
                        GetSet getSet = (GetSet) childAnnotation;
                        accessors.add(new GetSetDescriptor(true, child, getSet.dependsOn()));
                    }

                    // Validation
                    for (Map.Entry<String, TypeElement> markerAnnotation : customValidators.entrySet()) {
                        for (AnnotationMirror mirror : child.getAnnotationMirrors()) {
                            if (Util.typeToString(mirror.getAnnotationType()).equals(markerAnnotation.getKey())) {
                                messager.printMessage(Diagnostic.Kind.NOTE, "Field with annotation " + markerAnnotation.getKey() + " found", child);
                                validators.add(new ValidateDescriptor(markerAnnotation.getKey(), markerAnnotation.getValue(), (VariableElement) child));
                                break;
                            }
                        }
                    }
                }
            }

            input.put("accessors", accessors);
            input.put("methods", methods);
            input.put("validators", validators);

            try {
                // Create target file
                JavaFileObject file = processingEnv.getFiler().createSourceFile(Util.elementToString(element) + "$$Helper");
                Writer out = file.openWriter();

                // Get template
                Template template = cfg.getTemplate("ClassDef.ftl");

                // Fill in variables
                input.put("className", element.getSimpleName());
                input.put("package", Util.getPackage(element));

                // Process template
                template.process(input, out);

                // Write
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
