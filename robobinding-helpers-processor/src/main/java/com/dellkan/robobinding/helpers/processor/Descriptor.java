package com.dellkan.robobinding.helpers.processor;

import java.util.ArrayList;
import java.util.List;

public class Descriptor {
    ModelDescriptor modelDescriptor;

    public Descriptor(ModelDescriptor modelDescriptor) {
        this.modelDescriptor = modelDescriptor;
    }

    public boolean methodExists(String methodName) {
        for (MethodDescriptor descriptor : modelDescriptor.methods) {
            if (descriptor.getName().equals(methodName)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getMethods() {
        List<String> methods = new ArrayList<>();
        for (MethodDescriptor descriptor : modelDescriptor.methods) {
            methods.add(descriptor.getName());
        }
        return methods;
    }
}
