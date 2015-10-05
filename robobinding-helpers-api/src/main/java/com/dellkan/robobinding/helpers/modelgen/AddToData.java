package com.dellkan.robobinding.helpers.modelgen;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface AddToData {
    String alternativeName() default "";
    String alternativeDataFormatter() default "";
    String[] group() default "";
    String onlyIf() default "";
}
