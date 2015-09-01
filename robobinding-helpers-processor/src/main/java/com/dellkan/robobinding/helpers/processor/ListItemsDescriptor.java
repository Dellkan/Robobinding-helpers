package com.dellkan.robobinding.helpers.processor;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;

public class ListItemsDescriptor extends Descriptor {
    private Element element;
    private String prefix = "";

    public ListItemsDescriptor(ModelDescriptor modelDescriptor, Element element) {
        super(modelDescriptor);
        this.element = element;
    }

    public String getType() {
        return Util.typeToString(((DeclaredType) element.asType()).getTypeArguments().get(0));
    }

    public String getName() {
        return element.getSimpleName().toString();
    }

    public String getAccessor() {
        return "this.data." + prefix + element.getSimpleName().toString();
    }

    public Element getElement() {
        return this.element;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
