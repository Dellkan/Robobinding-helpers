package com.dellkan.robobinding.helpers.validation.processors;

import com.dellkan.robobinding.helpers.validation.ValidationProcessor;

import org.json.JSONObject;

public class ValidateBooleanProcessor extends ValidationProcessor {
    private boolean value;
    public ValidateBooleanProcessor(JSONObject config) {
        super(config);
        this.value = config.optBoolean("value", true);
        this.error = config.optInt("error", 0);
    }

    @Override
    public boolean isValid(Object value) {
        if (Boolean.class.isAssignableFrom(value.getClass())) {
            Boolean converted = (Boolean) value;
            return converted == this.value;
        }
        return false;
    }

    @Override
    public int getError(Object value) {
        if (!isValid(value)) {
            return error;
        }
        return 0;
    }
}
