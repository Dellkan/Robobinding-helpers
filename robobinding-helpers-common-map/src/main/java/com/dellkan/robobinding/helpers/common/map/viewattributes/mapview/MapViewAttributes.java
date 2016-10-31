package com.dellkan.robobinding.helpers.common.map.viewattributes.mapview;

import com.dellkan.robobinding.helpers.common.EventAttributeResolver;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import org.robobinding.BindingContext;
import org.robobinding.attribute.ChildAttributeResolverMappings;
import org.robobinding.attribute.Command;
import org.robobinding.attribute.ResolvedGroupAttributes;
import org.robobinding.viewattribute.event.EventViewAttribute;
import org.robobinding.viewattribute.grouped.ChildViewAttributesBuilder;
import org.robobinding.viewattribute.grouped.GroupedViewAttribute;
import org.robobinding.widgetaddon.view.ViewAddOnForView;

public class MapViewAttributes implements GroupedViewAttribute<MapView> {
	@Override
	public String[] getCompulsoryAttributes() {
		return new String[0];
	}

	@Override
	public void mapChildAttributeResolvers(ChildAttributeResolverMappings childAttributeResolverMappings) {
		childAttributeResolverMappings.map(new EventAttributeResolver(), "onMapLoaded");
		childAttributeResolverMappings.map(new EventAttributeResolver(), "onMapClicked");
	}

	@Override
	public void validateResolvedChildAttributes(ResolvedGroupAttributes resolvedGroupAttributes) {

	}

	@Override
	public void setupChildViewAttributes(MapView mapView, ChildViewAttributesBuilder<MapView> childViewAttributesBuilder) {
		if (childViewAttributesBuilder.hasAttribute("onMapLoaded")) {
			childViewAttributesBuilder.add("onMapLoaded", new MapViewLoadedAttribute());
		}
		if (childViewAttributesBuilder.hasAttribute("onMapClicked")) {
			childViewAttributesBuilder.add("onMapClicked", new MapViewClickedAttribute());
		}
	}

	@Override
	public void postBind(MapView mapView, BindingContext bindingContext) {

	}

	private static class MapViewLoadedAttribute implements EventViewAttribute<MapView, ViewAddOnForView> {
		@Override
		public void bind(ViewAddOnForView viewAddOnForView, final Command command, final MapView mapView) {
			mapView.getMapAsync(new OnMapReadyCallback() {
				@Override
				public void onMapReady(GoogleMap googleMap) {
					command.invoke(new MapViewLoadedEvent(mapView, googleMap));
				}
			});
		}

		@Override
		public Class<?> getEventType() {
			return MapViewLoadedEvent.class;
		}
	}

	private static class MapViewClickedAttribute implements EventViewAttribute<MapView, ViewAddOnForView> {
		@Override
		public void bind(ViewAddOnForView viewAddOnForView, final Command command, final MapView mapView) {
			mapView.getMapAsync(new OnMapReadyCallback() {
				@Override
				public void onMapReady(GoogleMap googleMap) {
					googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
						@Override
						public void onMapClick(LatLng latLng) {
							command.invoke(new MapViewClickedEvent(mapView, latLng));
						}
					});
				}
			});
		}

		@Override
		public Class<?> getEventType() {
			return MapViewClickedEvent.class;
		}
	}
}
