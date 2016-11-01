package com.dellkan.robobinding.helpers.common.map.viewattributes.mapview;

import com.dellkan.robobinding.helpers.common.EventAttributeResolver;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.robobinding.BindingContext;
import org.robobinding.attribute.ChildAttributeResolverMappings;
import org.robobinding.attribute.Command;
import org.robobinding.attribute.ResolvedGroupAttributes;
import org.robobinding.viewattribute.event.EventViewAttribute;
import org.robobinding.viewattribute.grouped.ChildViewAttributesBuilder;
import org.robobinding.viewattribute.grouped.GroupedViewAttribute;
import org.robobinding.viewattribute.property.OneWayMultiTypePropertyViewAttribute;
import org.robobinding.viewattribute.property.OneWayPropertyViewAttribute;
import org.robobinding.widgetaddon.view.ViewAddOnForView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.robobinding.attribute.ChildAttributeResolvers.propertyAttributeResolver;

public class MapViewAttributes implements GroupedViewAttribute<MapView> {
	@Override
	public String[] getCompulsoryAttributes() {
		return new String[0];
	}

	@Override
	public void mapChildAttributeResolvers(ChildAttributeResolverMappings childAttributeResolverMappings) {
		childAttributeResolverMappings.map(new EventAttributeResolver(), "onMapLoaded");
		childAttributeResolverMappings.map(new EventAttributeResolver(), "onMapClicked");
		childAttributeResolverMappings.map(propertyAttributeResolver(), "markers");
		childAttributeResolverMappings.map(propertyAttributeResolver(), "circles");
	}

	@Override
	public void validateResolvedChildAttributes(ResolvedGroupAttributes resolvedGroupAttributes) {
	}

	@Override
	public void setupChildViewAttributes(MapView mapView, ChildViewAttributesBuilder<MapView> childViewAttributesBuilder) {
		if (childViewAttributesBuilder.hasAttribute("onMapLoaded")) {
			childViewAttributesBuilder.add("onMapLoaded", new LoadedAttribute());
		}
		if (childViewAttributesBuilder.hasAttribute("onMapClicked")) {
			childViewAttributesBuilder.add("onMapClicked", new ClickedAttribute());
		}
		if (childViewAttributesBuilder.hasAttribute("markers")) {
			childViewAttributesBuilder.add("markers", new MarkersAttribute());
		}
		if (childViewAttributesBuilder.hasAttribute("circles")) {
			childViewAttributesBuilder.add("circles", new CirclesAttribute());
		}
	}

	@Override
	public void postBind(MapView mapView, BindingContext bindingContext) {

	}

	private static class LoadedAttribute implements EventViewAttribute<MapView, ViewAddOnForView> {
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

	private static class ClickedAttribute implements EventViewAttribute<MapView, ViewAddOnForView> {
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

	private static class MarkersAttribute implements OneWayMultiTypePropertyViewAttribute<MapView> {
		@Override
		public OneWayPropertyViewAttribute<MapView, ?> create(MapView mapView, Class<?> propertyType) {
			if (Collection.class.isAssignableFrom(propertyType)) {
				return new MarkersListAttribute();
			} else if (MapViewMarker.class.isAssignableFrom(propertyType)) {
				return new MarkerAttribute();
			}
			return null;
		}

		private static class MarkersListAttribute implements OneWayPropertyViewAttribute<MapView, Collection<MapViewMarker>> {
			List<WeakReference<Marker>> markers = new ArrayList<>();
			@Override
			public void updateView(MapView mapView, final Collection<MapViewMarker> mapViewMarkers) {
				mapView.getMapAsync(new OnMapReadyCallback() {
					@Override
					public void onMapReady(GoogleMap googleMap) {
						for (MapViewMarker mapViewMarker : mapViewMarkers) {
							if (mapViewMarker.getMarker() != null) {
								mapViewMarker.getMarker().setPosition(mapViewMarker.getPosition());
							} else {
								mapViewMarker.setMarker(googleMap.addMarker(new MarkerOptions()
										.position(mapViewMarker.getPosition())
										.draggable(mapViewMarker.isDraggable())
								));
							}
						}
					}
				});
			}
		}

		private static class MarkerAttribute implements OneWayPropertyViewAttribute<MapView, MapViewMarker> {
			@Override
			public void updateView(MapView mapView, final MapViewMarker mapViewMarker) {
				if (mapViewMarker.getMarker() != null) {
					mapViewMarker.getMarker().remove();
					mapView.getMapAsync(new OnMapReadyCallback() {
						@Override
						public void onMapReady(GoogleMap googleMap) {
							mapViewMarker.setMarker(googleMap.addMarker(new MarkerOptions()
									.position(mapViewMarker.getPosition())
									.draggable(mapViewMarker.isDraggable())
							));
						}
					});
				}
			}
		}
	}

	private static class CirclesAttribute implements OneWayMultiTypePropertyViewAttribute<MapView> {
		@Override
		public OneWayPropertyViewAttribute<MapView, ?> create(MapView mapView, Class<?> propertyType) {
			if (Collection.class.isAssignableFrom(propertyType)) {
				return new CirclesListAttribute();
			} else if (MapViewCircle.class.isAssignableFrom(propertyType)) {
				return new CircleAttribute();
			}
			return null;
		}

		private static class CirclesListAttribute implements OneWayPropertyViewAttribute<MapView, Collection<MapViewCircle>> {
			List<WeakReference<Marker>> markers = new ArrayList<>();
			@Override
			public void updateView(MapView mapView, final Collection<MapViewCircle> mapViewMarkers) {
				mapView.getMapAsync(new OnMapReadyCallback() {
					@Override
					public void onMapReady(GoogleMap googleMap) {
						for (MapViewCircle mapViewCircle : mapViewMarkers) {
							if (mapViewCircle.getCircle() != null) {
								mapViewCircle.getCircle().setCenter(mapViewCircle.getPosition());
							} else {
								mapViewCircle.setCircle(googleMap.addCircle(new CircleOptions()
										.center(mapViewCircle.getPosition())
										.clickable(mapViewCircle.isClickable())
								));
							}
						}
					}
				});
			}
		}

		private static class CircleAttribute implements OneWayPropertyViewAttribute<MapView, MapViewCircle> {
			@Override
			public void updateView(MapView mapView, final MapViewCircle mapViewCircle) {
				mapView.getMapAsync(new OnMapReadyCallback() {
					@Override
					public void onMapReady(GoogleMap googleMap) {
						if (mapViewCircle.getCircle() != null) {
							mapViewCircle.getCircle().setCenter(mapViewCircle.getPosition());
						} else {
							mapViewCircle.setCircle(googleMap.addCircle(new CircleOptions()
									.center(mapViewCircle.getPosition())
									.clickable(mapViewCircle.isClickable())
							));
						}
					}
				});
			}
		}
	}
}
