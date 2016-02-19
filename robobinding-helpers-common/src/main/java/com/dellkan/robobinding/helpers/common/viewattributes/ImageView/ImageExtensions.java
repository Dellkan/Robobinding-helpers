package com.dellkan.robobinding.helpers.common.viewattributes.ImageView;

import android.widget.ImageView;

import org.robobinding.annotation.ViewBinding;
import org.robobinding.customviewbinding.CustomViewBinding;
import org.robobinding.viewbinding.BindingAttributeMappings;

@ViewBinding(simpleOneWayProperties = {})
public class ImageExtensions extends CustomViewBinding<ImageView> {
    @Override
    public void mapBindingAttributes(BindingAttributeMappings<ImageView> mappings) {
        super.mapBindingAttributes(mappings);

        mappings.mapGroupedAttribute(ImageAttributes.class, "src", "fit");
    }
}
