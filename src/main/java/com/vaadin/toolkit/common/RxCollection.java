package com.vaadin.toolkit.common;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Kolisek
 */
public abstract class RxCollection<T> extends RxField<T>
{

	private Supplier<RxBean<T>> rxBeanSupplier;
	private List<RxBean<T>> rxBeanList = new ArrayList<>();

	public RxCollection(String property, Supplier<RxBean<T>> rxBeanSupplier)
	{
		super(property);
		this.rxBeanSupplier = rxBeanSupplier;
	}

	public RxCollection(boolean visible, boolean enabled, String caption, String property)
	{
		super(visible, enabled, caption, property);
	}
}
