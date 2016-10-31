package com.dellkan.robobinding.helpers.common.viewattributes.View;

import android.view.View;

import org.robobinding.attribute.Command;
import org.robobinding.viewattribute.event.EventViewAttribute;
import org.robobinding.widgetaddon.view.ViewAddOnForView;

public class ViewInitAttribute implements EventViewAttribute<View, ViewAddOnForView> {
    @Override
    public void bind(ViewAddOnForView viewAddon, Command command, View view) {
        command.invoke(new InitEvent(view));
    }

    @Override
    public Class<InitEvent> getEventType() {
        return InitEvent.class;
    }
}
