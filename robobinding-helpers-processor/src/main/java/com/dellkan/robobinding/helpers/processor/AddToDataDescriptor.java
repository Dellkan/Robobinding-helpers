package com.dellkan.robobinding.helpers.processor;

import com.dellkan.robobinding.helpers.modelgen.AddToData;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

public class AddToDataDescriptor extends Descriptor {
    private Element element;
    private AddToData annotation;
    private boolean isList;
    private String prefix = "";

    public AddToDataDescriptor(ModelDescriptor modelDescriptor, Element element, AddToData annotation, boolean isList) {
        super(modelDescriptor);
        this.element = element;
        this.annotation = annotation;
        this.isList = isList;
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

    public boolean isListContainer() {
        return this.isList;
    }

    public String getDataAccessor() {
        if (element.getKind().equals(ElementKind.METHOD)) {
            return getName();
        } else if (element.getKind().equals(ElementKind.FIELD)) {
            if (!annotation.alternativeDataFormatter().isEmpty() && methodExists(annotation.alternativeDataFormatter())) {
                return "this.data." + prefix + annotation.alternativeDataFormatter();
            }
            if (isListContainer()) {
                return "getListContainerData" + getName().substring(0, 1).toUpperCase() + getName().substring(1);
            }
            for (MethodDescriptor method : modelDescriptor.methods) {
                if (method.getName().equalsIgnoreCase("get" + getName())) {
                    return method.getName();
                }
            }
            for (GetSetDescriptor accessor : modelDescriptor.accessors) {
                if (accessor.isGetter() && accessor.getName().equals(getName())) {
                    if (accessor.isTwoState() || accessor.isBoolean()) {
                        return "is" + getName().substring(0, 1).toUpperCase() + getName().substring(1);
                    }
                    return "get" + getName().substring(0, 1).toUpperCase() + getName().substring(1);
                }
            }
        }
        return "";
    }

    public boolean isConditional() {
        return !annotation.onlyIf().isEmpty();
    }

    public String getConditionalMethod() {
        return annotation.onlyIf();
    }

    public Element getElement() {
        return element;
    }

    public AddToData getAnnotation() {
        return annotation;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
