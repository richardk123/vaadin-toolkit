package com.vaadin.toolkit.common;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.vaadin.annotations.PropertyId;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.toolkit.field.BeanField;
import com.vaadin.util.ReflectTools;
import org.apache.commons.beanutils.PropertyUtils;
import rx.functions.Action1;

public class RxBinder<T> extends Binder<T>
{

    private List<BindingProvider> bindingProviders = new ArrayList<>();
    private BindingFieldProvider<T> bindingFieldProvider = new BindingFieldProvider<T>(true, true, null, null)
    {
        @Override
        public BindingProvider<?> get(@Nonnull String property)
        {
            return getBindingProviderForProperty(property);
        }
    };

    public RxBinder(Class<T> beanType)
    {
        super(beanType);
    }

    @Override
    public void bindInstanceFields(Object objectWithMemberFields)
    {
        this.bindingProviders = new ArrayList<>();

        Class<?> objectClass = objectWithMemberFields.getClass();

        getFieldsInDeclareOrder(objectClass)
                .stream()
                .filter(memberField -> HasValue.class.isAssignableFrom(memberField.getType()))
                .forEach(field ->
                {
                    final HasValue fieldInstance = getFieldValue(field, objectWithMemberFields);
                    //todo: what if there is not propertyId present
                    final String property = field.getAnnotationsByType(PropertyId.class)[0].value();
                    final BindingProvider bindingProvider = createBindingProvider(property, fieldInstance);

                    bindingProviders.add(bindingProvider);

                    bindingProvider.getValue().subscribe((Action1) o ->
                    {
                        Object value = fieldInstance.getValue();
                        if (!Objects.equals(value, o))
                        {
                            fieldInstance.setValue(o);
                        }
                    });

                    bind(fieldInstance,
                            // getter
                            (bean) ->
                            {
                                return getVal(bean, property);
                            },
                            // setter
                            (bean, fieldValue) ->
                            {
                                setVal(bean, property, fieldValue);

                                if (!Objects.equals(fieldValue, bindingProvider.getValueReal()))
                                {
                                    bindingProvider.setValue(fieldValue);
                                }
                            });
                });
    }

    private BindingProvider createBindingProvider(String property, HasValue fieldInstance)
    {
        return new BindingFieldProvider(true, true, "", property)
        {
            @Override
            public BindingProvider get(@Nonnull String property)
            {
                if (fieldInstance instanceof BeanField)
                {
                    BeanField bf = (BeanField) fieldInstance;
                    return bf.getBinder().getBindingProviderForProperty(property);
                }

                throw new RuntimeException(String.format("unsuported for property: %s", property));
            }
        };
    }

    @Override
    public void setBean(final T bean)
    {
        // TODO: on first set of value there should not be an event for all subscribers
        bindingProviders.forEach(bp ->
        {
            String property = bp.getProperty();
            Object value = getVal(bean, property);
            bp.setValue(value);
        });

        super.setBean(bean);
    }

    private Object getVal(Object bean, String property)
    {
        try
        {
            return PropertyUtils.getProperty(bean, property);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private void setVal(Object bean, String property, Object value)
    {
        try
        {
            PropertyUtils.setProperty(bean, property, value);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private HasValue getFieldValue(Field field, Object objectWithMemberFields)
    {
        try {
            return (HasValue<?>) ReflectTools.getJavaFieldValue(
                    objectWithMemberFields, field, HasValue.class);
        } catch (IllegalArgumentException | IllegalAccessException
                | InvocationTargetException e) {
            return null;
        }
    }

    private List<Field> getFieldsInDeclareOrder(Class<?> searchClass) {
        ArrayList<Field> memberFieldInOrder = new ArrayList<>();

        while (searchClass != null) {
            memberFieldInOrder.addAll(Arrays.asList(searchClass.getDeclaredFields()));
            searchClass = searchClass.getSuperclass();
        }
        return memberFieldInOrder;
    }

    // for this
    public BindingProvider<T> get()
    {
        return bindingFieldProvider;
    }

    public BindingProvider getBindingProviderForProperty(String property)
    {
        return bindingProviders.stream()
                .filter(bindingProvider -> property.equals(bindingProvider.getProperty()))
                .findFirst().orElseThrow(() -> new RuntimeException(String.format("property %s not found", property)));
    }
}
