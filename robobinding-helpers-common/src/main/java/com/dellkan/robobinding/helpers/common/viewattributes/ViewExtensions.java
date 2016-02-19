package com.dellkan.robobinding.helpers.common.viewattributes;

import android.view.View;

import org.robobinding.annotation.ViewBinding;
import org.robobinding.customviewbinding.CustomViewBinding;
import org.robobinding.viewbinding.BindingAttributeMappings;

@ViewBinding(simpleOneWayProperties = {"enabled"})
public class ViewExtensions extends CustomViewBinding<View> {
    @Override
    public void mapBindingAttributes(BindingAttributeMappings<View> mappings) {
        super.mapBindingAttributes(mappings);
        mappings.mapOneWayMultiTypeProperty(ViewBackgroundAttribute.class, "background");
        mappings.mapOneWayMultiTypeProperty(ViewBackgroundColorAttribute.class, "backgroundColor");
    }
}
