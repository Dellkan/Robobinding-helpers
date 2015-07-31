package com.dellkan.robobinding.helpers.validation;

import java.lang.annotation.Annotation;

public abstract class ValidationProcessor {
    protected Annotation annotation;
    public ValidationProcessor(Annotation annotation) {
        this.annotation = annotation;
    }

    public abstract boolean isValid(Object value);
    public abstract int getError(Object value);
}
