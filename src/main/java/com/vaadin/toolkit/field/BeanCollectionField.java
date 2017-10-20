package com.vaadin.toolkit.field;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.function.Supplier;

import com.vaadin.toolkit.common.BeanRenderer;
import com.vaadin.toolkit.form.FormHandler;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Kolisek
 */
public class BeanCollectionField<T> extends CustomField<Collection<T>>
{

    private final Supplier<BeanRenderer<T>> rendererSupplier;
    private final FormHandler formHandler;

    private Collection<T> collection;
    protected VerticalLayout layout = new VerticalLayout();
    private final Class<T> beanClass;

    public BeanCollectionField(Class<T> beanClass,
                               @Nonnull FormHandler formHandler,
                               @Nonnull Supplier<BeanRenderer<T>> rendererSupplier)
    {
        this.formHandler = formHandler;
        this.rendererSupplier = rendererSupplier;
        this.beanClass = beanClass;

    }

    @Override
    protected Component initContent()
    {
        return layout;
    }

    protected void render()
    {
        layout.removeAllComponents();
        collection.forEach(bean ->
        {
            BeanField<T> beanField = new BeanField<>(beanClass, rendererSupplier);
            beanField.setValue(bean);

            layout.addComponent(beanField);
        });
    }

    @Override
    protected void doSetValue(Collection<T> value)
    {
        this.collection = value;
        render();
    }

    @Override
    public Collection<T> getValue()
    {
        //TODO; commit bean fields
        return collection;
    }
}
