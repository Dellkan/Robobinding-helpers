package com.dellkan.robobinding.helpers.common.viewattributes.View;

import android.support.v4.view.ViewCompat;
import android.view.View;

import org.robobinding.viewattribute.property.OneWayPropertyViewAttribute;

public class ViewTransitionNameAttribute implements OneWayPropertyViewAttribute<View, String> {
	@Override
	public void updateView(View view, String name) {
		ViewCompat.setTransitionName(view, name);
	}
}
