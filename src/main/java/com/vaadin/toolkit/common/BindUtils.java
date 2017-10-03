package com.vaadin.toolkit.common;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.vaadin.ui.Component;

/**
 * @author Kolisek
 */
public class BindUtils
{
	private Map<Property, Consumer> register = new HashMap<>();

	public <T> void register(Component component,
							 Property<T> property,
							 Consumer<T> listener)
	{
		register.put(property, listener);

		property.addValueChangeListener(listener);

		component.addDetachListener(l ->
		{
			unregisterAll();
		});
	}

	private void unregisterAll()
	{
		register.forEach((property, listener) ->
		{
			property.removeValueChangeListener(listener);
		});

		register.clear();
	}

	public void register(Component component,
						 UIProperty uiProperty)
	{
		final Consumer<String> captionListener = component::setCaption;
		final Consumer<Boolean> enabledListener = component::setEnabled;
		final Consumer<Boolean> visibleListener = component::setVisible;

		uiProperty.getCaption().addValueChangeListener(captionListener);
		uiProperty.getEnabled().addValueChangeListener(enabledListener);
		uiProperty.getVisible().addValueChangeListener(visibleListener);

		component.addDetachListener(e ->
		{
			uiProperty.getCaption().removeValueChangeListener(captionListener);
			uiProperty.getEnabled().removeValueChangeListener(enabledListener);
			uiProperty.getVisible().removeValueChangeListener(visibleListener);
		});

	}
}
