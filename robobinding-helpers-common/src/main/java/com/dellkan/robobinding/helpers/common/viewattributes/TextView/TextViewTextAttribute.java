package com.dellkan.robobinding.helpers.common.viewattributes.TextView;

import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.widget.TextView;

import org.robobinding.util.PrimitiveTypeUtils;
import org.robobinding.viewattribute.property.OneWayMultiTypePropertyViewAttribute;
import org.robobinding.viewattribute.property.OneWayPropertyViewAttribute;

public class TextViewTextAttribute implements OneWayMultiTypePropertyViewAttribute<TextView> {
    @Override
    public OneWayPropertyViewAttribute<TextView, ?> create(TextView view, Class<?> propertyType) {
        if (PrimitiveTypeUtils.integerIsAssignableFrom(propertyType)) {
            return new ResourceIdTextAttribute();
        } else if (CharSequenceTextAttribute.class.isAssignableFrom(propertyType)) {
            return new CharSequenceTextAttribute();
        } else if (String.class.isAssignableFrom(propertyType)) {
            return new StringTextAttribute();
        } else if (Enum.class.isAssignableFrom(propertyType)) {
            return new EnumAttribute();
        } else if (Spanned.class.isAssignableFrom(propertyType)) {
            return new SpannedAttribute();
        }

        return null;
    }

    static class ResourceIdTextAttribute implements OneWayPropertyViewAttribute<TextView, Integer> {
        @Override
        public void updateView(TextView view, Integer newValue) {
            if (newValue != null && newValue != 0) {
                view.setText(newValue);
            } else {
                view.setText(null);
            }
        }
    }

    static class CharSequenceTextAttribute implements OneWayPropertyViewAttribute<TextView, CharSequence> {
        @Override
        public void updateView(TextView view, CharSequence newValue) {
            view.setText(newValue);
        }
    }

    static class StringTextAttribute implements OneWayPropertyViewAttribute<TextView, String> {
        @Override
        public void updateView(TextView view, String newValue) {
            view.setText(newValue);
        }
    }

    static class EnumAttribute implements OneWayPropertyViewAttribute<TextView, Enum> {
        @Override
        public void updateView(TextView textView, Enum anEnum) {
            textView.setText(anEnum.name());
        }
    }

    static class SpannedAttribute implements OneWayPropertyViewAttribute<TextView, Spanned> {
        @Override
        public void updateView(TextView textView, Spanned value) {
            if (value.getSpans(0, value.length(), URLSpan.class).length > 0) {
                textView.setLinksClickable(true);
                textView.setMovementMethod(LinkMovementMethod.getInstance());
            }
            textView.setText(value);
        }
    }
}
