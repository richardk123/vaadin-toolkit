package com.vaadin.toolkit.form;

import java.util.function.Consumer;

import com.vaadin.toolkit.common.Property;
import com.vaadin.toolkit.common.UIProperty;
import com.vaadin.data.Binder;

/**
 * @author Kolisek
 */
public class FormHandler<T>
{
	private final Class<T> beanType;
	private final Property<T> bean = new Property<>();
	private final Property<Binder<T>> binder = new Property<>();

	private final UIProperty saveButton = new UIProperty(true, true, "Save");
	private final UIProperty cancelButton = new UIProperty(true, true, "Cancel");

	private Consumer<T> save;
	private Runnable cancel;

	public FormHandler(Class<T> beanType)
	{
		this.beanType = beanType;
		this.bean.addValueChangeListener(this::createBinder);
	}

	private Binder<T> createBinder(T bean)
	{
		return new Binder<T>(beanType);
	}

	protected Property<T> getBean()
	{
		return bean;
	}

	protected Property<Binder<T>> getBinder()
	{
		return binder;
	}

	protected void cancelBean()
	{
		if (cancel != null)
		{
			cancel.run();
		}
	}

	protected void saveBean()
	{
		if (save != null)
		{
			save.accept(bean.getValue());
		}
	}

	public FormHandler<T> withSave(Consumer<T> save)
	{
		this.save = save;
		return this;
	}

	public FormHandler<T> withCancel(Runnable cancel)
	{
		this.cancel = cancel;
		return this;
	}

	public UIProperty getSaveButton()
	{
		return saveButton;
	}

	public UIProperty getCancelButton()
	{
		return cancelButton;
	}
}
