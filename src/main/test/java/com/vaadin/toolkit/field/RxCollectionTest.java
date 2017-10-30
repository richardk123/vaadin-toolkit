package com.vaadin.toolkit.field;

import javax.annotation.Nonnull;

import com.vaadin.annotations.PropertyId;
import com.vaadin.toolkit.common.BeanRenderer;
import com.vaadin.toolkit.common.Organization;
import com.vaadin.toolkit.common.RxCollection;
import com.vaadin.toolkit.common.RxField;
import com.vaadin.toolkit.common.User;
import com.vaadin.toolkit.form.Form;
import com.vaadin.toolkit.form.FormHandler;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import org.junit.Test;

/**
 * @author Kolisek
 */
public class RxCollectionTest
{
	public class OrgFormHandler extends FormHandler<Organization>
	{
		@PropertyId("name")
		public RxField<String> name = new RxField<>("name");

		@PropertyId("users")
		private UserRxCollection users = new UserRxCollection("users");

		public class UserRxCollection extends RxCollection<User>
		{
			@PropertyId("firstName")
			public RxField<String> firstName = new RxField<>("firstName");

			@PropertyId("lastName")
			public RxField<String> lastName = new RxField<>("lastName");

			@PropertyId("userName")
			public RxField<String> userName = new RxField<>("userName");

			public UserRxCollection(String property)
			{
				super(property);
			}
		};
	}

	private class OrgForm extends Form<Organization>
	{
		@PropertyId("name")
		private TextField nameField;

		@PropertyId("users")
		private BeanCollectionField<User> usersField;

		public OrgForm(FormHandler<Organization> handler)
		{
			super(handler);
		}

		@Override
		public Component render(@Nonnull Organization bean)
		{
			nameField = new TextField();
			usersField = new BeanCollectionField<>(User.class, UserRenderer::new);
			return new FormLayout(nameField, usersField);
		}
	}

	public class UserRenderer implements BeanRenderer<User>
	{

		@PropertyId("firstName")
		private TextField firstName;

		@PropertyId("lastName")
		private TextField lastName;

		@PropertyId("userName")
		private TextField userName;

		@Override
		public Component render(@Nonnull User bean)
		{
			firstName = new TextField();
			lastName = new TextField();
			userName = new TextField();
			return new FormLayout(firstName, lastName, userName);
		}
	}

	@Test
	public void testCollectionDefaultValues()
	{
		OrgFormHandler handler = new OrgFormHandler();
		OrgForm orgForm = new OrgForm(handler);

		Organization organization = new Organization("test");
		organization.addUser(new User());

		orgForm.setBean(organization);

		handler.users.
	}

}
