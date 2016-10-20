package com.dellkan.robobinding.helpers.processor.descriptors;

import com.dellkan.robobinding.helpers.processor.Util;

import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;

public class ListItemsDescriptor extends Descriptor {
    public ListItemsDescriptor(ModelDescriptor modelDescriptor, Element element) {
        super(modelDescriptor, element);
    }

    public String getType() {
        return Util.typeToString(((DeclaredType) getField().asType()).getTypeArguments().get(0));
    }

    public String getSimpleType() {
        return Util.rawTypeToString(((DeclaredType) getField().asType()).getTypeArguments().get(0), '$');
    }
}
