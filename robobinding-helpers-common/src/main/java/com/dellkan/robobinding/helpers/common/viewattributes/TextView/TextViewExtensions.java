package com.dellkan.robobinding.helpers.common.viewattributes.TextView;

import android.widget.TextView;

import org.robobinding.annotation.ViewBinding;
import org.robobinding.customviewbinding.CustomViewBinding;
import org.robobinding.viewbinding.BindingAttributeMappings;
import org.robobinding.widget.textview.TextColorAttribute;

@ViewBinding(simpleOneWayProperties = {})
public class TextViewExtensions extends CustomViewBinding<TextView> {
    @Override
    public void mapBindingAttributes(BindingAttributeMappings<TextView> mappings) {
        super.mapBindingAttributes(mappings);
        mappings.mapOneWayMultiTypeProperty(TextViewTextAttribute.class, "text");
        mappings.mapOneWayProperty(TextColorAttribute.class, "textColor");
    }
}
