package ${package};

import org.robobinding.annotation.DependsOnStateOf;
import org.robobinding.annotation.PresentationModel;
import org.robobinding.presentationmodel.HasPresentationModelChangeSupport;
import org.robobinding.presentationmodel.PresentationModelChangeSupport;

import com.dellkan.robobinding.helpers.model.IHasPresentationModel;
import com.dellkan.robobinding.helpers.validation.ValidationProcessor;

import java.lang.annotation.Annotation;

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

    <#list accessors as item>
        <#include "/Accessor.ftl">
    </#list>

    // Existing methods
    <#list methods as item>
        <#include "/Method.ftl">
    </#list>

    // Validation
    <#include "/Validator.ftl">


    // Utilities
    public void refresh() {
        <#list accessors as item>
        this.changeHandler.firePropertyChange("${item.name}");
        </#list>
    }

    public void refresh(String field) {
        this.changeHandler.firePropertyChange(field);
    }
}