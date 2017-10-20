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
public abstract class Form<T> extends CustomComponent implements BeanRenderer<T>
{
	private final FormHandler<T> handler;
	private RxBinder<T> binder;
	private final Class<T> beanClass;

	public Form(final FormHandler<T> handler, Class<T> beanClass)
	{
		this.handler = handler;

		this.beanClass = beanClass;
	}

	private void createBinder(T bean)
	{
		this.binder = new RxBinder<>(beanClass);
		binder.bindInstanceFields(this, handler);
		binder.setBean(bean);
	}

	public void setBean(T bean)
	{
		renderForm(bean);
		createBinder(bean);
	}

	protected void renderForm(T bean)
	{
		Label formTitle = new Label();

		final VerticalLayout layout = new VerticalLayout();
		layout.setHeight(100, Unit.PERCENTAGE);
		layout.setSpacing(true);

		layout.addComponentsAndExpand(formTitle);
		BindUtils.subscribe(formTitle, handler.getTitleState());

		if (bean != null)
		{
			// make content scrollable
			VerticalLayout innerPanelLayout = new VerticalLayout();
			innerPanelLayout.setSpacing(true);
			innerPanelLayout.setWidth(100, Unit.PERCENTAGE);
			innerPanelLayout.setHeightUndefined();

			Component form = render(bean);
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

	public FormHandler<T> getHandler()
	{
		return handler;
	}
}
