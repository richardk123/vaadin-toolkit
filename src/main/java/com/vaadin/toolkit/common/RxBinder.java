package com.vaadin.toolkit.common;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.vaadin.annotations.PropertyId;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.toolkit.field.BeanCollectionField;
import com.vaadin.toolkit.field.BeanField;
import com.vaadin.ui.Component;

public class RxBinder<T> extends Binder<T>
{
    public RxBinder(Class<T> beanType)
    {
        super(beanType);
    }

    private String getFieldName(Field field)
    {
        return field.getAnnotationsByType(PropertyId.class)[0].value();
    }

    public void bindInstanceFields(final @Nonnull Object objectWithMemberFields,
                                   final @Nullable RxBean<T> rxBean)
    {
        Class<?> formClass = objectWithMemberFields.getClass();

        final Map<String, Field> formFieldMap = ReflectionUtils.getFieldsInDeclareOrder(formClass)
                .stream()
                .filter(field -> HasValue.class.isAssignableFrom(field.getType()))
                .filter(field -> field.getAnnotationsByType(PropertyId.class).length > 0)
                .collect(Collectors.toMap(this::getFieldName, Function.identity(), (oldValue, newValue) -> oldValue));

        final Map<String, Field> rxBeanFieldMap = createRxBeanFieldMap(rxBean);

        formFieldMap.forEach(
                (property, formJavaField) ->
                {
                    Field rxBeanJavaField = rxBeanFieldMap.get(property);

                    final HasValue<Object> formField = ReflectionUtils.getFieldValue(formJavaField, objectWithMemberFields, HasValue.class);
                    final RxField<Object> rxField = ReflectionUtils.getFieldValue(rxBeanJavaField, rxBean, RxField.class);

                    bindDefaultComponentState(rxField, formField);
                    subscribeToChangeAndSetToField(rxField, formField);

                    bind(formField,
                            (bean) -> ReflectionUtils.getVal(bean, property),
                            (bean, fieldValue) ->
                            {
                                ReflectionUtils.setVal(bean, property, fieldValue);

                                if (rxField != null && !Objects.equals(fieldValue, rxField.getValue()))
                                {
                                    rxField.setValue(fieldValue);
                                }
                            });

                }
        );
    }

    private void subscribeToChangeAndSetToField(RxField<Object> rxField,
                                                HasValue<Object> formField)
    {
        if (rxField != null)
        {
            rxField.getObservable().subscribe(newValue ->
            {
                if (newValue != null)
                {
                    fillBeanField(formField, rxField);
                    fillBeanCollectionField(formField, rxField);
                    formField.setValue(newValue);
                }
            });
        }
    }

    private void bindDefaultComponentState(RxField<Object> rxField, HasValue<Object> formField)
    {
        if (rxField != null && formField instanceof Component)
        {
            Component component = (Component) formField;
            BindUtils.subscribe(component, rxField);
        }
    }

    private Map<String, Field> createRxBeanFieldMap(final @Nullable RxBean<T> rxBean)
    {
        if (rxBean == null)
        {
            return new HashMap<>();
        }

        Class<?> rxBeanClass = rxBean.getClass();

        return ReflectionUtils.getFieldsInDeclareOrder(rxBeanClass)
                .stream()
                .filter(field -> RxField.class.isAssignableFrom(field.getType()))
                .collect(Collectors.toMap(Field::getName, Function.identity(), (oldValue, newValue) -> oldValue));
    }

    private void fillBeanField(HasValue<Object> field, Object rxBean)
    {
        if (field instanceof BeanField)
        {
            ((BeanField) field).setRxBean((RxBean) rxBean);
        }
    }

    private void fillBeanCollectionField(HasValue<Object> field, Object rxCollection)
    {
        if (field instanceof BeanCollectionField)
        {
            ((BeanCollectionField) field).setRxCollection((RxCollection) rxCollection);
        }
    }

    public void setBean(final @Nonnull T bean,
                        final @Nullable RxBean<T> rxBean)
    {
        if (rxBean != null)
        {
            rxBean.setValue(bean);
        }

        super.setBean(bean);
    }

}
