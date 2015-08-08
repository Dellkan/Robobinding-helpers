package com.dellkan.robobinding.helpers.validation.processors;

import com.dellkan.robobinding.helpers.validation.ValidateType;
import com.dellkan.robobinding.helpers.validation.ValidationProcessor;
import com.dellkan.robobinding.helpers.validation.validators.ValidateLength;

import java.lang.annotation.Annotation;

@ValidateType(ValidateLength.class)
public class ValidateLengthProcessor extends ValidationProcessor {
    private ValidateLength validateLength;
    public ValidateLengthProcessor(Annotation annotation) {
        super(annotation);
        validateLength = (ValidateLength) this.annotation;
    }

    @Override
    public boolean isValid(Object value) {
        if (String.class.isAssignableFrom(value.getClass())) {
            String text = value.toString();
            switch (validateLength.comparison()) {
                case LENGTH_EXACTLY:
                    return text.length() == validateLength.min();
                case LENGTH_RANGE_EXCLUDING:
                    return text.length() > validateLength.min() && text.length() < validateLength.max();
                case LENGTH_RANGE_INCLUDING:
                    return text.length() >= validateLength.min() && text.length() <= validateLength.max();
                default:
                    return false;
            }
        } else if (Number.class.isAssignableFrom(value.getClass())) {
            Double number = ((Number) value).doubleValue();
            switch (validateLength.comparison()) {
                case EXACTLY:
                    return number == validateLength.min();
                case RANGE_EXCLUDING:
                    return number > validateLength.min() && number < validateLength.max();
                case RANGE_INCLUDING:
                    return number >= validateLength.min() && number <= validateLength.max();
                default:
                    return false;
            }
        }
        return false;
    }

    @Override
    public int getError(Object value) {
        if (!isValid(value)) {
            return validateLength.error();
        } else {
            return 0;
        }
    }
}
