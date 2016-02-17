package com.dellkan.robobinding.helpers.common.viewattributes.EditText;

import android.widget.EditText;

import org.robobinding.annotation.ViewBinding;
import org.robobinding.customviewbinding.CustomViewBinding;
import org.robobinding.viewbinding.BindingAttributeMappings;
import org.robobinding.widget.edittext.OnTextChangedAttribute;

@ViewBinding(simpleOneWayProperties = {})
public class EditTextExtensions extends CustomViewBinding<EditText> {
    @Override
    public void mapBindingAttributes(BindingAttributeMappings<EditText> mappings) {
        super.mapBindingAttributes(mappings);
        mappings.mapTwoWayMultiTypeProperty(EditTextTextAttribute.class, "text");
        mappings.mapOneWayMultiTypeProperty(EditTextHintAttribute.class, "hint");
        mappings.mapEvent(OnTextChangedAttribute.class, "onTextChanged");
    }
}
