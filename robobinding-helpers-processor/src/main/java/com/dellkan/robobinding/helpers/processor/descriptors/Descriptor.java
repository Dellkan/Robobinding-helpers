package com.dellkan.robobinding.helpers.processor.descriptors;

import com.dellkan.robobinding.helpers.processor.Util;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.tools.Diagnostic;

public abstract class Descriptor {
    ModelDescriptor modelDescriptor;
    Element field;

    public Descriptor(ModelDescriptor modelDescriptor, Element field) {
        this.modelDescriptor = modelDescriptor;
        this.field = field;

        if (this.field.getModifiers().contains(Modifier.PRIVATE)) {
            modelDescriptor.getMessager().printMessage(
                    Diagnostic.Kind.ERROR,
                    String.format(
                            "%s.%s can't be set to private. The %s$$Helper model won't be able to access it!",
                            modelDescriptor.getModel().getSimpleName(),
                            field.getSimpleName(),
                            modelDescriptor.getModel().getSimpleName()
                    ),
                    field
            );
        }
    }

    public boolean methodExists(String methodName) {
        for (MethodDescriptor descriptor : modelDescriptor.getMethods()) {
            if (descriptor.getName().equals(methodName)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getMethods() {
        List<String> methods = new ArrayList<>();
        for (MethodDescriptor descriptor : modelDescriptor.getMethods()) {
            methods.add(descriptor.getName());
        }
        return methods;
    }

    public Element getField() {
        return this.field;
    }

    private String classPrefix = "";
    public String getClassPrefix() {
        return this.classPrefix;
    }

    public void setClassPrefix(String classPrefix) {
        this.classPrefix = classPrefix;
    }

    private List<String> prefixes = new ArrayList<>();
    public String getPrefix() {
        String joined_prefix = "";
        for (String prefix : prefixes) {
            joined_prefix += prefix;
        }
        return joined_prefix;
    }

    public List<String> getPrefixes() {
        return this.prefixes;
    }

    public void setPrefixes(List<String> prefixes) {
        this.prefixes.addAll(prefixes);
    }

    public void addPrefix(String prefix) {
        if (prefix.length() > 0) {
            prefix = prefix.substring(0, 1).toUpperCase() + prefix.substring(1);
        }
        this.prefixes.add(0, prefix);
    }

    public String getName() {
        String prefix = getPrefix();

        String name = getField().getSimpleName().toString();

        name = name.substring(0, 1).toUpperCase() + name.substring(1);

        return prefix + name;
    }

    public String getFieldName() {
        return getField().getSimpleName().toString();
    }

    public String getAccessor() {
        if (getField().getModifiers().contains(Modifier.STATIC)) {
            return String.format("%s.%s", Util.typeToString(getField().getEnclosingElement().asType()), getField().getSimpleName().toString());
        }
        return "this.data." + getClassPrefix() + getField().getSimpleName().toString();
    }

    public boolean isMethod() {
        return field.getKind().equals(ElementKind.METHOD);
    }

    public String getType() {
        return Util.typeToString(getField().asType());
    }
}
