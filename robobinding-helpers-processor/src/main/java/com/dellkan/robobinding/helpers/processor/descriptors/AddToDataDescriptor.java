package com.dellkan.robobinding.helpers.processor.descriptors;

import com.dellkan.robobinding.helpers.modelgen.AddToData;

import java.util.Arrays;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.tools.Diagnostic;

public class AddToDataDescriptor extends Descriptor {
    private AddToData annotation;
    private boolean isList;

    public AddToDataDescriptor(ModelDescriptor modelDescriptor, Element field, AddToData annotation, boolean isList) {
        super(modelDescriptor, field);
        this.annotation = annotation;
        this.isList = isList;
    }

    public String[] getPath() {
        return annotation.path().split("\\.");
    }

    public String getDataName() {
        if (annotation.alternativeName().isEmpty()) {
            return getField().getSimpleName().toString();
        }
        return annotation.alternativeName();
    }

    public boolean isListContainer() {
        return this.isList;
    }

    public String getListAccessor() {
        return "this.data." + getClassPrefix() + getField().getSimpleName().toString();
    }

    public String getDataAccessor() throws Exception {
        if (getField().getKind().equals(ElementKind.METHOD)) {
            String methodPrefix = "";
            String fieldName = getField().getSimpleName().toString();
            if (fieldName.startsWith("get")) {
                methodPrefix = "get";
                fieldName = fieldName.substring(3);
            } else if (fieldName.startsWith("is")) {
                methodPrefix = "is";
                fieldName = fieldName.substring(2);
            } else if (fieldName.startsWith("set")) {
                methodPrefix = "set";
                fieldName = fieldName.substring(3);
            }
            String name = methodPrefix + getPrefix() + fieldName;
            return name.substring(0, 1).toLowerCase() + name.substring(1) + "()";
        } else if (getField().getKind().equals(ElementKind.FIELD)) {
            if (!annotation.alternativeDataFormatter().isEmpty()) {
                return "this.data." + getClassPrefix() + annotation.alternativeDataFormatter() + "()";
            }
            if (isListContainer()) {
                return "getListContainerData" + getName().substring(0, 1).toUpperCase() + getName().substring(1) + "()";
            }
            return getAccessor();
        }
        this.modelDescriptor.getMessager().printMessage(Diagnostic.Kind.WARNING, "[AddToData Accessor failed!] Looking for " + getName() + " (prefix: " + getPrefix() + "). This is " + this.modelDescriptor.getModel().getSimpleName().toString());
        return "";
    }

    public boolean isConditional() {
        return !annotation.onlyIf().isEmpty();
    }

    public String getConditionalMethod() {
        return annotation.onlyIf();
    }

    public AddToData getAnnotation() {
        return annotation;
    }

    public boolean inGroup(String group) {
        return Arrays.asList(annotation.group()).contains(group);
    }
}
