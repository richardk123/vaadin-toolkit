package com.vaadin.toolkit.common;

import rx.Observable;

/**
 * @author Kolisek
 */
public interface RxComponent
{
	void setVisible(boolean visible);

	void setEnabled(boolean enabled);

	void setCaption(String caption);

	Observable<Boolean> getVisible();

	Observable<Boolean> getEnabled();

	Observable<String> getCaption();
}
