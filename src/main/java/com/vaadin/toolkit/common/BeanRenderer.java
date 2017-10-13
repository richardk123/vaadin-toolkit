package com.vaadin.toolkit.common;

import javax.annotation.Nonnull;

import com.vaadin.ui.Component;

/**
 * @author Kolisek
 */
public interface BeanRenderer<T>
{

	Component render(@Nonnull T bean);
}
