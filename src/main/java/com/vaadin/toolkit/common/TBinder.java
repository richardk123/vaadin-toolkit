package com.vaadin.toolkit.common;

import com.vaadin.annotations.PropertyId;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.toolkit.field.BeanCollectionField;
import com.vaadin.util.ReflectTools;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TBinder<T> extends Binder<T>
{

    public TBinder(Class<T> beanClass)
    {
        super(beanClass);
    }

    @Override
    public void bindInstanceFields(Object objectWithMemberFields)
    {
        Class<?> objectClass = objectWithMemberFields.getClass();

        getFieldsInDeclareOrder(objectClass)
                .stream()
                .filter(memberField -> BeanCollectionField.class.isAssignableFrom(memberField.getType()))
                .forEach(field ->
                {
                    HasValue fieldInstalce = getFieldValue(field, objectWithMemberFields);
                    bind(fieldInstalce, field.getAnnotationsByType(PropertyId.class)[0].value());
                });

        super.bindInstanceFields(objectWithMemberFields);
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
}
