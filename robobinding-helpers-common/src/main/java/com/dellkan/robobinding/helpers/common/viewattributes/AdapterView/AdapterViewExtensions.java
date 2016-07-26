package com.dellkan.robobinding.helpers.common.viewattributes.AdapterView;

import android.widget.AdapterView;

import org.robobinding.annotation.ViewBinding;
import org.robobinding.customviewbinding.CustomViewBinding;
import org.robobinding.viewbinding.BindingAttributeMappings;

@ViewBinding(simpleOneWayProperties = {})
public class AdapterViewExtensions extends CustomViewBinding<AdapterView> {
	@Override
	public void mapBindingAttributes(BindingAttributeMappings<AdapterView> mappings) {
		super.mapBindingAttributes(mappings);

		mappings.mapGroupedAttribute(AdapterViewGroupedAttributes.class, "emptyView");
	}
}