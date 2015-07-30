package com.dellkan.robobinding.helpers.processor;

import com.dellkan.robobinding.helpers.modelgen.Get;
import com.dellkan.robobinding.helpers.modelgen.GetSet;
import com.dellkan.robobinding.helpers.modelgen.PresentationModel;
import com.dellkan.robobinding.helpers.validation.ValidateLength;

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
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@SupportedAnnotationTypes({"com.dellkan.robobinding.helpers.modelgen.PresentationModel"})
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
        for (Element element : roundEnv.getElementsAnnotatedWith(PresentationModel.class)) {
            messager.printMessage(Diagnostic.Kind.NOTE, "Found element", element);

            Map<String, Object> input = new HashMap<>();

            List<MethodDescriptor> methods = new ArrayList<>();
            List<GetSetDescriptor> accessors = new ArrayList<>();
            List<LengthValidateDescriptor> validators = new ArrayList<>();

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
                    if ((childAnnotation = child.getAnnotation(ValidateLength.class)) != null) {
                        ValidateLength validateLength = (ValidateLength) childAnnotation;
                        validators.add(new LengthValidateDescriptor(validateLength, child));
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
