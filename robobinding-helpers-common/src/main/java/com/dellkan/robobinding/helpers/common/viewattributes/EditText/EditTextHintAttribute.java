package com.dellkan.robobinding.helpers.common.viewattributes.EditText;

import android.widget.EditText;

import org.robobinding.util.PrimitiveTypeUtils;
import org.robobinding.viewattribute.property.OneWayMultiTypePropertyViewAttribute;
import org.robobinding.viewattribute.property.OneWayPropertyViewAttribute;

public class EditTextHintAttribute implements OneWayMultiTypePropertyViewAttribute<EditText> {
    @Override
    public OneWayPropertyViewAttribute<EditText, ?> create(EditText editText, Class<?> propertyType) {
        if (PrimitiveTypeUtils.integerIsAssignableFrom(propertyType)) {
            return new ResourceIdHintAttribute();
        } else if (CharSequenceHintAttribute.class.isAssignableFrom(propertyType)) {
            return new CharSequenceHintAttribute();
        } else if (String.class.isAssignableFrom(propertyType)) {
            return new StringHintAttribute();
        }

        return null;
    }

    public static class ResourceIdHintAttribute implements OneWayPropertyViewAttribute<EditText, Integer> {
        @Override
        public void updateView(EditText editText, Integer integer) {
            editText.setHint(integer);
        }
    }

    public static class CharSequenceHintAttribute implements OneWayPropertyViewAttribute<EditText, CharSequence> {
        @Override
        public void updateView(EditText editText, CharSequence value) {
            editText.setHint(value);
        }
    }

    public static class StringHintAttribute implements OneWayPropertyViewAttribute<EditText, String> {
        @Override
        public void updateView(EditText editText, String value) {
            editText.setHint(value);
        }
    }
}
