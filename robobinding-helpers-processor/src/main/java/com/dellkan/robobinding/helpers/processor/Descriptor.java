package com.dellkan.robobinding.helpers.processor;

import java.util.ArrayList;
import java.util.List;

public class Descriptor {
    List<MethodDescriptor> list;

    public Descriptor(List<MethodDescriptor> list) {
        this.list = list;
    }

    public boolean methodExists(String methodName) {
        for (MethodDescriptor descriptor : list) {
            if (descriptor.getName().equals(methodName)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getMethods() {
        List<String> methods = new ArrayList<>();
        for (MethodDescriptor descriptor : list) {
            methods.add(descriptor.getName());
        }
        return methods;
    }
}
