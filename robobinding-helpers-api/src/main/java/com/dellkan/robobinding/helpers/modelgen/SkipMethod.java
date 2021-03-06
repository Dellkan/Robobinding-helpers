package com.dellkan.robobinding.helpers.modelgen;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @deprecated Simply omit annotation on methods instead.
 * Previously, builds would assume all methods should be included unless skipped.
 * New builds will assume no methods should be included unless explicitly included using {@link PresentationMethod}
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface SkipMethod {
}
