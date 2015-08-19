package com.dellkan.robobinding.helpers.validation.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
/**
 * Use this to validate that a {@link com.dellkan.robobinding.helpers.model.ListContainer ListContainer<>} has a selected element, OR that a {@link java.lang.Boolean Boolean} has a value (either true or false)
 */
public @interface ValidateSelected {
    int error() default 0;
}
