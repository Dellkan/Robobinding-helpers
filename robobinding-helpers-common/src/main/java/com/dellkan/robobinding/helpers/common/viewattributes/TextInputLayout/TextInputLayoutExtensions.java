package com.dellkan.robobinding.helpers.common.viewattributes.TextInputLayout;

import android.support.design.widget.TextInputLayout;

import org.robobinding.annotation.ViewBinding;
import org.robobinding.customviewbinding.CustomViewBinding;
import org.robobinding.viewbinding.BindingAttributeMappings;

@ViewBinding(simpleOneWayProperties = {"errorEnabled"})
public class TextInputLayoutExtensions extends CustomViewBinding<TextInputLayout> {
    @Override
    public void mapBindingAttributes(BindingAttributeMappings<TextInputLayout> mappings) {
        super.mapBindingAttributes(mappings);
        mappings.mapOneWayMultiTypeProperty(TextInputLayoutErrorAttribute.class, "error");
    }
}
