package com.dellkan.robobinding.helpers.validation.processors;

import com.dellkan.robobinding.helpers.model.ListContainer;
import com.dellkan.robobinding.helpers.validation.ValidationProcessor;
import com.dellkan.robobinding.helpers.validation.validators.ValidateList;

import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.util.Map;

public class ValidateListProcessor extends ValidationProcessor {
    @Override
    public boolean isValid(JSONObject config, Object value) {
        if (value instanceof ListContainer) {
            return ((ListContainer) value).size() >= config.optInt("min");
        }
        return false;
    }

    @Override
    public int getError(JSONObject config, Object value) {
        if (!isValid(config, value)) {
            return config.optInt("error");
        }
        return 0;
    }
}
