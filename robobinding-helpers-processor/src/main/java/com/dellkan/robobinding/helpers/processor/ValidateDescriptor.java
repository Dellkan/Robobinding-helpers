package com.dellkan.robobinding.helpers.processor;

import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * Used by freemarker, during compile-time annotation processing.
 * Describes attributes marked with a custom validation through {@link com.dellkan.robobinding.helpers.validation.ValidationProcessor ValidationProcessor}
 * and {@link com.dellkan.robobinding.helpers.validation.ValidateType ValidateType}.
 */
public class ValidateDescriptor extends Descriptor {
    private String annotation;
    private TypeElement element;
    private VariableElement childWithAnnotation;

    public ValidateDescriptor(List<MethodDescriptor> methods, String annotation, TypeElement element, VariableElement childWithAnnotation) {
        super(methods);
        this.annotation = annotation;
        this.element = element;
        this.childWithAnnotation = childWithAnnotation;
    }

    public String getProcessorType() {
        return Util.typeToString(this.element.asType());
    }

    public String getAnnotationType() {
        return annotation;
    }

    public String getName() {
        return childWithAnnotation.getSimpleName().toString();
    }
}
