package com.dellkan.robobinding.helpers.processor;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;

public class ListItemsDescriptor extends Descriptor {
    private Element element;

    public ListItemsDescriptor(List<MethodDescriptor> methods, Element element) {
        super(methods);
        this.element = element;
    }

    public String getType() {
        return Util.typeToString(((DeclaredType) element.asType()).getTypeArguments().get(0));
    }

    public String getName() {
        return element.getSimpleName().toString();
    }
}
