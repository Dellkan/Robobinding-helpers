package com.dellkan.robobinding.helpers.processor.descriptors;

import com.dellkan.robobinding.helpers.processor.Util;
import com.dellkan.robobinding.helpers.validation.ValidateIf;
import com.dellkan.robobinding.helpers.validation.ValidateIfValue;
import com.dellkan.robobinding.helpers.validation.validators.ValidateList;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

public class SubValidateDescriptor extends Descriptor {
    private String annotationClass;
    private TypeElement processor;
    private ValidateIf validateIf;
    private ValidateIfValue validateIfValue;
    private ValidateDescriptor parent;

    public SubValidateDescriptor(ModelDescriptor modelDescriptor, Element field, ValidateDescriptor parent, String annotationClass, TypeElement processor) {
        super(modelDescriptor, field);
        this.parent = parent;

        this.annotationClass = annotationClass;
        this.processor = processor;
        this.validateIf = field.getAnnotation(ValidateIf.class);
        this.validateIfValue = field.getAnnotation(ValidateIfValue.class);

        if (!parent.isMethodValidation() && this.processor == null) {
            modelDescriptor.getMessager().printMessage(Diagnostic.Kind.ERROR, "processor null on a non-methodValidation");
        }
    }

    public String getProcessorType() {
        return Util.typeToString(this.processor.asType());
    }

    public TypeElement getProcessor() {
        return this.processor;
    }

    public String getAnnotationType() {
        return annotationClass;
    }

    /**
     *
     * @return Validation config values, formatted as JSON
     */
    public String getConfigValues() {
        Map<String, Object> values = new HashMap<>();

        // Look at all the annotations for the field
        for (AnnotationMirror mirror : field.getAnnotationMirrors()) {
            // Find the validation annotation we're looking for
            if (mirror.getAnnotationType().asElement().asType().toString().equals(annotationClass)) {
                // Iterate through and convert its values. Note - you HAVE to use getElementUtils().getElementValuesWithDefaults(mirror), otherwise, you lose all annotation defaults!
                for(Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : modelDescriptor.getProcessingEnvironment().getElementUtils().getElementValuesWithDefaults(mirror).entrySet()) {
                    Object defaultValue = entry.getKey().getDefaultValue() != null ? entry.getKey().getDefaultValue().getValue() : null;
                    Object value = entry.getValue().getValue();

                    values.put(entry.getKey().getSimpleName().toString(), JSONObject.wrap(value != null ? value : defaultValue));
                }
            }
        }

        return new JSONObject(values).toString();
    }
}
