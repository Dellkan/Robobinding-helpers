package com.dellkan.robobinding.helpers.processor;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;

public class MethodDescriptor {
    private ExecutableElement method;

    public MethodDescriptor(ExecutableElement method) {
        this.method = method;
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
}
