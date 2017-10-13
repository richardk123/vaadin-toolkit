package com.vaadin.toolkit.common;


import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * @author Kolisek
 */
public class ComponentProperty implements RxComponent
{
	private final BehaviorSubject<Boolean> visible;
	private final BehaviorSubject<Boolean> enabled;
	private final BehaviorSubject<String> caption;

	public ComponentProperty(boolean visible, boolean enabled, String caption)
	{
		this.visible = BehaviorSubject.create(visible);
		this.enabled = BehaviorSubject.create(enabled);
		this.caption = BehaviorSubject.create(caption);
	}

	@Override
	public void setVisible(boolean visible)
	{
		this.visible.onNext(visible);
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		this.enabled.onNext(enabled);
	}

	@Override
	public void setCaption(String caption)
	{
		this.caption.onNext(caption);
	}

	@Override
	public Observable<Boolean> getVisible()
	{
		return visible;
	}

	@Override
	public Observable<Boolean> getEnabled()
	{
		return enabled;
	}

	@Override
	public Observable<String> getCaption()
	{
		return caption;
	}
}
