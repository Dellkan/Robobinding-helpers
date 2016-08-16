package com.dellkan.robobinding.helpers.processor.descriptors;

import com.dellkan.robobinding.helpers.processor.Util;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;

/**
 * Used by freemarker, during compile-time annotation processing.
 * Describes methods found on classes marked with {@link com.dellkan.robobinding.helpers.modelgen.PresentationModel PresentationModel}.
 */
public class MethodDescriptor extends Descriptor {
    public MethodDescriptor(ModelDescriptor modelDescriptor, ExecutableElement method) {
        super(modelDescriptor, method);
    }

    public List<Param> getParams() {
        List<Param> results = new ArrayList<>();
        for(VariableElement param : getMethod().getParameters()) {
            results.add(new Param(
                    Util.typeToString(param.asType()),
                    param.getSimpleName().toString()
            ));
        }
        return results;
    }

    @Override
    public String getType() {
        if (getMethod().getReturnType().getKind().equals(TypeKind.VOID)) {
            return "void";
        }
        if (getMethod().getReturnType().getKind().isPrimitive()) {
            return getMethod().getReturnType().getKind().name().toLowerCase();
        }
        return Util.typeToString(getMethod().getReturnType());
    }

    @Override
    public String getName() {
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

        if (getPrefix().length() > 0) {
            fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        }

        String name = methodPrefix + getPrefix() + fieldName;
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    public ExecutableElement getMethod() {
        return (ExecutableElement) getField();
    }

    public static class Param {
        private String type;
        private String name;

        public Param(String type, String getName) {
            this.type = type;
            this.name = getName;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }
    }
}
