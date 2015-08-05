package com.dellkan.robobinding.helpers.processor;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;

/**
 * Used by freemarker, during compile-time annotation processing.
 * Describes methods found on classes marked with {@link com.dellkan.robobinding.helpers.modelgen.PresentationModel PresentationModel}.
 */
public class MethodDescriptor {
    private ExecutableElement method;
    private String[] dependsOn;

    public MethodDescriptor(ExecutableElement method, String[] dependsOn) {
        this.method = method;
        this.dependsOn = dependsOn;
    }

    public String getName() {
        return method.getSimpleName().toString();
    }

    public List<Param> getParams() {
        List<Param> results = new ArrayList<>();
        for(VariableElement param : method.getParameters()) {
            results.add(new Param(
                    Util.typeToString(param.asType()),
                    param.getSimpleName().toString()
            ));
        }
        return results;
    }

    public String getType() {
        if (this.method.getReturnType().getKind().equals(TypeKind.VOID)) {
            return "void";
        }
        if (this.method.getReturnType().getKind().isPrimitive()) {
            return this.method.getReturnType().getKind().name().toLowerCase();
        }
        return Util.typeToString(this.method.getReturnType());
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

    public String[] getDependsOn() {
        return dependsOn != null ? dependsOn : new String[]{};
    }
}
