package com.dellkan.robobinding.helpers.processor;

import com.dellkan.robobinding.helpers.modelgen.IncludeModel;

import javax.lang.model.element.Element;

public class IncludeModelDescriptor {
    private IncludeModel annotation;
    private Element field;
    private Element fieldClassElement;

    public IncludeModelDescriptor(IncludeModel annotation, Element field, Element fieldClassElement) {
        this.annotation = annotation;
        this.field = field;
        this.fieldClassElement = fieldClassElement;
    }

    public IncludeModel getAnnotation() {
        return annotation;
    }

    public Element getField() {
        return field;
    }

    public Element getFieldClassElement() {
        return fieldClassElement;
    }

    public boolean shouldSkipField(String fieldName) {
        for (String exclude : annotation.excludeFields()) {
            if (exclude.equals(fieldName)) {
                return true;
            }
        }
        return false;
    }
}
