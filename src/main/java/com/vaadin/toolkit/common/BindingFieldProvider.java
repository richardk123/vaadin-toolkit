package com.vaadin.toolkit.common;

/**
 * @author Kolisek
 */
public abstract class BindingFieldProvider<T> extends FieldProperty<T> implements BindingProvider<T>
{

	public BindingFieldProvider(FieldProperty<T> fp)
	{
		super(true, true, null, fp.property);
		this.val = fp.val;
	}

	public BindingFieldProvider(boolean visible, boolean enabled, String caption, String property)
	{
		super(visible, enabled, caption, property);
	}
}
