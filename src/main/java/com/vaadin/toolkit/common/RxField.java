package com.vaadin.toolkit.common;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * @author Kolisek
 */
public class RxField<T> extends RxComponent
{
	protected T val;
	protected final String property;
	protected final PublishSubject<T> value;

	public RxField(String property)
	{
		super(true, true, null);
		this.property = property;
		this.value = PublishSubject.create();
	}

	public RxField(boolean visible, boolean enabled, String caption, String property)
	{
		super(visible, enabled, caption);
		this.property = property;
		this.value = PublishSubject.create();
	}

	public void setValue(T value)
	{
		this.val = value;
		this.value.onNext(value);
	}

	public T getValue()
	{
		return this.val;
	}

	public Observable<T> getObservable()
	{
		return value;
	}

	public String getProperty()
	{
		return property;
	}
}
