package com.dellkan.robobinding.helpers;

import com.dellkan.robobinding.helpers.model.IHasPresentationModel;
import com.dellkan.robobinding.helpers.model.PresentationModelWrapper;
import com.dellkan.robobinding.helpers.model.IGlobalPresentationModelLookup;

import java.util.HashMap;
import java.util.Map;

public final class GlobalPresentationModelLookup implements IGlobalPresentationModelLookup {
    public GlobalPresentationModelLookup() {}

    private static Map<Class<? extends IHasPresentationModel>, Class<? extends IHasPresentationModel>> lookups = new HashMap<>();
    private static void init() {
        if (lookups.isEmpty()) {<#list models as element, model><#if model.presentationModel>
            lookups.put(${model.type}.class, ${model.type}$$Helper.class);</#if></#list>
        }
    }

    public Class<? extends IHasPresentationModel> lookup(Class<? extends IHasPresentationModel> dataModelClass) {
        init();
        return lookups.get(dataModelClass);
    }

    public IHasPresentationModel getPresentationModel(PresentationModelWrapper dataModel) {
        Class<? extends IHasPresentationModel> cls = dataModel.getClass();
        <#list models as element, model><#if model.presentationModel>if (cls.equals(${model.type}.class)) {
            return new ${model.type}$$Helper(dataModel);
        }<#sep> else </#sep></#if></#list>
        return null;
    }
}