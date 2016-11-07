package com.dellkan.robobinding.helpers.common.viewattributes.MapView;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;

public interface MapViewCircle {
	Circle getCircle();
	void setCircle(Circle circle);

	LatLng getPosition();
	int getRadius();

	boolean isClickable();
}
