package com.dellkan.robobinding.helpers.common.viewattributes.View;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.robobinding.util.PrimitiveTypeUtils;
import org.robobinding.viewattribute.property.OneWayMultiTypePropertyViewAttribute;
import org.robobinding.viewattribute.property.OneWayPropertyViewAttribute;

import java.lang.ref.WeakReference;

public class ViewBackgroundAttribute implements OneWayMultiTypePropertyViewAttribute<View> {
    @Override
    public OneWayPropertyViewAttribute<View, ?> create(View view, Class<?> propertyType) {
        if (String.class.isAssignableFrom(propertyType)) {
            return new StringURLResourceAttribute();
        } else if (PrimitiveTypeUtils.integerIsAssignableFrom(propertyType)) {
            return new DrawableResourceAttribute();
        }
        return null;
    }

    private WeakReference<View> mView = new WeakReference<View>(null);

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            View view = mView.get();
            if (view != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(new BitmapDrawable(view.getResources(), bitmap));
                } else {
                    view.setBackgroundDrawable(new BitmapDrawable(view.getResources(), bitmap));
                }
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    public class StringURLResourceAttribute implements OneWayPropertyViewAttribute<View, String> {
        @Override
        public void updateView(View view, String url) {
            mView = new WeakReference<>(view);
            Picasso.with(view.getContext())
                    .load(url)
                    //.fit()
                    //.centerInside()
                    .into(target);
        }
    }

    public class DrawableResourceAttribute implements OneWayPropertyViewAttribute<View, Integer> {
        @Override
        public void updateView(View view, @DrawableRes Integer drawable) {
            mView = new WeakReference<>(view);
            Picasso.with(view.getContext())
                    .load(drawable)
                    //.fit()
                    //.centerInside()
                    .into(target);
        }
    }
}
