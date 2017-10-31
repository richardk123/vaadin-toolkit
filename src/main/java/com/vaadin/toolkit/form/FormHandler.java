package com.vaadin.toolkit.form;

import java.util.function.Consumer;

import com.vaadin.toolkit.common.RxBean;
import com.vaadin.toolkit.common.RxComponent;
import com.vaadin.toolkit.common.RxField;

/**
 * @author Kolisek
 */
public class FormHandler<T, V extends RxBean<T>>
{
	private Consumer<T> save;
	private Runnable cancel;

	private final RxField<String> titleState = new RxField<>();
	private final RxComponent saveBtnState = new RxComponent();
	private final RxComponent cancelBtnState = new RxComponent();

	private V rxBean;

	protected void cancelBean()
	{
		if (cancel != null)
		{
			cancel.run();
		}
	}

	protected void saveBean(T bean)
	{
		if (save != null)
		{
			save.accept(bean);
		}
	}

	public FormHandler<T, V> withRxBean(V rxBean)
	{
		this.rxBean = rxBean;
		return this;
	}

	public FormHandler<T, V> withSave(Consumer<T> save)
	{
		this.save = save;
		return this;
	}

	public FormHandler<T, V> withCancel(Runnable cancel)
	{
		this.cancel = cancel;
		return this;
	}

	protected RxComponent getSaveBtnState()
	{
		return saveBtnState;
	}

	protected RxComponent getCancelBtnState()
	{
		return cancelBtnState;
	}

	public RxField<String> getTitleState()
	{
		return titleState;
	}

	public V getRxBean()
	{
		return rxBean;
	}
}
