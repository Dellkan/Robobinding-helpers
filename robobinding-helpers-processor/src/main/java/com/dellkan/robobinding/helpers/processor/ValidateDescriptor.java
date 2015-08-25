package com.dellkan.robobinding.helpers.processor;

import com.dellkan.robobinding.helpers.validation.ValidateIf;
import com.dellkan.robobinding.helpers.validation.ValidateIfValue;

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
    private TypeElement processor;
    private VariableElement childWithAnnotation;
    private ValidateIf validateIf;
    private ValidateIfValue validateIfValue;
    private boolean isList;

    public ValidateDescriptor(List<MethodDescriptor> methods, String annotation, TypeElement processor, VariableElement childWithAnnotation, ValidateIf validateIf, ValidateIfValue validateIfValue, boolean isList) {
        super(methods);
        this.annotation = annotation;
        this.processor = processor;
        this.childWithAnnotation = childWithAnnotation;
        this.validateIf = validateIf;
        this.validateIfValue = validateIfValue;
        this.isList = isList;
    }

    public String getProcessorType() {
        return Util.typeToString(this.processor.asType());
    }

    public String getAnnotationType() {
        return annotation;
    }

    public String getName() {
        return childWithAnnotation.getSimpleName().toString();
    }

    public boolean getHasValidateIf() {
        return validateIf != null;
    }

    public boolean getHasValidateIfValue() {
        return validateIfValue != null;
    }

    public String getValidateIf() {
        return validateIf.value();
    }

    public String[] getDependsOn() {
        return validateIf != null ? validateIf.dependsOn() : new String[]{};
    }

    public boolean getIsList() {
        return isList;
    }

    /*
        Helpers needed for  hasValidateIfValue
     */
    public String getType() {
        return Util.typeToString(childWithAnnotation.asType());
    }

    public boolean isBoolean() {
        return Util.typeToString(childWithAnnotation.asType()).equals("java.lang.Boolean");
    }

    public boolean isNumeric() {
        String type = Util.typeToString(childWithAnnotation.asType());
        switch (type) {
            case "java.lang.Integer":
            case "java.lang.Float":
            case "java.lang.Double":
            case "java.lang.Long":
                return true;
            default:
                return false;
        }
    }

    public boolean isString() {
        return Util.typeToString(childWithAnnotation.asType()).equals("java.lang.String");
    }
}
