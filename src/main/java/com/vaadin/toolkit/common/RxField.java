package com.vaadin.toolkit.common;

import rx.Observable;

/**
 * @author Kolisek
 */
public interface RxField<T> extends RxComponent
{
	void setValue(T value);

	<X extends T> T getValue();

	<X extends T> Observable<X> getObservable();

	String getProperty();

}
