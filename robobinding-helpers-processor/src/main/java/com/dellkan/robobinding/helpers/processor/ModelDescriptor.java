package com.dellkan.robobinding.helpers.processor;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;

public class ModelDescriptor {
    Element model = null;
    List<MethodDescriptor> methods = new ArrayList<>();
    List<GetSetDescriptor> accessors = new ArrayList<>();
    List<ValidateDescriptor> validators = new ArrayList<>();
    List<ListItemsDescriptor> listItems = new ArrayList<>();
    List<AddToDataDescriptor> dataItems = new ArrayList<>();
    List<IncludeModelDescriptor> includeItems = new ArrayList<>();
    boolean writtenToFile = false;

    public ModelDescriptor(Element model) {
        this.model = model;
    }

    public boolean methodExists(String methodName) {
        for (MethodDescriptor descriptor : methods) {
            if (descriptor.getName().equals(methodName)) {
                return true;
            }
        }
        return false;
    }
}
