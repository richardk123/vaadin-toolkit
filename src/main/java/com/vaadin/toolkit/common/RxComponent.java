package com.vaadin.toolkit.common;


import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * @author Kolisek
 */
public class RxComponent
{
	private final BehaviorSubject<Boolean> visible;
	private final BehaviorSubject<Boolean> enabled;
	private final BehaviorSubject<String> caption;

	public RxComponent()
	{
		this.visible = BehaviorSubject.create(true);
		this.enabled = BehaviorSubject.create(true);
		this.caption = BehaviorSubject.create((String) null);
	}

	public RxComponent(boolean visible, boolean enabled, String caption)
	{
		this.visible = BehaviorSubject.create(visible);
		this.enabled = BehaviorSubject.create(enabled);
		this.caption = BehaviorSubject.create(caption);
	}

	public void setVisible(boolean visible)
	{
		this.visible.onNext(visible);
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled.onNext(enabled);
	}

	public void setCaption(String caption)
	{
		this.caption.onNext(caption);
	}

	public Observable<Boolean> getVisible()
	{
		return visible;
	}

	public Observable<Boolean> getEnabled()
	{
		return enabled;
	}

	public Observable<String> getCaption()
	{
		return caption;
	}
}
