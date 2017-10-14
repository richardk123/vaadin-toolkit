package com.vaadin.toolkit.common;

import javax.annotation.Nonnull;
import java.util.List;

import rx.Observable;

/**
 * @author Kolisek
 */
public interface BindingProvider<T> extends RxField<T>
{

	<X> BindingProvider<X> getBindingProvider(@Nonnull String property);

	<X> Observable<X> getObservable(@Nonnull String property);

	void setValue(@Nonnull String property, T value);

	<X> X getValue(@Nonnull String property);

	@Nonnull
	List<BindingProvider> getAll();
}
