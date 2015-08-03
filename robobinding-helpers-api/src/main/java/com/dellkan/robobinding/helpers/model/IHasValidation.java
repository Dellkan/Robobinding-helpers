package com.dellkan.robobinding.helpers.model;

import java.util.Map;

/**
 * <p>Helper functions to allow you to validate your model even while lacking access to the individually generated validator methods.</p>
 * {@link Wrapper} implements this so you don't have to. Extend {@link Wrapper} to get these for free.
 */
public interface IHasValidation {
    /**
     * Are all the fields on this model valid? Uses your custom annotations and processor to determine.
     * @return true if they are all valid, false if one or more fails validation.
     *
     * @see com.dellkan.robobinding.helpers.validation.ValidateType ValidateType
     * @see com.dellkan.robobinding.helpers.validation.ValidationProcessor ValidationProcessor
     */
    public boolean isValid();

    /**
     * Is this specific field valid? Uses your custom annotations and processor to determine.
     * @param field Field name that you want to check validity for
     * @return true if it is valid, false if it is not.
     *
     * @see com.dellkan.robobinding.helpers.validation.ValidateType ValidateType
     * @see com.dellkan.robobinding.helpers.validation.ValidationProcessor ValidationProcessor
     */
    public boolean isValid(String field);

    /**
     * Returns a map containing all the errors. Key in map is field name, the value is the @StringRes resource pointing to the error
     * @return Map of all errors from model (all fields)
     *
     * @see com.dellkan.robobinding.helpers.validation.ValidationProcessor#getError(Object)  ValidationProcessor.getError(Object)
     */
    public Map<String, Integer> getErrors();

    /**
     * Get the error for a single field. Used when you know the field name, but can't be arsed to get the generated model.
     * @param field Field name you want to retrieve error for
     * @return {@literal @}StringRes resource pointing to chosen error for this field.
     */
    public int getError(String field);
}
