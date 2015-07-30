package com.dellkan.robobinding.helpers.processor;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeKind;

public class GetSetDescriptor {
    private boolean isSetter;
    private Element element;
    private String[] dependsOn;

    public GetSetDescriptor(boolean isSetter, Element element, String[] dependsOn) {
        this.isSetter = isSetter;
        this.element = element;
        this.dependsOn = dependsOn;
    }

    public boolean isSetter() {
        return this.isSetter;
    }

    public String getType() {
        return Util.typeToString(element.asType());
    }

    public String getName() {
        return element.getSimpleName().toString();
    }

    public String[] getDependsOn() {
        return dependsOn != null ? dependsOn : new String[]{};
    }

    public boolean isBoolean() {
        return element.asType().getKind().equals(TypeKind.BOOLEAN);
    }

    public boolean isNumeric() {
        return
                element.asType().getKind().equals(TypeKind.INT) ||
                element.asType().getKind().equals(TypeKind.FLOAT) ||
                element.asType().getKind().equals(TypeKind.DOUBLE) ||
                element.asType().getKind().equals(TypeKind.LONG);
    }
}
