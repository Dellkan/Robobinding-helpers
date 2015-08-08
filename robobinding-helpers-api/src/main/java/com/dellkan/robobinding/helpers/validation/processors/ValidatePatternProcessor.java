package com.dellkan.robobinding.helpers.validation.processors;

import com.dellkan.robobinding.helpers.validation.ValidateType;
import com.dellkan.robobinding.helpers.validation.ValidationProcessor;
import com.dellkan.robobinding.helpers.validation.validators.ValidatePattern;

import java.lang.annotation.Annotation;

@ValidateType(ValidatePattern.class)
public class ValidatePatternProcessor extends ValidationProcessor {
    public ValidatePatternProcessor(Annotation annotation) {
        super(annotation);
    }

    @Override
    public boolean isValid(Object value) {
        return value.toString().matches(((ValidatePattern)annotation).value());
    }

    @Override
    public int getError(Object value) {
        return 0;
    }
}
