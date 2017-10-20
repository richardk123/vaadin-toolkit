package com.vaadin.toolkit.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.vaadin.annotations.PropertyId;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.toolkit.field.BeanField;

public class RxBinder<T> extends Binder<T>
{

    private List<RxField<Object>> rxFields = new ArrayList<>();

    public RxBinder(Class<T> beanType)
    {
        super(beanType);
    }

    private String getFieldName(Field field)
    {
        return field.getAnnotationsByType(PropertyId.class)[0].value();
    }

    public void bindInstanceFields(final Object objectWithMemberFields,
                                   final Object rxBean)
    {
        rxFields = new ArrayList<>();
        Class<?> formClass = objectWithMemberFields.getClass();
        Class<?> handlerClass = rxBean.getClass();

        final Map<String, Field> formFieldMap = ReflectionUtils.getFieldsInDeclareOrder(formClass)
                .stream()
                .filter(field -> HasValue.class.isAssignableFrom(field.getType()))
                .filter(field -> field.getAnnotationsByType(PropertyId.class).length > 0)
                .collect(Collectors.toMap(this::getFieldName, Function.identity(), (oldValue, newValue) -> oldValue));

        final Map<String, Field> handlerFieldMap = ReflectionUtils.getFieldsInDeclareOrder(handlerClass)
                .stream()
                .filter(field -> RxField.class.isAssignableFrom(field.getType()))
                .filter(field -> field.getAnnotationsByType(PropertyId.class).length > 0)
                .collect(Collectors.toMap(this::getFieldName, Function.identity(), (oldValue, newValue) -> oldValue));

        formFieldMap.forEach(
                (property, formJavaField) ->
                {
                    Field handlerJavaField = handlerFieldMap.get(property);

                    if (handlerJavaField == null)
                    {
                        throw new RuntimeException(String.format("property: %s is missing on handler", property));
                    }

                    final HasValue<Object> formField = ReflectionUtils.getFieldValue(formJavaField, objectWithMemberFields, HasValue.class);
                    final RxField<Object> rxField = ReflectionUtils.getFieldValue(handlerJavaField, rxBean, RxField.class);

                    this.rxFields.add(rxField);

                    if (formField == null)
                    {
                        throw new RuntimeException(String.format("formField for property %s is null", property));
                    }

                    if (rxField == null)
                    {
                        throw new RuntimeException(String.format("rxField for property %s is null", property));
                    }

                    // listen to changes on rxFields and set them to the form field
                    rxField.getObservable().subscribe(newValue ->
                    {
                        if (newValue != null)
                        {
                            fillBeanField(formField, rxField);
                            formField.setValue(newValue);
                        }
                    });

                    bind(formField,
                            // getter
                            (bean) -> ReflectionUtils.getVal(bean, property),
                            // setter
                            (bean, fieldValue) ->
                            {
                                ReflectionUtils.setVal(bean, property, fieldValue);

                                if (!Objects.equals(fieldValue, rxField.getValue()))
                                {
                                    rxField.setValue(fieldValue);
                                }
                            });

                }
        );
    }

    private void fillBeanField(HasValue<Object> field, Object rxBean)
    {
        if (field instanceof BeanField)
        {
            ((BeanField) field).setRxBean((RxBean) rxBean);
        }
    }

    @Override
    public void setBean(final T bean)
    {
        // TODO: on first set of value there should not be an event for all subscribers
        rxFields.forEach(rxField ->
        {
            String property = rxField.getProperty();
            Object value = ReflectionUtils.getVal(bean, property);
            rxField.setValue(value);
        });

        super.setBean(bean);
    }

}
