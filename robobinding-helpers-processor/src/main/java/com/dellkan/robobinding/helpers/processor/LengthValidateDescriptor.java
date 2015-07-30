package com.dellkan.robobinding.helpers.processor;

import com.dellkan.robobinding.helpers.validation.ValidateLength;

import javax.lang.model.element.Element;

public class LengthValidateDescriptor {
    private ValidateLength annotation;
    private Element element;

    public LengthValidateDescriptor(ValidateLength annotation, Element element) {
        this.annotation = annotation;
        this.element = element;
    }

    public String getName() {
        return element.getSimpleName().toString();
    }

    public String getEvaluationExpression() {
        String field = "this.data." + getName();
        switch (annotation.comparison()) {
            case LENGTH_EXACTLY:
                return field + ".length() == " + getMin();
            case LENGTH_RANGE_EXCLUDING:
                return field + ".length() > " + getMin() + " && " + field + ".length() < " + getMax();
            case LENGTH_RANGE_INCLUDING:
                return field + ".length() >= " + getMin() + " && " + field + ".length() <= " + getMax();
            case EXACTLY:
                return field + " == " + getMin();
            case RANGE_EXCLUDING:
                return field + " > " + getMin() + " && " + field + " < " + getMax();
            case RANGE_INCLUDING:
                return field + " >= " + getMin() + " && " + field + " <= " + getMax();
        }
        return "";
    }

    public int getMin() {
        return annotation.min();
    }

    public int getMax() {
        return annotation.max();
    }

    public String getErrorResource() {
        return Integer.toString(annotation.error());
    }
}
