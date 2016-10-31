package ${packageName};

import org.json.JSONObject;
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
import java.util.UUID;

@PresentationModel
public class ${className}$$ItemHelper implements IHasPresentationModel, ItemPresentationModel<${qualifiedClassName}> {
    private ${qualifiedClassName} data;
    private final PresentationModelChangeSupport changeHandler;
    public ${className}$$ItemHelper() {
        this.changeHandler = new PresentationModelChangeSupport(this);
    }

    @Override
    public void updateData(${qualifiedClassName} data, ItemContext itemContext) {
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

    // List items
    <#list listItems as item>
        <#include "/ListItems.ftl">
    </#list>

    // Utilities
    public void refresh() {
        this.changeHandler.refreshPresentationModel();
    }

    public void refresh(String... fields) {
        for (String field : fields) {
            this.changeHandler.firePropertyChange(field);
        }
    }

    // Data
    <#include "/Data.ftl">
}