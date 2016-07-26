package com.dellkan.robobinding.helpers.common.viewattributes.SwipeRefreshLayout;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import org.robobinding.attribute.Command;
import org.robobinding.viewattribute.event.EventViewAttribute;
import org.robobinding.widget.view.AbstractViewEvent;
import org.robobinding.widgetaddon.view.ViewAddOnForView;


public class SwipeRefreshLayoutOnRefreshAttribute implements EventViewAttribute<SwipeRefreshLayout, ViewAddOnForView> {
    @Override
    public void bind(ViewAddOnForView viewAddOn, final Command command, final SwipeRefreshLayout view) {
        view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                command.invoke(new RefreshEvent(view));
            }
        });
    }

    @Override
    public Class<?> getEventType() {
        return RefreshEvent.class;
    }

    public static class RefreshEvent extends AbstractViewEvent {
        protected RefreshEvent(View view) {
            super(view);
        }
    }
}
