package com.vaadin.toolkit.field;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.vaadin.toolkit.common.BeanRenderer;
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
        return layout;
    }

    protected void render()
    {
        layout.removeAllComponents();

        collection.forEach(bean ->
        {
            HorizontalLayout horizontalLayout = new HorizontalLayout();

            BeanField<T> beanField = new BeanField<>(beanClass, rendererSupplier);
            beanField.setValue(bean);

            horizontalLayout.addComponentsAndExpand(beanField);

            Button removeButton = new Button("Remove");
            removeButton.addClickListener(e -> removeHandler.accept());

            layout.addComponent(horizontalLayout);
        });

        Button addButton = new Button("Add");
        addButton.addClickListener(e -> addHandler.get());
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
}
