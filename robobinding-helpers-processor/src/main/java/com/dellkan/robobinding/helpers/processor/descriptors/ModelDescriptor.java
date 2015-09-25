package com.dellkan.robobinding.helpers.processor.descriptors;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;

public class ModelDescriptor {
    private Element model = null;
    private List<MethodDescriptor> methods = new ArrayList<>();
    private List<GetSetDescriptor> accessors = new ArrayList<>();
    private List<ValidateDescriptor> validators = new ArrayList<>();
    private List<ListItemsDescriptor> listItems = new ArrayList<>();
    private List<AddToDataDescriptor> dataItems = new ArrayList<>();
    private List<IncludeModelDescriptor> includeItems = new ArrayList<>();
    private boolean writtenToFile = false;
    private Messager messager;

    public ModelDescriptor(Element model, Messager messager) {
        this.setModel(model);
        this.setMessager(messager);
    }

    public boolean methodExists(String methodName) {
        for (MethodDescriptor descriptor : getMethods()) {
            if (descriptor.getName().equals(methodName)) {
                return true;
            }
        }
        return false;
    }


    public Element getModel() {
        return model;
    }

    public void setModel(Element model) {
        this.model = model;
    }

    public List<MethodDescriptor> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodDescriptor> methods) {
        this.methods = methods;
    }

    public List<GetSetDescriptor> getAccessors() {
        return accessors;
    }

    public void setAccessors(List<GetSetDescriptor> accessors) {
        this.accessors = accessors;
    }

    public List<ValidateDescriptor> getValidators() {
        return validators;
    }

    public void setValidators(List<ValidateDescriptor> validators) {
        this.validators = validators;
    }

    public List<ListItemsDescriptor> getListItems() {
        return listItems;
    }

    public void setListItems(List<ListItemsDescriptor> listItems) {
        this.listItems = listItems;
    }

    public List<AddToDataDescriptor> getDataItems() {
        return dataItems;
    }

    public void setDataItems(List<AddToDataDescriptor> dataItems) {
        this.dataItems = dataItems;
    }

    public List<IncludeModelDescriptor> getIncludeItems() {
        return includeItems;
    }

    public void setIncludeItems(List<IncludeModelDescriptor> includeItems) {
        this.includeItems = includeItems;
    }

    public boolean isWrittenToFile() {
        return writtenToFile;
    }

    public void setWrittenToFile(boolean writtenToFile) {
        this.writtenToFile = writtenToFile;
    }

    public Messager getMessager() {
        return messager;
    }

    public void setMessager(Messager messager) {
        this.messager = messager;
    }
}
