package com.vaadin.toolkit.field;

import com.vaadin.data.Binder;
import com.vaadin.shared.Registration;
import com.vaadin.toolkit.common.FormRenderer;
import com.vaadin.toolkit.common.TBinder;
import com.vaadin.ui.*;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * @author Kolisek
 */
public class BeanField<T> extends CustomField<T>
{
    private final Supplier<FormRenderer> rendererSupplier;
    private final TBinder<T> binder;
    private T bean;
    protected FormLayout layout = new FormLayout();
    private final Registration renderRegistration;


    public BeanField(@Nonnull Class<T> beanClass, @Nonnull Supplier<FormRenderer> rendererSupplier)
    {
        this.rendererSupplier = rendererSupplier;
        this.binder = new TBinder<>(beanClass);
        this.renderRegistration = this.addValueChangeListener(l -> render());
    }

    protected void render()
    {
        layout.removeAllComponents();

        FormRenderer<T> formRenderer = rendererSupplier.get();

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
