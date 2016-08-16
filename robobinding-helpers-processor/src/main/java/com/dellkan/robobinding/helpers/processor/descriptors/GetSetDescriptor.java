package com.dellkan.robobinding.helpers.processor.descriptors;

import com.dellkan.robobinding.helpers.modelgen.Stringify;

import javax.lang.model.element.Element;

/**
 * Used by freemarker, during compile-time annotation processing.
 * Describes attributes marked with {@link com.dellkan.robobinding.helpers.modelgen.Get Get},
 * {@link com.dellkan.robobinding.helpers.modelgen.Set Set} and
 * {@link com.dellkan.robobinding.helpers.modelgen.GetSet GetSet}
 */
public class GetSetDescriptor extends Descriptor {
    private boolean isGetter;
    private boolean isSetter;
    private String[] dependsOn;
    private boolean isTwoState;

    public GetSetDescriptor(ModelDescriptor model, Element field, boolean isGetter, boolean isSetter, boolean isTwoState) {
        super(model, field);
        this.isGetter = isGetter;
        this.isSetter = isSetter;
        this.isTwoState = isTwoState;
    }

    public boolean isGetter() {
        return this.isGetter;
    }

    public boolean isSetter() {
        return this.isSetter;
    }

    public boolean isBoolean() {
        return getType().equals("java.lang.Boolean");
    }

    public boolean isNumeric() {
        switch (getType()) {
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
        return getType().equals("java.lang.String");
    }

    public boolean isTwoState() {
        return this.isTwoState;
    }

    public boolean getForceString() {
        return getField().getAnnotation(Stringify.class) != null && (isString() || isNumeric());
    }
}
