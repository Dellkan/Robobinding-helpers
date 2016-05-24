package com.dellkan.robobinding.helpers.common.viewattributes.WebView;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.robobinding.BindingContext;
import org.robobinding.attribute.ChildAttributeResolverMappings;
import org.robobinding.attribute.ResolvedGroupAttributes;
import org.robobinding.attribute.StaticResourceAttribute;
import org.robobinding.attribute.ValueModelAttribute;
import org.robobinding.viewattribute.grouped.ChildViewAttributeWithAttribute;
import org.robobinding.viewattribute.grouped.ChildViewAttributesBuilder;
import org.robobinding.viewattribute.grouped.GroupedViewAttribute;
import org.robobinding.viewattribute.property.OneWayPropertyViewAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.robobinding.attribute.ChildAttributeResolvers.propertyAttributeResolver;
import static org.robobinding.attribute.ChildAttributeResolvers.staticResourceAttributeResolver;

public class WebViewAttributes implements GroupedViewAttribute<WebView> {
    private WebViewErrorLayoutAttribute errorLayoutAttribute;
    private WebViewAdditionalHeadersAttribute mHeadersAttribute;
    private WebViewRunJavascriptAttribute mRunJavascriptAttribute;
    private WebViewSourceAttribute mSourceAttribute;
    private boolean mFinishedLoading = false;

    @Override
    public String[] getCompulsoryAttributes() {
        return new String[] {"src"};
    }

    @Override
    public void mapChildAttributeResolvers(ChildAttributeResolverMappings resolverMappings) {
        resolverMappings.map(propertyAttributeResolver(), "additionalHeaders");
        resolverMappings.map(propertyAttributeResolver(), "src");
        resolverMappings.map(propertyAttributeResolver(), "runJavascript");
        resolverMappings.map(staticResourceAttributeResolver(), "errorLayout");
    }

    @Override
    public void validateResolvedChildAttributes(ResolvedGroupAttributes groupAttributes) {

    }

    @Override
    public void setupChildViewAttributes(WebView view, ChildViewAttributesBuilder<WebView> childViewAttributesBuilder, BindingContext bindingContext) {
        if (childViewAttributesBuilder.hasAttribute("additionalHeaders")) {
            childViewAttributesBuilder.add("additionalHeaders", mHeadersAttribute = new WebViewAdditionalHeadersAttribute(
                    childViewAttributesBuilder.valueModelAttributeFor("additionalHeaders")
            ));
            mHeadersAttribute.setInitialHeaders(bindingContext);
        }
        if (childViewAttributesBuilder.hasAttribute("runJavascript")) {
            childViewAttributesBuilder.add("runJavascript", mRunJavascriptAttribute = new WebViewRunJavascriptAttribute());
        }
        childViewAttributesBuilder.add("src", mSourceAttribute = new WebViewSourceAttribute());
        if (childViewAttributesBuilder.hasAttribute("errorLayout")) {
            childViewAttributesBuilder.add("errorLayout", errorLayoutAttribute = new WebViewErrorLayoutAttribute(view.getRootView()));
        }
    }

    private List<String> runJavascriptQueue = new ArrayList<>();
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void postBind(WebView webView, BindingContext bindingContext) {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("WebView", consoleMessage.toString());
                return super.onConsoleMessage(consoleMessage);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mFinishedLoading = false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mFinishedLoading = true;
                if (mRunJavascriptAttribute != null) {
                    for (String script : runJavascriptQueue) {
                        mRunJavascriptAttribute.updateView(view, script);
                    }
                }
                runJavascriptQueue.clear();
            }

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
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
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

    // Attribute for extra headers
    // FIXME: Not sure if OneWayPropertyViewAttribute is a good fit for what we're trying to do here.
    private class WebViewAdditionalHeadersAttribute implements OneWayPropertyViewAttribute<WebView, Map<String, Object>> {
        private Map<String, Object> headers = new HashMap<>();
        private ValueModelAttribute attribute;

        public WebViewAdditionalHeadersAttribute(ValueModelAttribute attribute) {
            this.attribute = attribute;
        }

        public void setInitialHeaders(BindingContext bindingContext) {
            Object value = bindingContext.getReadOnlyPropertyValueModel(this.attribute.getPropertyName()).getValue();
            this.headers = (Map<String, Object>) value;
        }

        @Override
        public void updateView(WebView webView, Map<String, Object> headers) {
            this.headers = headers;
        }

        public Map<String, String> getHeaders() {
            Map<String, String> convertedHeaders = new HashMap<>();
            if (headers != null) {
                for (Map.Entry<String, Object> entry : headers.entrySet()) {
                    convertedHeaders.put(
                            entry.getKey(),
                            entry.getValue() != null ? entry.getValue().toString() : null
                    );
                }
            }
            return convertedHeaders;
        }
    }

    private class WebViewRunJavascriptAttribute implements OneWayPropertyViewAttribute<WebView, String> {
        @Override
        public void updateView(WebView view, String newValue) {
            if (mFinishedLoading) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    view.evaluateJavascript(newValue, null);
                } else {
                    view.loadUrl(String.format("javascript:%s", newValue));
                }
            } else {
                runJavascriptQueue.add(newValue);
            }
        }
    }

    // Attribute for source
    private class WebViewSourceAttribute implements OneWayPropertyViewAttribute<WebView, String> {
        @Override
        public void updateView(WebView webView, String url) {
            if (mHeadersAttribute != null) {
                webView.loadUrl(url, mHeadersAttribute.getHeaders());
            } else {
                webView.loadUrl(url);
            }
            if (errorLayoutAttribute != null) {
                webView.setVisibility(View.VISIBLE);
                errorLayoutAttribute.getView().setVisibility(View.GONE);
            }
        }
    }
}
