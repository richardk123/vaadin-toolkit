package com.vaadin.toolkit;

import com.vaadin.toolkit.common.RxBean;
import com.vaadin.toolkit.common.RxCollection;
import com.vaadin.toolkit.common.RxField;

/**
 * @author Kolisek
 */
public class OrganizationRxBean extends RxBean<Organization>
{
	private MyUI components;
	public RxField<String> name = new RxField<>();
	public UserRxBean owner = new UserRxBean();
	public RxCollection<User> users = new RxCollection<>(UserRxBean::new);

	public OrganizationRxBean(MyUI components)
	{
		this.components = components;
	}
}
