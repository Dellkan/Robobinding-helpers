package com.dellkan.robobinding.helpers.common.viewattributes.MapView;

import com.google.android.gms.maps.MapView;

import org.robobinding.annotation.ViewBinding;
import org.robobinding.customviewbinding.CustomViewBinding;
import org.robobinding.viewbinding.BindingAttributeMappings;

@ViewBinding(simpleOneWayProperties = {})
public class MapViewExtensions extends CustomViewBinding<MapView> {
	@Override
	public void mapBindingAttributes(BindingAttributeMappings<MapView> mappings) {
		mappings.mapGroupedAttribute(MapViewAttributes.class, "onMapLoaded", "onMapClicked", "markers", "circles");
	}
}
