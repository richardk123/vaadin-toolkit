package com.vaadin.toolkit.common;

import com.vaadin.ui.Component;

import javax.annotation.Nonnull;

/**
 * @author Kolisek
 */
public interface FormRenderer<T>
{

	Component render(@Nonnull T bean);
}
