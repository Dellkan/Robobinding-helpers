package com.dellkan.robobinding.helpers.common.viewattributes.ImageView;

import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.robobinding.BindingContext;
import org.robobinding.attribute.ChildAttributeResolverMappings;
import org.robobinding.attribute.EnumAttribute;
import org.robobinding.attribute.ResolvedGroupAttributes;
import org.robobinding.viewattribute.grouped.ChildViewAttributesBuilder;
import org.robobinding.viewattribute.grouped.GroupedViewAttribute;
import org.robobinding.viewattribute.property.OneWayMultiTypePropertyViewAttribute;
import org.robobinding.viewattribute.property.OneWayPropertyViewAttribute;

import static org.robobinding.attribute.ChildAttributeResolvers.enumChildAttributeResolver;
import static org.robobinding.attribute.ChildAttributeResolvers.propertyAttributeResolver;

public class ImageAttributes implements GroupedViewAttribute<ImageView> {
    protected enum BOOLEAN {
        TRUE, FALSE;

        private final String value;
        private BOOLEAN() {
            value = this.name().toLowerCase();
        }

        public String toString() {
            return value;
        }
    }
    private ImageSrcAttribute srcAttribute;
    @Override
    public String[] getCompulsoryAttributes() {
        return new String[] {"src"};
    }

    @Override
    public void mapChildAttributeResolvers(ChildAttributeResolverMappings childAttributeResolverMappings) {
        childAttributeResolverMappings.map(propertyAttributeResolver(), "src");
        childAttributeResolverMappings.map(enumChildAttributeResolver(BOOLEAN.class), "fit");
    }

    @Override
    public void validateResolvedChildAttributes(ResolvedGroupAttributes resolvedGroupAttributes) {

    }

    @Override
    public void setupChildViewAttributes(ImageView imageView, ChildViewAttributesBuilder<ImageView> childViewAttributesBuilder, BindingContext bindingContext) {
        childViewAttributesBuilder.add("src", srcAttribute = new ImageSrcAttribute());
        if (childViewAttributesBuilder.hasAttribute("fit")) {
            EnumAttribute<BOOLEAN> value = childViewAttributesBuilder.enumAttributeFor("fit");
            srcAttribute.setFit(value.getValue().equals(BOOLEAN.TRUE));
        }
    }

    @Override
    public void postBind(ImageView imageView, BindingContext bindingContext) {

    }

    // Src
    class ImageSrcAttribute implements OneWayMultiTypePropertyViewAttribute<ImageView> {
        private boolean fit;

        public void setFit(boolean toggle) {
            fit = toggle;
        }

        @Override
        public OneWayPropertyViewAttribute<ImageView, ?> create(ImageView imageView, Class<?> propertyType) {
            if (Uri.class.isAssignableFrom(propertyType)) {
                return new UriImageSrcAttribute();
            } else if (String.class.isAssignableFrom(propertyType)) {
                return new StringImageSrcAttribute();
            } else if (Integer.class.isAssignableFrom(propertyType)) {
                return new IntImageSrcAttribute();
            }

            return null;
        }

        class UriImageSrcAttribute implements OneWayPropertyViewAttribute<ImageView, Uri> {
            @Override
            public void updateView(ImageView imageView, Uri source) {
                RequestCreator request = Picasso.with(imageView.getContext())
                        .load(source);

                if (fit) {
                    request.fit().centerInside();
                }

                request.into(imageView);
            }
        }

        class StringImageSrcAttribute implements OneWayPropertyViewAttribute<ImageView, String> {
            @Override
            public void updateView(ImageView imageView, String source) {
                RequestCreator request = Picasso.with(imageView.getContext())
                        .load(source);

                if (fit) {
                    request.fit().centerInside();
                }

                request.into(imageView);
            }
        }

        class IntImageSrcAttribute implements OneWayPropertyViewAttribute<ImageView, Integer> {
            @Override
            public void updateView(ImageView imageView, Integer source) {
                if (source != null && source != 0) {
                    RequestCreator request = Picasso.with(imageView.getContext())
                            .load(source);

                    if (fit) {
                        request.fit().centerInside();
                    }

                    request.into(imageView);
                } else {
                    imageView.setImageDrawable(null);
                }
            }
        }
    }
}
