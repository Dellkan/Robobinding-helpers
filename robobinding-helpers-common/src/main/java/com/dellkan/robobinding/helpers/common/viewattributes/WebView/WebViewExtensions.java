package com.dellkan.robobinding.helpers.common.viewattributes.WebView;

import android.webkit.WebView;

import org.robobinding.annotation.ViewBinding;
import org.robobinding.customviewbinding.CustomViewBinding;
import org.robobinding.viewbinding.BindingAttributeMappings;

@ViewBinding(simpleOneWayProperties = {})
public class WebViewExtensions extends CustomViewBinding<WebView> {
    @Override
    public void mapBindingAttributes(BindingAttributeMappings<WebView> mappings) {
        super.mapBindingAttributes(mappings);
        mappings.mapGroupedAttribute(WebViewAttributes.class, "additionalHeaders", "runJavascript", "src", "errorLayout");

        mappings.mapEvent(WebViewClickAttribute.class, "onClick");
        mappings.mapEvent(WebViewInitAttribute.class, "onInit");
    }
}
