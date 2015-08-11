package com.dellkan.robobinding.helpers.processor;

import com.dellkan.robobinding.helpers.modelgen.AddToData;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

public class AddDataDescriptor extends Descriptor {
    List<MethodDescriptor> methods;
    List<GetSetDescriptor> accessors;
    private Element element;
    private AddToData annotation;

    public AddDataDescriptor(List<MethodDescriptor> methods, List<GetSetDescriptor> accessors, Element element, AddToData annotation) {
        super(methods);
        this.methods = methods;
        this.accessors = accessors;
        this.element = element;
        this.annotation = annotation;
    }

    public String getName() {
        return element.getSimpleName().toString();
    }

    public String getDataName() {
        if (annotation.alternativeName().isEmpty()) {
            return getName();
        }
        return annotation.alternativeName();
    }

    public String getDataAccessor() {
        if (element.getKind().equals(ElementKind.METHOD)) {
            return getName();
        } else if (element.getKind().equals(ElementKind.FIELD)) {
            if (!annotation.alternativeDataFormatter().isEmpty() && methodExists(annotation.alternativeDataFormatter())) {
                return "this.data." + annotation.alternativeDataFormatter();
            }
            for (MethodDescriptor method : methods) {
                if (method.getName().equalsIgnoreCase("get" + getName())) {
                    return method.getName();
                }
            }
            for (GetSetDescriptor accessor : accessors) {
                if (accessor.isGetter() && accessor.getName().equals(getName())) {
                    return "get" + getName().substring(0, 1).toUpperCase() + getName().substring(1);
                }
            }
        }
        return "";
    }
}
