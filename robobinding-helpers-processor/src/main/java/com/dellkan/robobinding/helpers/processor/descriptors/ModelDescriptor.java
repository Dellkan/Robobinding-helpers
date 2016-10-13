package com.dellkan.robobinding.helpers.processor.descriptors;

import com.dellkan.robobinding.helpers.modelgen.ItemPresentationModel;
import com.dellkan.robobinding.helpers.modelgen.PresentationModel;
import com.dellkan.robobinding.helpers.processor.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
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
    private ProcessingEnvironment processingEnvironment;

    public ModelDescriptor(Element model, Messager messager, ProcessingEnvironment processingEnvironment) {
        this.setModel(model);
        this.setMessager(messager);
        this.setProcessingEnvironment(processingEnvironment);
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

    public String getType() {
        return Util.typeToString(model.asType());
    }

    public boolean isPresentationModel() {
        return model.getAnnotation(PresentationModel.class) != null;
    }

    public boolean isItemPresentationModel() {
        return model.getAnnotation(ItemPresentationModel.class) != null;
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

    public ProcessingEnvironment getProcessingEnvironment() {
        return processingEnvironment;
    }

    public void setProcessingEnvironment(ProcessingEnvironment processingEnvironment) {
        this.processingEnvironment = processingEnvironment;
    }

    public List<String> getDataGroups() {
        Set<String> groups = new HashSet<>();
        for (AddToDataDescriptor descriptor : getDataItems()) {
            groups.addAll(Arrays.asList(descriptor.getAnnotation().group()));
        }
        return new ArrayList<String>(groups);
    }
}
