package com.dellkan.robobinding.helpers.common.viewattributes.View;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.View;

import org.robobinding.util.PrimitiveTypeUtils;
import org.robobinding.viewattribute.property.OneWayMultiTypePropertyViewAttribute;
import org.robobinding.viewattribute.property.OneWayPropertyViewAttribute;

public class ViewBackgroundColorAttribute implements OneWayMultiTypePropertyViewAttribute<View> {
    @Override
    public OneWayPropertyViewAttribute<View, ?> create(View view, Class<?> propertyType) {
        if (String.class.isAssignableFrom(propertyType)) {
            return new RGBAttribute();
        } else if (PrimitiveTypeUtils.integerIsAssignableFrom(propertyType)) {
            return new IntegerResourceAttribute();
        }
        return null;
    }

    public class RGBAttribute implements OneWayPropertyViewAttribute<View, String> {
        @Override
        public void updateView(View view, String color) {
            GradientDrawable background = (GradientDrawable) view.getBackground();
            if (color != null && color.length() > 0) {
                try {
                    background.setColor(Color.parseColor(color));
                } catch (IllegalArgumentException e) {

                }
            } else {
                background.setColor(Color.GRAY);
            }
        }
    }

    public class IntegerResourceAttribute implements OneWayPropertyViewAttribute<View, Integer> {
        @Override
        public void updateView(View view, Integer colorSource) {
            int color = colorSource;
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    color = view.getResources().getColor(colorSource, null);
                } else {
                    color = view.getResources().getColor(colorSource);
                }
            } catch (Resources.NotFoundException e) {}

            view.setBackgroundColor(color);
//            GradientDrawable background = (GradientDrawable) view.getBackground();
//            background.setColor(color);
        }
    }
}