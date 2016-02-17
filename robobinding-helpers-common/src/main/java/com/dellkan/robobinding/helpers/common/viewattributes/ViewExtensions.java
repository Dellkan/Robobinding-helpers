package com.dellkan.robobinding.helpers.common.viewattributes;

import android.view.View;

import org.robobinding.annotation.ViewBinding;
import org.robobinding.customviewbinding.CustomViewBinding;

@ViewBinding(simpleOneWayProperties = {"enabled"})
public class ViewExtensions extends CustomViewBinding<View> {
}
