package com.vaadin.toolkit.field;

import com.vaadin.shared.Registration;
import com.vaadin.toolkit.common.FormRenderer;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.VerticalLayout;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author Kolisek
 */
public class BeanCollectionField<T> extends CustomField<Collection<T>>
{

    private final Class<T> beanClass;
    private final Supplier<FormRenderer> rendererSupplier;

    private Collection<T> collection;
    protected VerticalLayout layout = new VerticalLayout();
    private final Registration changeRegistration;

    public BeanCollectionField(@Nonnull Class<T> beanClass, @Nonnull Supplier<FormRenderer> rendererSupplier)
    {
        this.beanClass = beanClass;
        this.rendererSupplier = rendererSupplier;

        changeRegistration = this.addValueChangeListener(l -> render());
    }

    @Override
    public void detach()
    {
        super.detach();
        changeRegistration.remove();
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
    }

    @Override
    public Collection<T> getValue()
    {
        //TODO; commit bean fields
        return collection;
    }
}
