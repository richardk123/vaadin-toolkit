package com.vaadin.toolkit.common;

import javax.annotation.Nonnull;

import com.vaadin.annotations.PropertyId;
import com.vaadin.toolkit.field.BeanField;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

/**
 * @author Kolisek
 */
public class TestDataRenderer implements BeanRenderer<TestData>
{

	@PropertyId("name")
	private TextField nameField;

	@PropertyId("test")
	private BeanField<TestData> testBeanField;

	@Override
	public Component render(@Nonnull TestData bean)
	{
		this.nameField = new TextField();
		this.testBeanField = new BeanField<>(TestData.class, TestDataRenderer::new);
		return new FormLayout(nameField, testBeanField);
	}

}
