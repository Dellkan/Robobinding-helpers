package ${packageName};

import org.json.JSONObject;
import org.robobinding.annotation.DependsOnStateOf;
import org.robobinding.annotation.ItemPresentationModel;
import org.robobinding.annotation.PresentationModel;
import org.robobinding.presentationmodel.HasPresentationModelChangeSupport;
import org.robobinding.presentationmodel.PresentationModelChangeSupport;
import org.robobinding.widget.adapterview.ItemClickEvent;

import com.dellkan.robobinding.helpers.model.IHasPresentationModel;
import com.dellkan.robobinding.helpers.model.PresentationModelWrapper;
import com.dellkan.robobinding.helpers.validation.ValidationProcessor;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@PresentationModel
public class ${className}$$Helper implements HasPresentationModelChangeSupport, IHasPresentationModel {
    private ${className} data;
    private final PresentationModelChangeSupport changeHandler;
    public ${className}$$Helper(${className} data) {
        this.data = data;
        this.changeHandler = new PresentationModelChangeSupport(this);
        <#list descriptor.includeItems as includeModel>
            ${includeModel.accessor}.attachModel(this.data, "${includeModel.prefix}");
        </#list>
    }

    public ${className}$$Helper(PresentationModelWrapper data) {
        this.data = (${className})data;
        this.changeHandler = new PresentationModelChangeSupport(this);
        <#list descriptor.includeItems as includeModel>
            ${includeModel.accessor}.attachModel(this.data, "${includeModel.prefix}");
        </#list>
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

    // Data
    <#include "/Data.ftl">

    // Utilities
    public void refresh() {
        this.changeHandler.refreshPresentationModel();
    }

    public void refresh(String... fields) {
        for (String field : fields) {
            this.changeHandler.firePropertyChange(field);
        }
    }

    public static Class<? extends IHasPresentationModel> getModelClass() {
        return ${className}.class;
    }
}