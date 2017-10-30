package com.vaadin.toolkit.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kolisek
 */
public abstract class RxCollection<T> extends RxField<T>
{

	private List<RxBean<T>> rxBeanList = new ArrayList<>();

	public RxCollection(String property)
	{
		super(property);
	}

	public RxCollection(boolean visible, boolean enabled, String caption, String property)
	{
		super(visible, enabled, caption, property);
	}
}
