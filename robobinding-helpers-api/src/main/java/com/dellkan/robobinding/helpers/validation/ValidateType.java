package com.dellkan.robobinding.helpers.validation;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.CLASS)
public @interface ValidateType {
    Class<? extends Annotation> value();
}
