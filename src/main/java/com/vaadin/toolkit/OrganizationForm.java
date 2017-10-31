package com.vaadin.toolkit;

import javax.annotation.Nonnull;

import com.vaadin.annotations.PropertyId;
import com.vaadin.toolkit.common.RxBean;
import com.vaadin.toolkit.field.BeanCollectionField;
import com.vaadin.toolkit.field.BeanField;
import com.vaadin.toolkit.form.Form;
import com.vaadin.toolkit.form.FormHandler;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

/**
 * @author Kolisek
 */
class OrganizationForm extends Form<Organization>
{

	@PropertyId("name")
	private TextField nameField;

	@PropertyId("owner")
	private BeanField<User> ownerField;

	@PropertyId("users")
	private BeanCollectionField<User> usersCollection;

	public OrganizationForm(FormHandler<Organization, ? extends RxBean<Organization>> handler)
	{
		super(handler);
	}

	@Override
	public Component render(@Nonnull Organization bean)
	{
		this.nameField = new TextField("name");
		this.ownerField = new BeanField<>(User.class, UserRenderer::new);
		ownerField.setCaption("owner");

		this.usersCollection = new BeanCollectionField<>(User.class, UserRenderer::new)
				.withAddHandler(() -> bean.addUser(new User()))
				.withRemoveHandler(bean::removeUser);

		usersCollection.setCaption("users");

		return new FormLayout(nameField, ownerField, usersCollection);
	}
}
