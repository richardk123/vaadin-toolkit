package com.vaadin.toolkit.field;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.vaadin.toolkit.common.BeanRenderer;
import com.vaadin.toolkit.common.RxCollection;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Kolisek
 */
public class BeanCollectionField<T> extends CustomField<Collection<T>>
{

    private final Supplier<BeanRenderer<T>> rendererSupplier;

    private Collection<T> collection;
    protected VerticalLayout layout = new VerticalLayout();
    private final Class<T> beanClass;

    private RxCollection<T> rxCollection;

    private Supplier<T> addHandler;
    private Consumer<T> removeHandler;

    public BeanCollectionField(@Nonnull Class<T> beanClass,
                               @Nonnull Supplier<BeanRenderer<T>> rendererSupplier)
    {
        this.rendererSupplier = rendererSupplier;
        this.beanClass = beanClass;

    }

    @Override
    protected Component initContent()
    {
        layout.setSpacing(false);
        layout.setMargin(false);
        layout.setSizeUndefined();
        return layout;
    }

    protected void render()
    {
        layout.removeAllComponents();

        Button addButton = new Button("Add");
        addButton.addClickListener(e ->
        {
            T bean = addHandler.get();

            if (rxCollection != null)
            {
                rxCollection.addBean(bean);
            }

            renderBean(bean);
        });

        collection.forEach(this::renderBean);

        layout.addComponent(addButton);
    }

    protected void renderBean(T bean)
    {
        final HorizontalLayout rowLayout = new HorizontalLayout();
        rowLayout.setSpacing(true);
        rowLayout.setMargin(false);
        rowLayout.setSizeUndefined();

        rowLayout.addComponentsAndExpand(createBeanField(bean));

        Button removeButton = new Button("Remove");
        removeButton.addClickListener(e ->
        {
            removeHandler.accept(bean);

            if (rxCollection != null)
            {
                rxCollection.removeBean(rxCollection.getRxBean(bean));
            }

            layout.removeComponent(rowLayout);
        });

        rowLayout.addComponent(removeButton);
        rowLayout.setComponentAlignment(removeButton, Alignment.BOTTOM_CENTER);
        layout.addComponent(rowLayout);
    }

    protected BeanField<T> createBeanField(T bean)
    {
        BeanField<T> beanField = new BeanField<>(beanClass, rendererSupplier);

        if (rxCollection != null)
        {
            beanField.setRxBean(rxCollection.getRxBean(bean));
        }

        beanField.setValue(bean);

        return beanField;
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
        return collection;
    }

    public BeanCollectionField<T> withAddHandler(@Nonnull Supplier<T> addHandler)
    {
        this.addHandler = addHandler;
        return this;
    }

    public BeanCollectionField<T> withRemoveHandler(@Nonnull Consumer<T> removeHandler)
    {
        this.removeHandler = removeHandler;
        return this;
    }

    public void setRxCollection(RxCollection<T> rxCollection)
    {
        this.rxCollection = rxCollection;
    }
}
