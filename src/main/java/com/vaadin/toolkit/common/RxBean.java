package com.vaadin.toolkit.common;

/**
 * @author Kolisek
 */
public abstract class RxBean<T> extends RxField<T>
{
	public RxBean(String property)
	{
		super(property);
	}

	public RxBean(boolean visible, boolean enabled, String caption, String property)
	{
		super(visible, enabled, caption, property);
	}
}
