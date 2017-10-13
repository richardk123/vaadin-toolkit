package com.vaadin.toolkit.form;

import java.util.function.Consumer;

import com.vaadin.toolkit.common.BindingProvider;
import com.vaadin.toolkit.common.ComponentProperty;

/**
 * @author Kolisek
 */
public class FormHandler<T>
{
	private BindingProvider<T> bean;

	private Consumer<T> save;
	private Runnable cancel;

	private final ComponentProperty saveBtnState = new ComponentProperty(true, true, "Save");
	private final ComponentProperty cancelBtnState = new ComponentProperty(true, true, "Save");

	protected void cancelBean()
	{
		if (cancel != null)
		{
			cancel.run();
		}
	}

	protected void setBindingProvider(final BindingProvider<T> bean)
	{
		this.bean = bean;
	}

	protected void saveBean(T bean)
	{
		if (save != null)
		{
			save.accept(bean);
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

	public BindingProvider<T> getBean()
	{
		return bean;
	}

	protected ComponentProperty getSaveBtnState()
	{
		return saveBtnState;
	}

	protected ComponentProperty getCancelBtnState()
	{
		return cancelBtnState;
	}
}
