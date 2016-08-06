package com.dellkan.robobinding.helpers.common.viewattributes.ImageView;

import android.graphics.drawable.Drawable;
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
    public enum FIT {
        NONE, CROPCENTER, CENTERCROP, CENTERINSIDE;

        private final String value;
        private FIT() {
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
        childAttributeResolverMappings.map(enumChildAttributeResolver(FIT.class), "fit");
    }

    @Override
    public void validateResolvedChildAttributes(ResolvedGroupAttributes resolvedGroupAttributes) {

    }

    @Override
    public void setupChildViewAttributes(ImageView imageView, ChildViewAttributesBuilder<ImageView> childViewAttributesBuilder, BindingContext bindingContext) {
        childViewAttributesBuilder.add("src", srcAttribute = new ImageSrcAttribute());
        srcAttribute.setFit(getEnumValueForFit(childViewAttributesBuilder));
    }

    private FIT getEnumValueForFit(ChildViewAttributesBuilder<ImageView> childViewAttributesBuilder) {
        if (childViewAttributesBuilder.hasAttribute("fit")) {
            EnumAttribute<FIT> value = childViewAttributesBuilder.enumAttributeFor("fit");
            return value.getValue();
        }
        return FIT.NONE;
    }

    @Override
    public void postBind(ImageView imageView, BindingContext bindingContext) {

    }

    // Src
    class ImageSrcAttribute implements OneWayMultiTypePropertyViewAttribute<ImageView> {
        private FIT fit = FIT.NONE;

        public FIT getFit() {
            return fit != null ? fit : FIT.NONE;
        }

        public void setFit(FIT fit) {
            this.fit = fit != null ? fit : FIT.NONE;
        }

        @Override
        public OneWayPropertyViewAttribute<ImageView, ?> create(ImageView imageView, Class<?> propertyType) {
            if (Drawable.class.isAssignableFrom(propertyType)) {
                return new DrawableImageSrcAttribute();
            } else if (Uri.class.isAssignableFrom(propertyType)) {
                return new UriImageSrcAttribute();
            } else if (String.class.isAssignableFrom(propertyType)) {
                return new StringImageSrcAttribute();
            } else if (Integer.class.isAssignableFrom(propertyType)) {
                return new IntImageSrcAttribute();
            }

            return null;
        }

        private void setupCropping(RequestCreator request) {
            switch (getFit()) {
                case CENTERCROP:
                case CROPCENTER:
                    request.centerCrop();
                    break;
                case CENTERINSIDE:
                    request.centerInside();
                    break;
            }

            request.fit();
        }

        class UriImageSrcAttribute implements OneWayPropertyViewAttribute<ImageView, Uri> {
            @Override
            public void updateView(ImageView imageView, Uri source) {
                RequestCreator request = Picasso.with(imageView.getContext())
                        .load(source);

                setupCropping(request);

                request.into(imageView);
            }
        }

        class StringImageSrcAttribute implements OneWayPropertyViewAttribute<ImageView, String> {
            @Override
            public void updateView(ImageView imageView, String source) {
                if (source != null && source.length() > 0) {
                    RequestCreator request = Picasso.with(imageView.getContext())
                            .load(source);

                    setupCropping(request);

                    request.into(imageView);
                }
            }
        }

        class IntImageSrcAttribute implements OneWayPropertyViewAttribute<ImageView, Integer> {
            @Override
            public void updateView(ImageView imageView, Integer source) {
                if (source != null && source != 0) {
                    RequestCreator request = Picasso.with(imageView.getContext())
                            .load(source);

                    setupCropping(request);

                    request.into(imageView);
                } else {
                    imageView.setImageDrawable(null);
                }
            }
        }

        class DrawableImageSrcAttribute implements OneWayPropertyViewAttribute<ImageView, Drawable> {
            @Override
            public void updateView(ImageView view, Drawable newValue) {
                view.setImageDrawable(newValue);
            }
        }
    }
}
