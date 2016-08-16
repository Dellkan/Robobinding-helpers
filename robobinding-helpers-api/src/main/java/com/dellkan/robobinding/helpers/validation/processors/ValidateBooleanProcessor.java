package com.dellkan.robobinding.helpers.validation.processors;

import com.dellkan.robobinding.helpers.validation.ValidationProcessor;

import org.json.JSONObject;

public class ValidateBooleanProcessor extends ValidationProcessor {
    @Override
    public boolean isValid(JSONObject config, Object value) {
        if (Boolean.class.isAssignableFrom(value.getClass())) {
            Boolean converted = (Boolean) value;
            return converted == config.optBoolean("value", true);
        }
        return false;
    }

    @Override
    public int getError(JSONObject config, Object value) {
        if (!isValid(config, value)) {
            return config.optInt("error", 0);
        }
        return 0;
    }
}
