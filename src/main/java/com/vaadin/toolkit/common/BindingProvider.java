package com.vaadin.toolkit.common;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author Kolisek
 */
public interface BindingProvider<T> extends RxField<T>
{

	<X> BindingProvider<X> get(@Nonnull String property);

	@Nonnull
	List<BindingProvider> getAll();
}
