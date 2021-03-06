package com.dellkan.robobinding.helpers.common.viewattributes.MapView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public interface MapViewMarker {
	Marker getMarker();
	void setMarker(Marker marker);

	LatLng getPosition();

	boolean isDraggable();
}
