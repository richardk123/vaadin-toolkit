package com.vaadin.toolkit.field;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

import com.vaadin.shared.Registration;
import com.vaadin.toolkit.common.BeanRenderer;
import com.vaadin.toolkit.common.RxBinder;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.FormLayout;

/**
 * @author Kolisek
 */
public class BeanField<T> extends CustomField<T>
{
    private final Supplier<BeanRenderer<T>> rendererSupplier;
    private final RxBinder<T> binder;
    private T bean;
    protected FormLayout layout = new FormLayout();
    private final Registration renderRegistration;


    public BeanField(@Nonnull Class<T> beanClass, @Nonnull Supplier<BeanRenderer<T>> rendererSupplier)
    {
        this.rendererSupplier = rendererSupplier;
        this.binder = new RxBinder<>(beanClass);
        this.renderRegistration = this.addValueChangeListener(l -> render());
    }

    protected void render()
    {
        layout.removeAllComponents();

        BeanRenderer<T> formRenderer = rendererSupplier.get();

        if (bean != null && formRenderer != null)
        {
            layout.addComponent(formRenderer.render(bean));

            binder.bindInstanceFields(formRenderer);
            binder.readBean(bean);
        }
    }

    @Override
    public void detach()
    {
        super.detach();
        renderRegistration.remove();
    }

    @Override
    protected Component initContent()
    {
        return layout;
    }

    @Override
    protected void doSetValue(T value)
    {
        this.bean = value;
    }

    @Override
    public T getValue()
    {
        if (bean != null)
        {
            binder.writeBeanIfValid(bean);
        }
        return this.bean;
    }
}
