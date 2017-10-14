package com.vaadin.toolkit.common;

import javax.annotation.Nonnull;

import rx.Observable;

/**
 * @author Kolisek
 */
public abstract class BindingFieldProvider<T> extends FieldProperty<T> implements BindingProvider<T>
{

	public BindingFieldProvider(boolean visible, boolean enabled, String caption, String property)
	{
		super(visible, enabled, caption, property);
	}

	@Override
	public <X> Observable<X> getObservable(@Nonnull String property)
	{
		return getBindingProvider(property).getObservable();
	}

	@Override
	public <X> X getValue(@Nonnull String property)
	{
		return (X) getBindingProvider(property).getValue();
	}

	@Override
	public void setValue(@Nonnull String property, T value)
	{
		getBindingProvider(property).setValue(value);
	}

	@Override
	public abstract <X> BindingProvider<X> getBindingProvider(@Nonnull String property);
}
