package com.dellkan.robobinding.helpers.common.viewattributes.EditText;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import org.robobinding.property.ValueModel;
import org.robobinding.util.PrimitiveTypeUtils;
import org.robobinding.viewattribute.property.TwoWayMultiTypePropertyViewAttribute;
import org.robobinding.viewattribute.property.TwoWayPropertyViewAttribute;
import org.robobinding.widgetaddon.ViewAddOn;

public class EditTextTextAttribute implements TwoWayMultiTypePropertyViewAttribute<EditText> {
    @Override
    public TwoWayPropertyViewAttribute<EditText, ?, ?> create(EditText editText, Class<?> propertyType) {
        if (PrimitiveTypeUtils.integerIsAssignableFrom(propertyType)) {
            return new ResourceIdTextAttribute();
        } else if (CharSequenceTextAttribute.class.isAssignableFrom(propertyType)) {
            return new CharSequenceTextAttribute();
        } else if (String.class.isAssignableFrom(propertyType)) {
            return new StringTextAttribute();
        }

        return null;
    }

    public static class ResourceIdTextAttribute implements TwoWayPropertyViewAttribute<EditText, ViewAddOn, Integer> {
        @Override
        public void updateView(EditText editText, Integer integer, ViewAddOn viewAddOn) {
            editText.setText(integer);
        }

        @Override
        public void observeChangesOnTheView(ViewAddOn viewAddOn, final ValueModel<Integer> valueModel, EditText view) {

        }
    }

    public static class CharSequenceTextAttribute implements TwoWayPropertyViewAttribute<EditText, ViewAddOn, CharSequence> {
        @Override
        public void updateView(EditText editText, CharSequence charSequence, ViewAddOn viewAddOn) {
            editText.setText(charSequence);
        }

        @Override
        public void observeChangesOnTheView(ViewAddOn viewAddOn, final ValueModel<CharSequence> valueModel, EditText view) {
            view.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    valueModel.setValue(s);
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    public static class StringTextAttribute implements TwoWayPropertyViewAttribute<EditText, ViewAddOn, String> {
        @Override
        public void updateView(EditText editText, String charSequence, ViewAddOn viewAddOn) {
            editText.setText(charSequence);
        }

        @Override
        public void observeChangesOnTheView(ViewAddOn viewAddOn, final ValueModel<String> valueModel, EditText view) {
            view.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    valueModel.setValue(s.toString());
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }
}
