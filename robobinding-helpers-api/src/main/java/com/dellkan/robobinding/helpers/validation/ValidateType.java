package com.dellkan.robobinding.helpers.validation;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This will allow you to create custom {@link com.dellkan.robobinding.helpers.validation.ValidationProcessor ValidationProcessor} to validate your attributes.
 * Must be put on a class that extends {@link com.dellkan.robobinding.helpers.validation.ValidationProcessor ValidationProcessor}.
 * @see com.dellkan.robobinding.helpers.validation.ValidationProcessor
 */
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.CLASS)
public @interface ValidateType {
    /**
     * Your custom annotation. Put your annotation on an attribute on your {@link com.dellkan.robobinding.helpers.modelgen.PresentationModel PresentationModel},
     * and {@link com.dellkan.robobinding.helpers.validation.ValidationProcessor#isValid(Object) ValidationProcessor.isValid} will run to validate it
     */
    Class<? extends Annotation> value();
}
