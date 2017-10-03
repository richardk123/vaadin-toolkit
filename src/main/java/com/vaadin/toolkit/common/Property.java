package com.vaadin.toolkit.common;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Kolisek
 */
public class Property<T>
{

	private T value;
	private List<Consumer<T>> valueChangeListeners = new ArrayList<>();

	public Property()
	{

	}

	public Property(T value)
	{
		this.value = value;
	}

	public T getValue()
	{
		return value;
	}

	public void setValue(T value)
	{
		this.value = value;
		valueChangeListeners.forEach(e -> e.accept(value));
	}

	public void addValueChangeListener(Consumer<T> listener)
	{
		this.valueChangeListeners.add(listener);
	}

	public void removeValueChangeListener(Consumer<T> listener)
	{
		this.valueChangeListeners.remove(listener);
	}


}
