package com.dellkan.robobinding.helpers.modelgen;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a field on your {@link PresentationModel} to get a getter auto-generated for it.
 * boolean fields get is{Name} instead of get{Name}. Numeric types are converted to strings.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface Get {
}
