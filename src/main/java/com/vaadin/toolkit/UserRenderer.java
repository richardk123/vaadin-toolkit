package com.vaadin.toolkit;

import javax.annotation.Nonnull;

import com.vaadin.annotations.PropertyId;
import com.vaadin.toolkit.common.BeanRenderer;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

/**
 * @author Kolisek
 */
class UserRenderer implements BeanRenderer<User>
{
	@PropertyId("firstName")
	private TextField firstNameField;

	@PropertyId("lastName")
	private TextField lastNameField;

	@PropertyId("userName")
	private TextField userNameField;

	@Override
	public Component render(@Nonnull User bean)
	{
		this.firstNameField = new TextField("first name");
		this.lastNameField = new TextField("last name");
		this.userNameField = new TextField("username");
		userNameField.setReadOnly(true);

		return new HorizontalLayout(firstNameField, lastNameField, userNameField);
	}
}
