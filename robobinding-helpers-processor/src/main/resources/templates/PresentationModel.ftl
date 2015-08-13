package ${packageName};

import org.robobinding.annotation.DependsOnStateOf;
import org.robobinding.annotation.ItemPresentationModel;
import org.robobinding.annotation.PresentationModel;
import org.robobinding.presentationmodel.HasPresentationModelChangeSupport;
import org.robobinding.presentationmodel.PresentationModelChangeSupport;
import org.robobinding.widget.adapterview.ItemClickEvent;

import com.dellkan.robobinding.helpers.model.IHasPresentationModel;
import com.dellkan.robobinding.helpers.validation.ValidationProcessor;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PresentationModel
public class ${className}$$Helper implements HasPresentationModelChangeSupport, IHasPresentationModel {
    private ${className} data;
    private final PresentationModelChangeSupport changeHandler;
    public ${className}$$Helper(${className} data) {
        this.data = data;
        this.changeHandler = new PresentationModelChangeSupport(this);
    }

    @Override
    public PresentationModelChangeSupport getPresentationModelChangeSupport() {
        return this.changeHandler;
    }

    // Accessors
    <#list accessors as item>
        <#include "/Accessor.ftl">
    </#list>

    // Existing methods
    <#list methods as item>
        <#include "/Method.ftl">
    </#list>

    // Validation
    <#include "/Validator.ftl">

    // List items
    <#list listItems as item>
        <#include "/ListItems.ftl">
    </#list>


    // Utilities
    public void refresh() {
        <#list accessors as item>
        this.changeHandler.firePropertyChange("${item.name}");
        </#list>
    }

    public void refresh(String field) {
        this.changeHandler.firePropertyChange(field);
    }

    // Data
    <#include "/Data.ftl">
}