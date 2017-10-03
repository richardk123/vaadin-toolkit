package com.vaadin.toolkit.common;

import com.vaadin.ui.Component;

/**
 * @author Kolisek
 */
public interface FormRenderer<T>
{

	Component render(T bean);
}
