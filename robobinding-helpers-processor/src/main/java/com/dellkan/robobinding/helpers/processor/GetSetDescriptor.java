package com.dellkan.robobinding.helpers.processor;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeKind;

/**
 * Used by freemarker, during compile-time annotation processing.
 * Describes attributes marked with {@link com.dellkan.robobinding.helpers.modelgen.Get Get} and
 * {@link com.dellkan.robobinding.helpers.modelgen.GetSet GetSet}
 */
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
        String type = Util.typeToString(element.asType());
        switch (type) {
            case "java.lang.Integer":
            case "java.lang.Float":
            case "java.lang.Double":
            case "java.lang.Long":
                return true;
            default:
                return false;
        }
    }
}
