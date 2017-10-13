package com.vaadin.toolkit.form;

import com.vaadin.toolkit.common.BeanRenderer;
import com.vaadin.toolkit.common.BindUtils;
import com.vaadin.toolkit.common.RxBinder;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author Kolisek
 */
public class Form<T> extends CustomComponent
{
	private final FormHandler<T> handler;
	private RxBinder<T> binder;
	private BeanRenderer<T> formRenderer;
	private final Class<T> beanClass;

	public Form(final FormHandler<T> handler, Class<T> beanClass)
	{
		this.handler = handler;

		this.beanClass = beanClass;
	}

	public Form<T> withFormRenderer(BeanRenderer<T> formRenderer)
	{
		this.formRenderer = formRenderer;
		return this;
	}

	private void createBinder(T bean)
	{
		this.binder = new RxBinder<>(beanClass);

		if (formRenderer != null)
		{
			binder.bindInstanceFields(formRenderer);
			binder.setBean(bean);
		}
	}

	public void setBean(T bean)
	{
		render(bean);
		createBinder(bean);

		handler.setBindingProvider(binder.get());

		BindUtils.subscribe(this, handler.getBean().getObservable(), b ->
		{
			render(b);
			createBinder(b);
		});
	}

	protected void render(T bean)
	{
		Label formTitle = new Label();

		final VerticalLayout layout = new VerticalLayout();
		layout.setHeight(100, Unit.PERCENTAGE);
		layout.setSpacing(true);

		layout.addComponentsAndExpand(formTitle);
		BindUtils.subscribe(formTitle, handler.getTitleState());

		if (bean != null && formRenderer != null)
		{
			// make content scrollable
			VerticalLayout innerPanelLayout = new VerticalLayout();
			innerPanelLayout.setSpacing(true);
			innerPanelLayout.setWidth(100, Unit.PERCENTAGE);
			innerPanelLayout.setHeightUndefined();

			Component form = formRenderer.render(bean);
			innerPanelLayout.addComponent(form);

			// wrap content of form by panel
			Panel panel = new Panel(innerPanelLayout);
			panel.addStyleName(ValoTheme.PANEL_BORDERLESS);
			panel.setSizeFull();

			layout.addComponent(panel);
			layout.setExpandRatio(panel, 1.0f);
		}

		createButtons(layout);
		setCompositionRoot(layout);
	}

	protected void createButtons(VerticalLayout layout)
	{
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		addSaveButton(horizontalLayout);
		addCancelButton(horizontalLayout);

		layout.addComponent(horizontalLayout);
	}

	protected void addSaveButton(HorizontalLayout layout)
	{
		Button button = new Button();
		BindUtils.subscribe(button, handler.getSaveBtnState());
		button.addClickListener(e -> handler.saveBean(binder.getBean()));
		layout.addComponent(button);
	}

	protected void addCancelButton(HorizontalLayout layout)
	{
		Button button = new Button();
		BindUtils.subscribe(button, handler.getCancelBtnState());
		button.addClickListener(e -> handler.cancelBean());
		layout.addComponent(button);
	}

	public RxBinder<T> getBinder()
	{
		return binder;
	}
}
