package com.dellkan.robobinding.helpers.validation.processors;

import com.dellkan.robobinding.helpers.validation.ValidateType;
import com.dellkan.robobinding.helpers.validation.ValidationProcessor;
import com.dellkan.robobinding.helpers.validation.validators.ValidateBoolean;

import java.lang.annotation.Annotation;

@ValidateType(ValidateBoolean.class)
public class ValidateBooleanProcessor extends ValidationProcessor {
    public ValidateBooleanProcessor(Annotation annotation) {
        super(annotation);
    }

    @Override
    public boolean isValid(Object value) {
        if (Boolean.class.isAssignableFrom(value.getClass())) {
            Boolean converted = (Boolean) value;
            return converted == ((ValidateBoolean)annotation).value();
        }
        return false;
    }

    @Override
    public int getError(Object value) {
        return 0;
    }
}
