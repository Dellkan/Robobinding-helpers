package ${packageName};

import org.robobinding.annotation.DependsOnStateOf;
import org.robobinding.itempresentationmodel.ItemContext;
import org.robobinding.itempresentationmodel.ItemPresentationModel;
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
public class ${className}$$ItemHelper implements IHasPresentationModel, ItemPresentationModel<${className}> {
    private ${className} data;
    private final PresentationModelChangeSupport changeHandler;
    public ${className}$$ItemHelper() {
        this.changeHandler = new PresentationModelChangeSupport(this);
    }

    @Override
    public void updateData(${className} data, ItemContext itemContext) {
        this.data = data;
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