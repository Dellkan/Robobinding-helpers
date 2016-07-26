package com.dellkan.robobinding.helpers.common.viewattributes.SwipeRefreshLayout;

import android.support.v4.widget.SwipeRefreshLayout;

import org.robobinding.annotation.ViewBinding;
import org.robobinding.customviewbinding.CustomViewBinding;
import org.robobinding.viewbinding.BindingAttributeMappings;

@ViewBinding(simpleOneWayProperties = {})
public class SwipeRefreshLayoutExtensions extends CustomViewBinding<SwipeRefreshLayout> {
    @Override
    public void mapBindingAttributes(BindingAttributeMappings<SwipeRefreshLayout> mappings) {
        super.mapBindingAttributes(mappings);
        mappings.mapEvent(SwipeRefreshLayoutOnRefreshAttribute.class, "refresh");
        mappings.mapOneWayProperty(SwipeRefreshLayoutRefreshAttribute.class, "refreshing");
    }
}
