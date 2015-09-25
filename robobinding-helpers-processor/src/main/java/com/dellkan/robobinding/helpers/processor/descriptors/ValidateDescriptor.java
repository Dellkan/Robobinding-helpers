package com.dellkan.robobinding.helpers.processor.descriptors;

import com.dellkan.robobinding.helpers.processor.Util;
import com.dellkan.robobinding.helpers.validation.ValidateIf;
import com.dellkan.robobinding.helpers.validation.ValidateIfValue;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 * Used by freemarker, during compile-time annotation processing.
 * Describes attributes marked with a custom validation through {@link com.dellkan.robobinding.helpers.validation.ValidationProcessor ValidationProcessor}
 * and {@link com.dellkan.robobinding.helpers.validation.ValidateType ValidateType}.
 */
public class ValidateDescriptor extends Descriptor {
    private String annotation;
    private TypeElement processor;
    private ValidateIf validateIf;
    private ValidateIfValue validateIfValue;
    private boolean isList;

    public ValidateDescriptor(ModelDescriptor modelDescriptor, Element field, String annotation, TypeElement processor, ValidateIf validateIf, ValidateIfValue validateIfValue, boolean isList) {
        super(modelDescriptor, field);
        this.annotation = annotation;
        this.processor = processor;
        this.validateIf = validateIf;
        this.validateIfValue = validateIfValue;
        this.isList = isList;
    }

    public String getProcessorType() {
        return Util.typeToString(this.processor.asType());
    }

    public TypeElement getProcessor() {
        return this.processor;
    }

    public String getAnnotationType() {
        return annotation;
    }

    public String getAccessorClass() {
        if (!getClassPrefix().isEmpty()) {
            // Get rid of the dot at the end of prefix by applying substring
            return "this.data." + getClassPrefix().substring(0, getClassPrefix().length() - 1);
        }
        return "this.data";
    }

    public boolean getHasValidateIf() {
        return validateIf != null;
    }

    public boolean getHasValidateIfValue() {
        return validateIfValue != null;
    }

    public ValidateIfValue getValidateIfValueAnnotation() {
        return this.validateIfValue;
    }

    public String getValidateIf() {
        return validateIf.value();
    }

    public ValidateIf getValidateIfAnnotation() {
        return this.validateIf;
    }

    public String[] getDependsOn() {
        List<String> dependencies = new ArrayList<>();
        String prefix = getPrefix();
        String name = getName();

        name = name.substring(0, 1).toLowerCase() + name.substring(1);

        dependencies.add(name);
        if (isList) {
            dependencies.add(name + "Selected");
        }

        if (validateIf != null) {
            for (String dependency : validateIf.dependsOn()) {
                if (prefix.length() > 1) {
                    dependencies.add(prefix.substring(0, 1).toLowerCase() + prefix.substring(1) + dependency.substring(0, 1).toUpperCase() + dependency.substring(1));
                } else {
                    dependencies.add(dependency);
                }
            }
        }

        String[] processedDependencies = new String[dependencies.size()];
        dependencies.toArray(processedDependencies);
        return processedDependencies;
    }

    public boolean getIsList() {
        return isList;
    }

    /*
        Helpers needed for  hasValidateIfValue
     */

    public boolean isBoolean() {
        return getType().equals("java.lang.Boolean");
    }

    public boolean isNumeric() {
        switch (getType()) {
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
        return getType().equals("java.lang.String");
    }
}
