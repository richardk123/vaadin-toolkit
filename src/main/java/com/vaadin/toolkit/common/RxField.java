package com.vaadin.toolkit.common;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * @author Kolisek
 */
public class RxField<T> extends RxComponent
{
	protected T val;
	protected final PublishSubject<T> value;

	public RxField()
	{
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

}
