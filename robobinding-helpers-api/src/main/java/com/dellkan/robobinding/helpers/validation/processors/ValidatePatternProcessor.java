package com.dellkan.robobinding.helpers.validation.processors;

import com.dellkan.robobinding.helpers.validation.ValidationProcessor;
import com.dellkan.robobinding.helpers.validation.validators.ValidatePattern;

import org.json.JSONObject;

import java.lang.annotation.Annotation;

public class ValidatePatternProcessor extends ValidationProcessor {
    @Override
    public boolean isValid(JSONObject config, Object value) {
        String text = value != null ? value.toString() : "";
        return text.matches(config.optString("value", ""));
    }

    @Override
    public int getError(JSONObject config, Object value) {
        if (!isValid(config, value)) {
            return config.optInt("error");
        }
        return 0;
    }
}
