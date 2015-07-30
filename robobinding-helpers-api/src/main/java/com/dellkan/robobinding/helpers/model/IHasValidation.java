package com.dellkan.robobinding.helpers.model;

import java.util.Map;

public interface IHasValidation {
    public boolean isValid();
    public boolean isValid(String field);
    public Map<String, Integer> getErrors();
    public int getError(String field);
}
