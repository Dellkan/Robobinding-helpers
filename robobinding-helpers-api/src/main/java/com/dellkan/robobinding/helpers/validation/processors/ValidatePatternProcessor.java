package com.dellkan.robobinding.helpers.validation.processors;

import com.dellkan.robobinding.helpers.validation.ValidationProcessor;
import com.dellkan.robobinding.helpers.validation.validators.ValidatePattern;

import org.json.JSONObject;

import java.lang.annotation.Annotation;

public class ValidatePatternProcessor extends ValidationProcessor {
    private String value;
    public ValidatePatternProcessor(JSONObject config) {
        super(config);
        value = config.optString("value", "");
    }

    @Override
    public boolean isValid(Object value) {
        String text = value != null ? value.toString() : "";
        return text.matches(this.value);
    }

    @Override
    public int getError(Object value) {
        if (!isValid(value)) {
            return this.error;
        }
        return 0;
    }
}
