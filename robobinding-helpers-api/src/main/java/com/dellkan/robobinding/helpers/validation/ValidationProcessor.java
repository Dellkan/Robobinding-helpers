package com.dellkan.robobinding.helpers.validation;

import java.lang.annotation.Annotation;

/**
 * <p>Extend this class to create custom validation of your attributes.
 * At the moment, only one validation is possible per attribute.
 * Make a reference to your own custom annotation in {@link ValidateType}, put that on your attributes,
 * and this class will determine if that attribute is holding a valid value</p>
 */
public abstract class ValidationProcessor {
    /**
     * Your annotation, with appropriate values set. Must be cast to your own type.
     */
    protected Annotation annotation;

    /**
     * This constructor must be called, as it stores away your annotation with values.
     * @param annotation Used by engine to give us a reference to the annotation.
     */
    public ValidationProcessor(Annotation annotation) {
        this.annotation = annotation;
    }

    /**
     * Called by the engine to determine if the value of your annotated field is valid
     * @param value The new value of the field. We can't know what types of fields you'll annotate, so we can't
     *              assume any types for value.
     * @return true if the field has a valid value, or not
     */
    public abstract boolean isValid(Object value);

    /**
     * Determine the type of error if validation fails. This will usually be called directly, so
     * take care to evaluate through {@link #isValid}.
     * @param value The new value of the field. We can't know what types of fields you'll annotate, so we can't
     *              assume any types for value.
     * @return A @StringRes resource pointing to an error, or 0 if there is no error.
     */
    public abstract int getError(Object value);
}
