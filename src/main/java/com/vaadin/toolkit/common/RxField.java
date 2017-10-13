package com.vaadin.toolkit.common;

import rx.Observable;

/**
 * @author Kolisek
 */
public interface RxField<T> extends RxComponent
{
	void setValue(T value);

	T getValueReal();

	Observable<T> getValue();

	String getProperty();

}
