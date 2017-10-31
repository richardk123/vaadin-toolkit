package com.vaadin.toolkit.common;

import javax.annotation.Nonnull;

import com.vaadin.data.HasValue;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
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





	public static void subscribeLabel(@Nonnull final Label label,
									 	  @Nonnull final RxField<String> rxField)
	{
		subscribe(label, rxField);

		rxField.setValue(label.getValue());

		final Subscription visibleSubscription = rxField.getObservable().subscribe(label::setValue);

		label.addDetachListener(e ->
		{
			visibleSubscription.unsubscribe();
		});
	}
	public static <T> void subscribeField(@Nonnull final HasValue<T> hasValue,
									 	  @Nonnull final RxField<T> rxField)
	{
		Component component = (Component) hasValue;

		subscribe(component, rxField);

		rxField.setValue(hasValue.getValue());

		final Subscription visibleSubscription = rxField.getObservable().subscribe(hasValue::setValue);

		component.addDetachListener(e ->
		{
			visibleSubscription.unsubscribe();
		});
	}

	public static void subscribe(@Nonnull final Component component,
								 @Nonnull final RxComponent uiProperty)
	{

		uiProperty.setCaption(component.getCaption());
		uiProperty.setEnabled(component.isEnabled());
		uiProperty.setVisible(component.isVisible());

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
