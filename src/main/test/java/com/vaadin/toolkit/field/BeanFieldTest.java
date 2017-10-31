package com.vaadin.toolkit.field;

import javax.annotation.Nonnull;

import com.vaadin.annotations.PropertyId;
import com.vaadin.data.HasValue;
import com.vaadin.toolkit.common.BeanRenderer;
import com.vaadin.toolkit.common.Organization;
import com.vaadin.toolkit.common.RxBean;
import com.vaadin.toolkit.common.RxField;
import com.vaadin.toolkit.common.User;
import com.vaadin.toolkit.form.Form;
import com.vaadin.toolkit.form.FormHandler;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Kolisek
 */
public class BeanFieldTest
{

	public class OrgRxBean extends RxBean<Organization>
	{
		public RxField<String> name = new RxField<>();

		private UserRxBean owner = new UserRxBean();

		public class UserRxBean extends RxBean<User>
		{
			public RxField<String> firstName = new RxField<>();

			public RxField<String> lastName = new RxField<>();

			public RxField<String> userName = new RxField<>();
		};
	}

	private class OrgForm extends Form<Organization>
	{
		@PropertyId("name")
		private TextField nameField;

		@PropertyId("owner")
		private BeanField<User> ownerField;

		public OrgForm(FormHandler<Organization, OrgRxBean> handler)
		{
			super(handler);
		}

		@Override
		public Component render(@Nonnull Organization bean)
		{
			nameField = new TextField();
			ownerField = new BeanField<>(User.class, UserRenderer::new);
			return new FormLayout(nameField, ownerField);
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

	private Organization createOrganization()
	{
		Organization organization = new Organization("orgName");
		User user = new User();
		user.setFirstName("firstName");
		organization.setOwner(user);

		return organization;
	}

	@Test
	public void testDefaultValueInBeanFieldRxProperty()
	{
		FormHandler<Organization, OrgRxBean> formHandler = new FormHandler<>();
		formHandler.withRxBean(new OrgRxBean());

		OrgForm form = new OrgForm(formHandler);

		form.setBean(createOrganization());

		String rxValue = formHandler.getRxBean().owner.firstName.getValue();
		Assert.assertEquals("firstName", rxValue);
	}

	@Test
	public void testChangeInBeanFieldValuePropagateToRxField()
	{
		FormHandler<Organization, OrgRxBean> formHandler = new FormHandler<>();
		formHandler.withRxBean(new OrgRxBean());
		OrgForm form = new OrgForm(formHandler);

		form.setBean(createOrganization());

		BeanField<?> beanField = (BeanField) form.getBinder().getFields()
				.filter(f -> f instanceof BeanField)
				.findFirst().get();

		beanField.getBinder().getFields()
				.filter(f -> f instanceof TextField)
				.findFirst()
				.ifPresent(f -> ((HasValue) f).setValue("changedValue"));

		String rxValue = formHandler.getRxBean().owner.lastName.getValue();

		Assert.assertEquals("changedValue", rxValue);
	}

}
