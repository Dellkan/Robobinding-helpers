package com.dellkan.robobinding.helpers.validation.processors;

import com.dellkan.robobinding.helpers.model.ListContainer;
import com.dellkan.robobinding.helpers.model.ListItemWithValidation;
import com.dellkan.robobinding.helpers.validation.ValidationProcessor;
import com.dellkan.robobinding.helpers.validation.validators.ValidateSelected;

import java.lang.annotation.Annotation;

public class ValidateSelectedProcessor extends ValidationProcessor {
    /**
     * This constructor must be called, as it stores away your annotation with values.
     *
     * @param annotation Used by engine to give us a reference to the annotation.
     */
    public ValidateSelectedProcessor(Annotation annotation) {
        super(annotation);
    }

    @Override
    public boolean isValid(Object value) {
        if (value instanceof ListContainer) {
            Object selectedValue = ((ListContainer) value).getSelectedItem();
            if (selectedValue instanceof ListItemWithValidation) {
                return ((ListItemWithValidation) selectedValue).isValid((ListContainer) value);
            }
            return ((ListContainer) value).getSelectedItem() != null;
        } else {
            // If value isn't a ListContainer, we're gonna assume it's a boolean (checkbox, radios and the like). As such,
            // we're only really interested in whether a value is set at all
            return value != null;
        }
    }

    @Override
    public int getError(Object value) {
        if (!isValid(value)) {
            return ((ValidateSelected) annotation).error();
        }
        return 0;
    }
}
