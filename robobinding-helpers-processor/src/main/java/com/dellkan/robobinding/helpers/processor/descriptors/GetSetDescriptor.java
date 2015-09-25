package com.dellkan.robobinding.helpers.processor.descriptors;

import com.dellkan.robobinding.helpers.modelgen.Stringify;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

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

    public GetSetDescriptor(ModelDescriptor model, Element field, boolean isGetter, boolean isSetter, boolean isTwoState, String[] dependsOn) {
        super(model, field);
        this.isGetter = isGetter;
        this.isSetter = isSetter;
        this.dependsOn = dependsOn;
        this.isTwoState = isTwoState;
    }

    public boolean isGetter() {
        return this.isGetter;
    }

    public boolean isSetter() {
        return this.isSetter;
    }

    public String[] getDependsOn() {
        List<String> dependencies = new ArrayList<>();
        String prefix = getPrefix();

        if (dependsOn != null) {
            for (String dependency : dependsOn) {
                if (prefix.length() > 1) {
                    if (dependency.toLowerCase().contains("specialists")) {
                        modelDescriptor.getMessager().printMessage(Diagnostic.Kind.WARNING,
                                "Prefix: " + prefix + "\n" +
                                        "Prefix processed: " + prefix.substring(0, 1).toLowerCase() + prefix.substring(1) + "\n" +
                                        "Dependency: " + dependency + "\n" +
                                        "Dependency processed: " + dependency.substring(0, 1).toUpperCase() + dependency.substring(1) + "\n" +
                                        "complete: " + prefix.substring(0, 1).toLowerCase() + prefix.substring(1) + dependency.substring(0, 1).toUpperCase() + dependency.substring(1)
                        );
                    }
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

    public String[] getDependsOnRaw() {
        return dependsOn;
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
