package com.dellkan.robobinding.helpers.validation;

import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Extend this class to create custom validation of your attributes.
 * At the moment, only one validation is possible per attribute.
 * Make a reference to your own custom annotation in {@link ValidateType}, put that on your attributes,
 * and this class will determine if that attribute is holding a valid value</p>
 */
public abstract class ValidationProcessor {
    /**
     * Called by the engine to determine if the value of your annotated field is valid
     * @param value The new value of the field. We can't know what types of fields you'll annotate, so we can't
     *              assume any types for value.
     * @return true if the field has a valid value, or not
     */
    public abstract boolean isValid(JSONObject config, Object value);

    /**
     * Determine the type of error if validation fails. This will usually be called directly, so
     * take care to evaluate through {@link #isValid}.
     * @param value The new value of the field. We can't know what types of fields you'll annotate, so we can't
     *              assume any types for value.
     * @return A @StringRes resource pointing to an error, or 0 if there is no error.
     */
    public abstract int getError(JSONObject config, Object value);

    private static Map<String, ValidationProcessor> processors = new HashMap<>();
    public static ValidationProcessor getProcessor(String type, ValidationProcessor createIfMissing) {
        if (createIfMissing != null) {
            processors.put(type, createIfMissing);
        }
        return processors.get(type);
    }

    public static boolean processorExists(String type) {
        return processors.containsKey(type);
    }
}
