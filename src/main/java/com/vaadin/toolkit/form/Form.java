package com.vaadin.toolkit.form;

import com.vaadin.toolkit.common.BindUtils;
import com.vaadin.toolkit.common.FormRenderer;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author Kolisek
 */
public class Form<T> extends CustomComponent
{

	private final FormHandler<T> handler;
	private FormRenderer<T> formRenderer;
	private final BindUtils bindUtils = new BindUtils();

	public Form(final FormHandler<T> handler)
	{
		this.handler = handler;
	}

	@Override
	public void attach()
	{
		super.attach();
		bindUtils.register(this, handler.getBean(), this::createLayout);
		bindUtils.register(this, handler.getBinder(), this::binderChange);

		createLayout(handler.getBean().getValue());
	}

	public Form<T> withFormRenderer(FormRenderer<T> formRenderer)
	{
		this.formRenderer = formRenderer;
		return this;
	}

	private void binderChange(Binder<T> binder)
	{
		if (formRenderer != null)
		{
			binder.bindInstanceFields(formRenderer);
			try
			{
				binder.writeBean(handler.getBean().getValue());
			}
			catch (ValidationException e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	public void setBean(T bean)
	{
		handler.getBean().setValue(bean);
	}

	protected void createLayout(T bean)
	{
		// global form layout
		final VerticalLayout layout = new VerticalLayout();
		layout.addStyleName("form-view");
		layout.setHeight(100, Unit.PERCENTAGE);
		layout.setSpacing(true);

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
		Button button = new Button(handler.getSaveButton().getCaption().getValue());
		button.setEnabled(handler.getSaveButton().getEnabled().getValue());
		button.setVisible(handler.getSaveButton().getVisible().getValue());
		button.addClickListener(e -> handler.saveBean());
		layout.addComponent(button);
		bindUtils.register(button, handler.getSaveButton());
	}

	protected void addCancelButton(HorizontalLayout layout)
	{
		Button button = new Button(handler.getCancelButton().getCaption().getValue());
		button.setEnabled(handler.getCancelButton().getEnabled().getValue());
		button.setVisible(handler.getCancelButton().getVisible().getValue());
		button.addClickListener(e -> handler.cancelBean());
		layout.addComponent(button);
		bindUtils.register(button, handler.getCancelButton());
	}

}