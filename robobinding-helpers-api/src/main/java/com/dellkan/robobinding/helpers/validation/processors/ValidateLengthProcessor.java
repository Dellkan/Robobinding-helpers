package com.dellkan.robobinding.helpers.validation.processors;

import com.dellkan.robobinding.helpers.validation.ValidationProcessor;
import com.dellkan.robobinding.helpers.validation.validators.ValidateLengthMax;
import com.dellkan.robobinding.helpers.validation.validators.ValidateLengthMin;
import com.dellkan.robobinding.helpers.validation.validators.ValidateLengthRange;
import com.dellkan.robobinding.helpers.validation.validators.ValidateMax;
import com.dellkan.robobinding.helpers.validation.validators.ValidateMin;
import com.dellkan.robobinding.helpers.validation.validators.ValidateRange;

import java.lang.annotation.Annotation;

public class ValidateLengthProcessor extends ValidationProcessor {
    private int mError = 0;
    public ValidateLengthProcessor(Annotation annotation) {
        super(annotation);
        if (annotation instanceof ValidateLengthMin) {
            mError = ((ValidateLengthMin) annotation).error();
        } else if (annotation instanceof ValidateLengthMax) {
            mError = ((ValidateLengthMax) annotation).error();
        } else if (annotation instanceof ValidateLengthRange) {
            mError = ((ValidateLengthRange) annotation).error();
        } else if (annotation instanceof ValidateMin) {
            mError = ((ValidateMin) annotation).error();
        } else if (annotation instanceof ValidateMax) {
            mError = ((ValidateMax) annotation).error();
        } else if (annotation instanceof ValidateRange) {
            mError = ((ValidateRange) annotation).error();
        }
    }

    @Override
    public boolean isValid(Object value) {
        if (value == null) {
            return false;
        }

        if (annotation instanceof ValidateLengthMin) {
            return value.toString().length() >= ((ValidateLengthMin) annotation).min();
        } else if (annotation instanceof ValidateLengthMax) {
            return value.toString().length() <= ((ValidateLengthMax) annotation).max();
        } else if (annotation instanceof ValidateLengthRange) {
            return value.toString().length() >= ((ValidateLengthRange) annotation).min() && value.toString().length() <= ((ValidateLengthRange) annotation).max();
        } else { // Number validation types
            Double number = null;
            if (Number.class.isAssignableFrom(value.getClass())) {
                number = ((Number) value).doubleValue();
            } else if (String.class.isAssignableFrom(value.getClass())) {
                try {
                    number = Double.parseDouble(value.toString());
                } catch (NullPointerException e) {

                } catch (NumberFormatException e) {

                }
            }
            if (number != null) {
                if (annotation instanceof ValidateMin) {
                    return number >= ((ValidateMin) annotation).min();
                } else if (annotation instanceof ValidateMax) {
                    return number <= ((ValidateMax) annotation).max();
                } else if (annotation instanceof ValidateRange) {
                    return number >= ((ValidateRange) annotation).min() && number <= ((ValidateRange) annotation).max();
                }
            }
        }
        return false;
    }

    @Override
    public int getError(Object value) {
        if (!isValid(value)) {
            return mError;
        } else {
            return 0;
        }
    }
}
