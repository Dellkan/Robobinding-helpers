package com.dellkan.robobinding.helpers.common.viewattributes.WebView;

import android.webkit.WebView;

import org.robobinding.attribute.Command;
import org.robobinding.viewattribute.event.EventViewAttribute;
import org.robobinding.widgetaddon.view.ViewAddOnForView;

public class WebViewInitAttribute implements EventViewAttribute<WebView, ViewAddOnForView> {
    @Override
    public void bind(ViewAddOnForView webViewAddon, Command command, WebView webView) {
        command.invoke(new InitEvent(webView));
    }

    @Override
    public Class<InitEvent> getEventType() {
        return InitEvent.class;
    }
}
