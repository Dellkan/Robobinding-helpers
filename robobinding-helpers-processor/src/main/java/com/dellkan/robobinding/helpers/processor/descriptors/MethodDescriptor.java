package com.dellkan.robobinding.helpers.processor.descriptors;

import com.dellkan.robobinding.helpers.modelgen.DependsOnStateOf;
import com.dellkan.robobinding.helpers.processor.Util;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.tools.Diagnostic;

/**
 * Used by freemarker, during compile-time annotation processing.
 * Describes methods found on classes marked with {@link com.dellkan.robobinding.helpers.modelgen.PresentationModel PresentationModel}.
 */
public class MethodDescriptor extends Descriptor {
    private DependsOnStateOf dependsOn;

    public MethodDescriptor(ModelDescriptor modelDescriptor, ExecutableElement method, DependsOnStateOf dependsOn) {
        super(modelDescriptor, method);
        this.dependsOn = dependsOn;
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

    @Override
    public String getAccessor() {
        if (getField().getModifiers().contains(Modifier.STATIC)) {
            return String.format("%s.%s", Util.typeToString(getMethod().getEnclosingElement().asType()), getField().getSimpleName().toString());
        }
        return super.getAccessor();
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
        List<String> dependencies = new ArrayList<>();
        String prefix = getPrefix();

        if (dependsOn != null) {
            for (String dependency : dependsOn.value()) {
                if (prefix.length() > 1) {
                    dependencies.add(prefix.substring(0, 1).toLowerCase() + prefix.substring(1) + dependency.substring(0, 1).toUpperCase() + dependency.substring(1));
                } else {
                    dependencies.add(dependency);
                }
            }
        }

        String[] processedDependencies = new String[dependencies.size()];
        dependencies.toArray(processedDependencies);
        return processedDependencies;
    }

    public DependsOnStateOf getDependsOnAnnotation() {
        return dependsOn;
    }
}
