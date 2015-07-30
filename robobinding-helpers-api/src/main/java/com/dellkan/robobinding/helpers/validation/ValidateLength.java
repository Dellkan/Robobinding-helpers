package com.dellkan.robobinding.helpers.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.CLASS)
public @interface ValidateLength {
    ComparisonTypes comparison() default ComparisonTypes.LENGTH_EXACTLY;
    int min();
    int max() default 0;
    int error() default 0;
}
