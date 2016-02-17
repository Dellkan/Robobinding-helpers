package com.dellkan.robobinding.helpers.common.viewattributes.WebView;

import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

import org.robobinding.attribute.Command;
import org.robobinding.viewattribute.event.EventViewAttribute;
import org.robobinding.widget.view.ClickEvent;
import org.robobinding.widgetaddon.view.ViewAddOnForView;

public class WebViewClickAttribute implements EventViewAttribute<WebView, ViewAddOnForView> {
    public final static int FINGER_RELEASED = 0;
    public final static int FINGER_DOWN = 1;
    public final static int FINGER_DRAGGING = 2;

    private int fingerState = FINGER_RELEASED;

    private float posY;
    private long lastDragged;

    private final int DRAG_LIMIT = 10;
    private final int DRAG_DELAY_CLICK = 500;

    @Override
    public void bind(ViewAddOnForView webViewAddon, final Command command, WebView webView) {
        webViewAddon.addOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        fingerState = FINGER_DOWN;
                        posY = event.getY();
                        break;

                    case MotionEvent.ACTION_UP:
                        if (fingerState == FINGER_DOWN && (Math.abs(event.getY() - posY) > DRAG_LIMIT)) {
                            fingerState = FINGER_DRAGGING;
                        }

                        long time = (event.getEventTime() - event.getDownTime());

                        if (fingerState == FINGER_DOWN && (time > 25 && time <= 250) && (event.getEventTime() > (lastDragged + DRAG_DELAY_CLICK))) {
                            ClickEvent clickEvent = new ClickEvent(view);
                            command.invoke(clickEvent);
                        } else if (fingerState == FINGER_DRAGGING) {
                            lastDragged = event.getEventTime();
                        }
                        fingerState = FINGER_RELEASED;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (fingerState == FINGER_DOWN && (Math.abs(event.getY() - posY) > DRAG_LIMIT)) {
                            fingerState = FINGER_DRAGGING;
                        }
                        break;

                    default:
                        fingerState = FINGER_RELEASED;

                }

                return false;
            }
        });
    }

    @Override
    public Class<ClickEvent> getEventType() {
        return ClickEvent.class;
    }
}
