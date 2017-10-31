package com.vaadin.toolkit.common;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vaadin.util.ReflectTools;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * @author Kolisek
 */
public class ReflectionUtils
{


	public static Object getVal(Object bean, String property)
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

	public static void setVal(Object bean, String property, Object value)
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

	@Nullable
	public static <T> T getFieldValue(@Nullable Field field,
									  @Nonnull Object objectWithMemberFields,
									  @Nonnull Class<T> fieldClass)
	{
		if (field == null)
		{
			return null;
		}

		try
		{
			return (T) ReflectTools.getJavaFieldValue(objectWithMemberFields, field, fieldClass);
		}
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			return null;
		}
	}

	public static List<Field> getFieldsInDeclareOrder(Class<?> searchClass) {
		ArrayList<Field> memberFieldInOrder = new ArrayList<>();

		while (searchClass != null)
		{
			memberFieldInOrder.addAll(Arrays.asList(searchClass.getDeclaredFields()));
			searchClass = searchClass.getSuperclass();
		}
		return memberFieldInOrder;
	}
}