package com.dellkan.robobinding.helpers.common.map.viewattributes.mapview;

import android.view.View;

import com.google.android.gms.maps.GoogleMap;

import org.robobinding.widget.view.AbstractViewEvent;

public class MapViewLoadedEvent extends AbstractViewEvent {
	private GoogleMap map;
	protected MapViewLoadedEvent(View view, GoogleMap map) {
		super(view);
		this.map = map;
	}

	public GoogleMap getMap() {
		return map;
	}
}
