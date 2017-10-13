package com.vaadin.toolkit.field;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

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
    private final Class<T> beanClass;
    private RxBinder<T> binder;
    private T bean;
    protected FormLayout layout = new FormLayout();

    public BeanField(@Nonnull Class<T> beanClass, @Nonnull Supplier<BeanRenderer<T>> rendererSupplier)
    {
        this.beanClass = beanClass;
        this.rendererSupplier = rendererSupplier;
    }

    protected void render(T bean)
    {
        layout.removeAllComponents();

        BeanRenderer<T> formRenderer = rendererSupplier.get();

        if (bean != null && formRenderer != null)
        {
            layout.addComponent(formRenderer.render(bean));
            binder.bindInstanceFields(formRenderer);
            binder.setBean(bean);
        }
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
        this.binder = new RxBinder<>(beanClass);
        render(value);
    }

    @Override
    public T getValue()
    {
        return this.bean;
    }

    public RxBinder<T> getBinder()
    {
        return binder;
    }
}
