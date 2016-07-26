package com.dellkan.robobinding.helpers.common.viewattributes.AdapterView;

import android.view.View;
import android.widget.AdapterView;

import org.robobinding.BindingContext;
import org.robobinding.attribute.ChildAttributeResolverMappings;
import org.robobinding.attribute.ResolvedGroupAttributes;
import org.robobinding.attribute.StaticResourceAttribute;
import org.robobinding.viewattribute.grouped.ChildViewAttributeWithAttribute;
import org.robobinding.viewattribute.grouped.ChildViewAttributesBuilder;
import org.robobinding.viewattribute.grouped.GroupedViewAttribute;

import static org.robobinding.attribute.ChildAttributeResolvers.staticResourceAttributeResolver;

public class AdapterViewGroupedAttributes implements GroupedViewAttribute<AdapterView> {
	@Override
	public String[] getCompulsoryAttributes() {
		return new String[]{"emptyView"};
	}

	@Override
	public void mapChildAttributeResolvers(ChildAttributeResolverMappings childAttributeResolverMappings) {
		childAttributeResolverMappings.map(staticResourceAttributeResolver(), "emptyView");
	}

	@Override
	public void validateResolvedChildAttributes(ResolvedGroupAttributes resolvedGroupAttributes) {

	}

	@Override
	public void setupChildViewAttributes(AdapterView adapterView, ChildViewAttributesBuilder<AdapterView> childViewAttributesBuilder) {
		childViewAttributesBuilder.add("emptyView", new AdapterViewEmptyViewAttribute(adapterView));
	}

	@Override
	public void postBind(AdapterView adapterView, BindingContext bindingContext) {

	}

	// Sub class
	private class AdapterViewEmptyViewAttribute implements ChildViewAttributeWithAttribute<StaticResourceAttribute> {
		private StaticResourceAttribute attribute;
		private AdapterView<?> adapterView;

		private AdapterViewEmptyViewAttribute(AdapterView adapterView) {
			this.adapterView = adapterView;
		}

		@Override
		public void setAttribute(StaticResourceAttribute attribute) {
			this.attribute = attribute;
		}

		@Override
		public void bindTo(BindingContext bindingContext) {
			int layoutId = attribute.getResourceId(bindingContext.getContext());
			View emptyLayout = adapterView.getRootView().findViewById(layoutId);
			if (emptyLayout == null) {
				throw new RuntimeException("No adapterView exists with the ID that was appointed by emptyViewLayout: " + attribute.getName());
			}
			adapterView.setEmptyView(emptyLayout);
		}
	}
}
