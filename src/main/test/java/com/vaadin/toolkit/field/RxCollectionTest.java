package com.vaadin.toolkit.field;

import javax.annotation.Nonnull;

import com.vaadin.annotations.PropertyId;
import com.vaadin.toolkit.common.BeanRenderer;
import com.vaadin.toolkit.common.Organization;
import com.vaadin.toolkit.common.RxBean;
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
	public class OrgRxBean extends RxBean<Organization>
	{
		public RxField<String> name = new RxField<>();

		private RxCollection<User> users = new RxCollection<>(UserBean::new);

		public class UserBean extends RxBean<User>
		{

			public RxField<String> firstName = new RxField<>();

			public RxField<String> lastName = new RxField<>();

			public RxField<String> userName = new RxField<>();

		}
	}

	private class OrgForm extends Form<Organization>
	{
		@PropertyId("name")
		private TextField nameField;

		@PropertyId("users")
		private BeanCollectionField<User> usersField;

		public OrgForm(FormHandler<Organization, OrgRxBean> handler)
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
		FormHandler<Organization, OrgRxBean> handler = new FormHandler<>();
		handler.withRxBean(new OrgRxBean());
		OrgForm orgForm = new OrgForm(handler);

		Organization organization = new Organization("test");
		organization.addUser(new User());

		orgForm.setBean(organization);

	}

}
