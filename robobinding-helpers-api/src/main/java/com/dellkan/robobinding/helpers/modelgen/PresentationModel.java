package com.dellkan.robobinding.helpers.modelgen;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a model with this annotation to make it get picked up during compile-time for processing.<br />
 * All models with this annotation can use {@link Get} and {@link GetSet} to get auto-generated getters/setters.<br />
 * By extending Wrapper, you'll also get some convenience methods available on your model.<br />
 * <br />
 * @see com.dellkan.robobinding.helpers.validation.ValidateType @ValidateType
 * @see com.dellkan.robobinding.helpers.validation.ValidationProcessor ValidationProcessor
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface PresentationModel {
}
