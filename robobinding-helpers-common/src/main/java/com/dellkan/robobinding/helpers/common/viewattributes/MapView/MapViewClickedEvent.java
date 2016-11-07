package com.dellkan.robobinding.helpers.common.viewattributes.MapView;

import android.view.View;

import com.google.android.gms.maps.model.LatLng;

import org.robobinding.widget.view.AbstractViewEvent;

public class MapViewClickedEvent extends AbstractViewEvent {
	private LatLng position;
	protected MapViewClickedEvent(View view, LatLng position) {
		super(view);
		this.position = position;
	}

	public LatLng getPosition() {
		return position;
	}
}
