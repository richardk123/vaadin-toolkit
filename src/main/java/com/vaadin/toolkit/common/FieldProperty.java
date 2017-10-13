package com.vaadin.toolkit.common;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * @author Kolisek
 */
public class FieldProperty<T> extends ComponentProperty implements RxField<T>
{
	protected T val;
	protected final String property;
	protected final PublishSubject<T> value;

	public FieldProperty(boolean visible, boolean enabled, String caption, String property)
	{
		super(visible, enabled, caption);
		this.property = property;
		this.value = PublishSubject.create();
	}

	@Override
	public void setValue(T value)
	{
		this.val = value;
		this.value.onNext(value);
	}

	@Override
	public T getValue()
	{
		return this.val;
	}

	@Override
	public Observable<T> getObservable()
	{
		return value;
	}

	@Override
	public String getProperty()
	{
		return property;
	}
}
