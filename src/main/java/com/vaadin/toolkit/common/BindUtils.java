package com.vaadin.toolkit.common;

import javax.annotation.Nonnull;

import com.vaadin.ui.Component;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

/**
 * @author Kolisek
 */
public class BindUtils
{

	public static <T> void subscribe(@Nonnull final Component component,
								 	 @Nonnull final Observable<T> observable,
									 @Nonnull final Action1<T> onNext)
	{
		Subscription subscription = observable.subscribe(onNext);

		component.addDetachListener(e ->
		{
			subscription.unsubscribe();
		});
	}

	public static void subscribe(@Nonnull final Component component,
								 @Nonnull final RxComponent uiProperty)
	{

		final Subscription captionSubscription = uiProperty.getCaption().subscribe(component::setCaption);
		final Subscription enabledSubscription = uiProperty.getEnabled().subscribe(component::setEnabled);
		final Subscription visibleSubscription = uiProperty.getVisible().subscribe(component::setVisible);

		component.addDetachListener(e ->
		{
			captionSubscription.unsubscribe();
			enabledSubscription.unsubscribe();
			visibleSubscription.unsubscribe();
		});

	}
}
