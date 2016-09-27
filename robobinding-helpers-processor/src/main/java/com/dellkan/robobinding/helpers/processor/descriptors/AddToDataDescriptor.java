package com.dellkan.robobinding.helpers.processor.descriptors;

import com.dellkan.robobinding.helpers.model.ListContainer;
import com.dellkan.robobinding.helpers.modelgen.AddToData;

import java.util.Arrays;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

public class AddToDataDescriptor extends Descriptor {
    private AddToData annotation;
    private boolean isList;

    public AddToDataDescriptor(ModelDescriptor modelDescriptor, Element field, AddToData annotation) {
        super(modelDescriptor, field);
        this.annotation = annotation;

        ProcessingEnvironment processingEnv = modelDescriptor.getProcessingEnvironment();
        Types typeUtils = processingEnv.getTypeUtils();

        TypeMirror childType = null;
        if (field.getKind().equals(ElementKind.METHOD)) {
            childType = ((ExecutableElement) field).getReturnType();
        } else if (field.getKind().equals(ElementKind.FIELD)) {
            childType = field.asType();
        }

        if (childType != null) {
            this.isList = typeUtils.isAssignable(
                    childType,
                    typeUtils.getDeclaredType(
                            processingEnv.getElementUtils().getTypeElement(ListContainer.class.getCanonicalName()),
                            typeUtils.getWildcardType(null, null)
                    )
            );
        } else {
            modelDescriptor.getMessager().printMessage(Diagnostic.Kind.ERROR, "Couldn't process AddToData for child. Unable to determine (return)type", field);
        }
    }

    public String[] getPath() {
        return annotation.path().split("\\.");
    }

    public String getDataName() {
        if (annotation.alternativeName().isEmpty()) {
            String name = getField().getSimpleName().toString();
            if (isMethod()) {
                if (name.startsWith("get") && name.length() > 3) {
                    name = name.substring(3, 4).toLowerCase() + name.substring(4);
                }
            }
            return name;
        }
        return annotation.alternativeName();
    }

    public boolean isListContainer() {
        return this.isList;
    }

    public String getListAccessor() {
        return "this.data." + getClassPrefix() + getField().getSimpleName().toString();
    }

    public String getDataAccessor() {
        if (!annotation.alternativeDataFormatter().isEmpty()) {
            return "this.data." + getClassPrefix() + annotation.alternativeDataFormatter() + "()";
        }
        if (isListContainer()) {
            return "getListContainerData" + getName().substring(0, 1).toUpperCase() + getName().substring(1) + "()";
        }
        return isMethod() ? getAccessor() + "()" : getAccessor();
    }

    public boolean isConditional() {
        return !annotation.onlyIf().isEmpty();
    }

    public String getConditionalMethod() {
        String prefix = getPrefix();
        if (prefix.length() > 0) {
            return prefix.toLowerCase() + annotation.onlyIf().substring(0, 1).toUpperCase() + annotation.onlyIf().substring(1);
        }
        return annotation.onlyIf();
    }

    public AddToData getAnnotation() {
        return annotation;
    }

    public boolean inGroup(String group) {
        return Arrays.asList(annotation.group()).contains(group);
    }
}
