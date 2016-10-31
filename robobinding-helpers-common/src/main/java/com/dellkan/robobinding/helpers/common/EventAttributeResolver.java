package com.dellkan.robobinding.helpers.common;

import org.robobinding.attribute.AbstractAttribute;
import org.robobinding.attribute.ChildAttributeResolver;
import org.robobinding.attribute.EventAttribute;


public class EventAttributeResolver implements ChildAttributeResolver {
	@Override
	public AbstractAttribute resolveChildAttribute(String attribute, String attributeValue) {
		return new EventAttribute(attribute, attributeValue);
	}
}
