package com.vaadin.toolkit.common;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Kolisek
 */
public abstract class RxBean<T> extends RxField<T>
{
	public RxBean()
	{
		super();
		// fill values for all RxFields
		this.value.subscribe(
				bean ->
				{
					final Map<String, Field> beanFieldMap = ReflectionUtils.getFieldsInDeclareOrder(this.getClass())
							.stream()
							.filter(field -> RxField.class.isAssignableFrom(field.getType()))
							.filter(field -> !field.getName().startsWith("this"))
							.collect(Collectors.toMap(
									Field::getName,
									Function.identity(),
									(oldValue, newValue) -> oldValue));


					beanFieldMap.forEach(
							(prop, field) ->
							{
								Object value = ReflectionUtils.getVal(bean, prop);
								RxField rxField = ReflectionUtils.getFieldValue(field, this, RxField.class);
								rxField.setValue(value);
							}
					);
				}
		);
	}

}
