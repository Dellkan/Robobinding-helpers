package com.dellkan.robobinding.helpers.processor;

import com.dellkan.robobinding.helpers.modelgen.Stringify;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeKind;

/**
 * Used by freemarker, during compile-time annotation processing.
 * Describes attributes marked with {@link com.dellkan.robobinding.helpers.modelgen.Get Get},
 * {@link com.dellkan.robobinding.helpers.modelgen.Set Set} and
 * {@link com.dellkan.robobinding.helpers.modelgen.GetSet GetSet}
 */
public class GetSetDescriptor extends Descriptor {
    private boolean isGetter;
    private boolean isSetter;
    private Element element;
    private String[] dependsOn;
    private boolean isTwoState;
    private String prefix = "";

    public GetSetDescriptor(ModelDescriptor model, boolean isGetter, boolean isSetter, boolean isTwoState, Element element, String[] dependsOn) {
        super(model);
        this.isGetter = isGetter;
        this.isSetter = isSetter;
        this.element = element;
        this.dependsOn = dependsOn;
        this.isTwoState = isTwoState;
    }

    public boolean isGetter() {
        return this.isGetter;
    }

    public boolean isSetter() {
        return this.isSetter;
    }

    public String getType() {
        return Util.typeToString(element.asType());
    }

    public String getAccessor() {
        return "this.data." + prefix + element.getSimpleName().toString();
    }

    public Element getElement() {
        return this.element;
    }

    public String getName() {
        return element.getSimpleName().toString();
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String[] getDependsOn() {
        return dependsOn != null ? dependsOn : new String[]{};
    }

    public boolean isBoolean() {
        return Util.typeToString(element.asType()).equals("java.lang.Boolean");
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

    public boolean isString() {
        return Util.typeToString(element.asType()).equals("java.lang.String");
    }

    public boolean isTwoState() {
        return this.isTwoState;
    }

    public boolean getForceString() {
        return element.getAnnotation(Stringify.class) != null && (isString() || isNumeric());
    }
}
