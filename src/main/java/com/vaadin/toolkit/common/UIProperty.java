package com.vaadin.toolkit.common;

/**
 * @author Kolisek
 */
public class UIProperty
{
	private final Property<Boolean> visible;
	private final Property<Boolean> enabled;
	private final Property<String> caption;

	public UIProperty(boolean visible, boolean enabled, String caption)
	{
		this.visible = new Property<>(visible);
		this.enabled = new Property<>(enabled);
		this.caption = new Property<>(caption);
	}

	public Property<Boolean> getVisible()
	{
		return visible;
	}

	public Property<Boolean> getEnabled()
	{
		return enabled;
	}

	public Property<String> getCaption()
	{
		return caption;
	}
}
