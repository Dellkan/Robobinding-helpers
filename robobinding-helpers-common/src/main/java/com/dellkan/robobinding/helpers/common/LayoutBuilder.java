package com.dellkan.robobinding.helpers.common;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dellkan.robobinding.helpers.common.viewattributes.EditText.EditTextExtensions;
import com.dellkan.robobinding.helpers.common.viewattributes.ImageView.ImageExtensions;
import com.dellkan.robobinding.helpers.common.viewattributes.TextInputLayout.TextInputLayoutExtensions;
import com.dellkan.robobinding.helpers.common.viewattributes.TextView.TextViewExtensions;
import com.dellkan.robobinding.helpers.common.viewattributes.ViewExtensions;
import com.dellkan.robobinding.helpers.common.viewattributes.WebView.WebViewExtensions;

import org.robobinding.ViewBinder;
import org.robobinding.binder.BinderFactoryBuilder;

public final class LayoutBuilder {
    private LayoutBuilder() {}
    public static BinderFactoryBuilder getBinderBuilder() {
        BinderFactoryBuilder binderFactoryBuilder = new BinderFactoryBuilder();
        binderFactoryBuilder.add(new ViewExtensions().extend(View.class));
        binderFactoryBuilder.add(new TextInputLayoutExtensions().extend(TextInputLayout.class));
        binderFactoryBuilder.add(new ImageExtensions().extend(ImageView.class));
        binderFactoryBuilder.add(new WebViewExtensions().extend(WebView.class));
        binderFactoryBuilder.add(new TextViewExtensions().forView(TextView.class));
        binderFactoryBuilder.add(new EditTextExtensions().forView(EditText.class));

        return binderFactoryBuilder;
    }

    public static ViewBinder getViewBinder(Context context) {
        return getBinderBuilder().build().createViewBinder(context);
    }
}
