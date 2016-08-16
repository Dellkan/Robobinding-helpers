package com.dellkan.robobinding.helpers.processor.descriptors;

import com.dellkan.robobinding.helpers.model.ListContainer;
import com.dellkan.robobinding.helpers.processor.Util;
import com.dellkan.robobinding.helpers.validation.ValidateIf;
import com.dellkan.robobinding.helpers.validation.ValidateIfValue;
import com.dellkan.robobinding.helpers.validation.validators.Validate;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * Used by freemarker, during compile-time annotation processing.
 * Describes attributes marked with a custom validation through {@link com.dellkan.robobinding.helpers.validation.ValidationProcessor ValidationProcessor}
 * and {@link com.dellkan.robobinding.helpers.validation.ValidateType ValidateType}.
 */
public class ValidateDescriptor extends Descriptor {
    private ValidateIf validateIf;
    private ValidateIfValue validateIfValue;
    private boolean isList;

    private Validate methodAnnotation;
    private boolean isMethodValidation;

    public ValidateDescriptor(ModelDescriptor modelDescriptor, Element field) {
        super(modelDescriptor, field);
        this.validateIf = field.getAnnotation(ValidateIf.class);
        this.validateIfValue = field.getAnnotation(ValidateIfValue.class);

        ProcessingEnvironment processingEnv = modelDescriptor.getProcessingEnvironment();
        Types typeUtils = processingEnv.getTypeUtils();
        this.isMethodValidation = getField().getKind().equals(ElementKind.METHOD);

        if (this.isMethodValidation) {
            // Make sure our method validation returns a boolean value
            if (!((ExecutableElement)this.field).getReturnType().getKind().equals(TypeKind.BOOLEAN)) {
                modelDescriptor.getMessager().printMessage(Diagnostic.Kind.ERROR, "Methods annotated with @Validate must return a boolean value!", field);
            }

            methodAnnotation = getField().getAnnotation(Validate.class);
            if (this.methodAnnotation == null) {
                modelDescriptor.getMessager().printMessage(Diagnostic.Kind.ERROR, "Could not retrieve method validation annotation");
            }
        } else {
            this.isList = typeUtils.isAssignable(
                    field.asType(),
                    typeUtils.getDeclaredType(
                            processingEnv.getElementUtils().getTypeElement(ListContainer.class.getCanonicalName()),
                            typeUtils.getWildcardType(null, null)
                    )
            );
        }
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

    public List<String> getDependsOn() {
        List<String> dependencies = super.getDependsOn();

        String name = getName();
        name = name.substring(0, 1).toLowerCase() + name.substring(1);

        dependencies.add(name);


        if (isList) {
            dependencies.add(name + "Selected");
        }

        return dependencies;
    }

    public boolean getIsList() {
        return isList;
    }

    /*
        Helpers needed for  hasValidateIfValue
     */

    public int getMethodError() {
        if (isMethodValidation() && methodAnnotation != null) {
            return methodAnnotation.error();
        } else {
            return 0;
        }
    }

    public String getValidationNameValid() {
        StringBuilder builder = new StringBuilder();
        if (isMethodValidation) {
            if (!getName().startsWith("is")) {
                builder.append("is");
            }
            builder.append(getName());
            if (!getName().endsWith("Valid")) {
                builder.append("Valid");
            }
        } else {
            builder.append("is").append(getName()).append("Valid");
        }
        return builder.toString();
    }

    public String getValidationNameInvalid() {
        StringBuilder builder = new StringBuilder();
        if (isMethodValidation) {
            if (!getName().startsWith("is")) {
                builder.append("is");
            }
            if (getName().endsWith("Valid")) {
                builder.append(getName().substring(0, getName().length() - "Valid".length()));
            } else {
                builder.append(getName());
            }
            if (!getName().endsWith("Invalid")) {
                builder.append("Invalid");
            }
        } else {
            builder.append("is").append(getName()).append("Invalid");
        }
        return builder.toString();
    }

    public boolean isMethodValidation() {
        return isMethodValidation;
    }

    public boolean selfDependencyNecessary(String dependency) {
        if (isMethodValidation()) {
            if (getName().equalsIgnoreCase(dependency)) {
                return false;
            }
        }
        return true;
    }

    private List<SubValidateDescriptor> subValidateDescriptors = new ArrayList<>();
    public List<SubValidateDescriptor> getValidators() {
        return subValidateDescriptors;
    }

    public void addValidator(SubValidateDescriptor subValidateDescriptor) {
        subValidateDescriptors.add(subValidateDescriptor);
    }
}
