package com.dellkan.robobinding.helpers.common.viewattributes.TextInputLayout;

import android.content.res.Resources;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;

import org.robobinding.util.PrimitiveTypeUtils;
import org.robobinding.viewattribute.property.OneWayMultiTypePropertyViewAttribute;
import org.robobinding.viewattribute.property.OneWayPropertyViewAttribute;

public class TextInputLayoutErrorAttribute implements OneWayMultiTypePropertyViewAttribute<TextInputLayout> {
    @Override
    public OneWayPropertyViewAttribute<TextInputLayout, ?> create(TextInputLayout view, Class<?> propertyType) {
        if (PrimitiveTypeUtils.integerIsAssignableFrom(propertyType)) {
            return new ResourceIdErrorAttribute();
        } else if (String.class.isAssignableFrom(propertyType)) {
            return new StringErrorAttribute();
        }

        return null;
    }

    public static class ResourceIdErrorAttribute implements OneWayPropertyViewAttribute<TextInputLayout, Integer> {
        private int mLastError;
        @Override
        public void updateView(TextInputLayout textInputLayout, Integer newValue) {
            if (mLastError != newValue) {
                mLastError = newValue;
                try {
                    String text = textInputLayout.getContext().getString(newValue);
                    if (TextUtils.isEmpty(text)) {
                        textInputLayout.setError(null);
                        textInputLayout.setErrorEnabled(false);
                    } else {
                        textInputLayout.setError(text);
                    }
                } catch (Resources.NotFoundException e) {
                    textInputLayout.setError(null);
                    textInputLayout.setErrorEnabled(false);
                }
            }
        }
    }

    public static class StringErrorAttribute implements OneWayPropertyViewAttribute<TextInputLayout, String> {
        private String mLastError = "";
        @Override
        public void updateView(TextInputLayout textInputLayout, String newValue) {
            if (!mLastError.equals(newValue)) {
                mLastError = newValue;
                if (TextUtils.isEmpty(newValue)) {
                    textInputLayout.setError(null);
                    textInputLayout.setErrorEnabled(false);
                } else {
                    textInputLayout.setError(newValue);
                }
            }
        }
    }
}
