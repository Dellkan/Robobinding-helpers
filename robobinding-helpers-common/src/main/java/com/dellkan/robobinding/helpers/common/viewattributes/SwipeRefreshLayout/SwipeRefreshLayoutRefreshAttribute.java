package com.dellkan.robobinding.helpers.common.viewattributes.SwipeRefreshLayout;

import android.support.v4.widget.SwipeRefreshLayout;

import org.robobinding.viewattribute.property.OneWayPropertyViewAttribute;

public class SwipeRefreshLayoutRefreshAttribute implements OneWayPropertyViewAttribute<SwipeRefreshLayout, Boolean> {
    @Override
    public void updateView(SwipeRefreshLayout swipeRefreshLayout, Boolean value) {
        swipeRefreshLayout.setRefreshing(value);
    }
}
