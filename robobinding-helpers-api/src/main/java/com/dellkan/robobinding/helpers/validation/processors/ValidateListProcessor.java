package com.dellkan.robobinding.helpers.validation.processors;

import com.dellkan.robobinding.helpers.model.ListContainer;
import com.dellkan.robobinding.helpers.validation.ValidationProcessor;
import com.dellkan.robobinding.helpers.validation.validators.ValidateList;

import java.lang.annotation.Annotation;

public class ValidateListProcessor extends ValidationProcessor {
    /**
     * This constructor must be called, as it stores away your annotation with values.
     *
     * @param annotation Used by engine to give us a reference to the annotation.
     */
    public ValidateListProcessor(Annotation annotation) {
        super(annotation);
    }

    @Override
    public boolean isValid(Object value) {
        if (value instanceof ListContainer) {
            return ((ListContainer) value).size() >= ((ValidateList) annotation).min();
        }
        return false;
    }

    @Override
    public int getError(Object value) {
        if (!isValid(value)) {
            return ((ValidateList) annotation).error();
        }
        return 0;
    }
}
