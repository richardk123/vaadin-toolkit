package com.vaadin.toolkit;

import com.vaadin.toolkit.common.RxBean;
import com.vaadin.toolkit.common.RxField;

/**
 * @author Kolisek
 */
public class UserRxBean extends RxBean<User>
{
	public RxField<String> firstName = new RxField<>();
	public RxField<String> lastName = new RxField<>();
	public RxField<String> userName = new RxField<>();
}
