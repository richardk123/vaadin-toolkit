package com.vaadin.toolkit;

import com.vaadin.toolkit.common.RxBean;
import com.vaadin.toolkit.common.RxCollection;
import com.vaadin.toolkit.common.RxField;
import com.vaadin.toolkit.form.FormHandler;
import rx.Observable;

/**
 * @author Kolisek
 */
public class OrganizationFormHandler extends FormHandler<Organization, OrganizationFormHandler.OrganizationRxBean>
{

	public OrganizationFormHandler()
	{
		withRxBean(new OrganizationRxBean());

		// title of form
		getRxBean().name.getObservable().subscribe(
				val -> getTitleState().setCaption("Title for form: " + val));

		// username for owner
		fillUserName(getRxBean().owner);

		// userName for users
		getRxBean().users.getAddedSubject().subscribe(rx ->
		{
			fillUserName((UserRxBean) rx);
		});

		withSave(bean -> System.out.println(bean.getName()));
	}

	private void fillUserName(UserRxBean userRxBean)
	{
		Observable.combineLatest(
				userRxBean.firstName.getObservable(),
				userRxBean.lastName.getObservable(),
				(s1, s2) -> s1 + "." + s2)
				.subscribe(userRxBean.userName::setValue);
	}

	public class OrganizationRxBean extends RxBean<Organization>
	{
		private RxField<String> name = new RxField<>();
		private UserRxBean owner = new UserRxBean();
		private RxCollection<User> users = new RxCollection<>(UserRxBean::new);
	}

	public class UserRxBean extends RxBean<User>
	{
		private RxField<String> firstName = new RxField<>();
		private RxField<String> lastName = new RxField<>();
		private RxField<String> userName = new RxField<>();
	}
}
