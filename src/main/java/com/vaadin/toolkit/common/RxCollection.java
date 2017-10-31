package com.vaadin.toolkit.common;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import rx.subjects.PublishSubject;

/**
 * @author Kolisek
 */
public class RxCollection<T> extends RxField<Collection<T>>
{

	private List<RxBean<T>> rxBeanList = new ArrayList<>();
	private final PublishSubject<RxBean<T>> addedSubject;
	private final PublishSubject<RxBean<T>> removedSubject;
	private final Supplier<RxBean<T>> rxBeanSupplier;

	public RxCollection(Supplier<RxBean<T>> rxBeanSupplier)
	{
		this.rxBeanSupplier = rxBeanSupplier;
		this.addedSubject = PublishSubject.create();
		this.removedSubject = PublishSubject.create();

		value.subscribe(
				collection ->
				{
					collection.forEach(
							bean ->
							{
								RxBean<T> rxBean = rxBeanSupplier.get();
								addBean(rxBean);
							});
				}
		);
	}

	public void addBean(T bean)
	{
		RxBean<T> rxBean = rxBeanSupplier.get();
		rxBean.setValue(bean);
		addBean(rxBean);
	}

	public void addBean(RxBean<T> rxBean)
	{
		rxBeanList.add(rxBean);
		addedSubject.onNext(rxBean);
	}

	public void removeBean(RxBean<T> rxBean)
	{
		rxBeanList.remove(rxBean);
		removedSubject.onNext(rxBean);
	}

	@Nullable
	public RxBean<T> getRxBean(@Nonnull T bean)
	{
		return rxBeanList.stream()
				.filter(rxBean -> bean.equals(rxBean.getValue()))
				.findFirst()
				.orElse(null);
	}

	public PublishSubject<RxBean<T>> getAddedSubject()
	{
		return addedSubject;
	}

	public PublishSubject<RxBean<T>> getRemovedSubject()
	{
		return removedSubject;
	}
}
