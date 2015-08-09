package com.dellkan.robobinding.helpers.validation.processors;

import com.dellkan.robobinding.helpers.validation.ValidationProcessor;
import com.dellkan.robobinding.helpers.validation.validators.ValidatePattern;

import java.lang.annotation.Annotation;

public class ValidatePatternProcessor extends ValidationProcessor {
    public ValidatePatternProcessor(Annotation annotation) {
        super(annotation);
    }

    @Override
    public boolean isValid(Object value) {
        String text = value != null ? value.toString() : "";
        return text.matches(((ValidatePattern)annotation).value());
    }

    @Override
    public int getError(Object value) {
        if (!isValid(value)) {
            return ((ValidatePattern) annotation).error();
        }
        return 0;
    }
}
