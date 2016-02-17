package com.dellkan.robobinding.helpers.common.viewattributes.WebView;

import android.annotation.SuppressLint;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.robobinding.BindingContext;
import org.robobinding.attribute.ChildAttributeResolverMappings;
import org.robobinding.attribute.ResolvedGroupAttributes;
import org.robobinding.attribute.StaticResourceAttribute;
import org.robobinding.viewattribute.grouped.ChildViewAttributeWithAttribute;
import org.robobinding.viewattribute.grouped.ChildViewAttributesBuilder;
import org.robobinding.viewattribute.grouped.GroupedViewAttribute;
import org.robobinding.viewattribute.property.OneWayPropertyViewAttribute;

import static org.robobinding.attribute.ChildAttributeResolvers.propertyAttributeResolver;
import static org.robobinding.attribute.ChildAttributeResolvers.staticResourceAttributeResolver;

public class WebViewAttributes implements GroupedViewAttribute<WebView> {
    private WebViewErrorLayoutAttribute errorLayoutAttribute;
    @Override
    public String[] getCompulsoryAttributes() {
        return new String[] {"src"};
    }

    @Override
    public void mapChildAttributeResolvers(ChildAttributeResolverMappings resolverMappings) {
        resolverMappings.map(propertyAttributeResolver(), "src");
        resolverMappings.map(staticResourceAttributeResolver(), "errorLayout");
    }

    @Override
    public void validateResolvedChildAttributes(ResolvedGroupAttributes groupAttributes) {

    }

    @Override
    public void setupChildViewAttributes(WebView view, ChildViewAttributesBuilder<WebView> childViewAttributesBuilder, BindingContext bindingContext) {
        childViewAttributesBuilder.add("src", new WebViewSourceAttribute());
        if (childViewAttributesBuilder.hasAttribute("errorLayout")) {
            childViewAttributesBuilder.add("errorLayout", errorLayoutAttribute = new WebViewErrorLayoutAttribute(view.getRootView()));
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void postBind(WebView webView, BindingContext bindingContext) {
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (errorLayoutAttribute != null) {
                    view.setVisibility(View.GONE);
                    errorLayoutAttribute.getView().setVisibility(View.VISIBLE);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
    }

    // Attribute for error
    private class WebViewErrorLayoutAttribute implements ChildViewAttributeWithAttribute<StaticResourceAttribute> {
        private View view;
        private StaticResourceAttribute attribute;
        private View parent;

        private WebViewErrorLayoutAttribute(View parent) {
            this.parent = parent;
        }

        @Override
        public void setAttribute(StaticResourceAttribute attribute) {
            this.attribute = attribute;
        }

        @Override
        public void bindTo(BindingContext bindingContext) {
            int layoutId = attribute.getResourceId(bindingContext.getContext());
            view = parent.findViewById(layoutId);
            if (view == null) {
                throw new RuntimeException("No view exists with the ID that was appointed by errorLayout: " + attribute.getName());
            }
        }

        public View getView() {
            return view;
        }
    }

    // Attribute for source
    private class WebViewSourceAttribute implements OneWayPropertyViewAttribute<WebView, String> {
        @Override
        public void updateView(WebView webView, String url) {
            webView.loadUrl(url);
            if (errorLayoutAttribute != null) {
                webView.setVisibility(View.VISIBLE);
                errorLayoutAttribute.getView().setVisibility(View.GONE);
            }
        }
    }
}
