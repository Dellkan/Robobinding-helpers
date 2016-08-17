package com.dellkan.robobinding.helpers.validation.processors;

import com.dellkan.robobinding.helpers.validation.ValidationProcessor;
import com.dellkan.robobinding.helpers.validation.validators.ValidateLengthMax;
import com.dellkan.robobinding.helpers.validation.validators.ValidateLengthMin;
import com.dellkan.robobinding.helpers.validation.validators.ValidateLengthRange;
import com.dellkan.robobinding.helpers.validation.validators.ValidateMax;
import com.dellkan.robobinding.helpers.validation.validators.ValidateMin;
import com.dellkan.robobinding.helpers.validation.validators.ValidateRange;

import org.json.JSONObject;

import java.lang.annotation.Annotation;

public class ValidateLengthProcessor extends ValidationProcessor {
    @Override
    public boolean isValid(JSONObject config, Object value) {
        if (value == null) {
            return false;
        }

        String annotationType = config.optString("_annotation_type", ValidateLengthRange.class.getCanonicalName());

        int min = config.optInt("min");
        int max = config.optInt("max");

        if (annotationType.equals(ValidateLengthMin.class.getCanonicalName())) {
            return value.toString().length() >= min;
        } else if (annotationType.equals(ValidateLengthMax.class.getCanonicalName())) {
            return value.toString().length() <= max;
        } else if (annotationType.equals(ValidateLengthRange.class.getCanonicalName())) {
            return value.toString().length() >= min && value.toString().length() <= max;
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
                if (annotationType.equals(ValidateMin.class.getCanonicalName())) {
                    return number >= min;
                } else if (annotationType.equals(ValidateMax.class.getCanonicalName())) {
                    return number <= max;
                } else if (annotationType.equals(ValidateRange.class.getCanonicalName())) {
                    return number >= min && number <= max;
                }
            }
        }
        return false;
    }

    @Override
    public int getError(JSONObject config, Object value) {
        if (!isValid(config, value)) {
            return config.optInt("error", 0);
        } else {
            return 0;
        }
    }
}
