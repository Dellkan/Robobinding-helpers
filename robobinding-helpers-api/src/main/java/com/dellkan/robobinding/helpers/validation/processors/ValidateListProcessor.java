package com.dellkan.robobinding.helpers.validation.processors;

import com.dellkan.robobinding.helpers.model.ListContainer;
import com.dellkan.robobinding.helpers.validation.ValidationProcessor;
import com.dellkan.robobinding.helpers.validation.validators.ValidateList;

import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.util.Map;

public class ValidateListProcessor extends ValidationProcessor {
    private int min;
    public ValidateListProcessor(JSONObject config) {
        super(config);
        this.min = config.optInt("min");
    }

    @Override
    public boolean isValid(Object value) {
        if (value instanceof ListContainer) {
            return ((ListContainer) value).size() >= min;
        }
        return false;
    }

    @Override
    public int getError(Object value) {
        if (!isValid(value)) {
            return this.error;
        }
        return 0;
    }
}
