package com.vaadin.toolkit.form;

import javax.annotation.Nonnull;

import com.vaadin.annotations.PropertyId;
import com.vaadin.data.HasValue;
import com.vaadin.toolkit.common.BeanRenderer;
import com.vaadin.toolkit.common.TestData;
import com.vaadin.toolkit.field.BeanField;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Kolisek
 */
public class BeanFieldTest
{

	@Test
	public void testDefaultValueInBeanFieldRxProperty()
	{

		FormHandler<TestData> formHandler = new FormHandler<>();
		Form<TestData> form = new Form<>(formHandler, TestData.class)
				.withFormRenderer(new FormRenderer());

		TestData testData = new TestData("default");
		testData.setTest(new TestData("innerValue"));
		form.setBean(testData);

		String rxValue = (String) formHandler.getBean().get("test").get("name").getValue();

		Assert.assertEquals("innerValue", rxValue);
	}

	@Test
	public void testChangeInBeanFieldValuePropagateToRxField()
	{
		FormHandler<TestData> formHandler = new FormHandler<>();
		Form<TestData> form = new Form<>(formHandler, TestData.class)
				.withFormRenderer(new FormRenderer());

		TestData testData = new TestData("default");
		testData.setTest(new TestData("innerData"));
		form.setBean(testData);

		BeanField<?> beanField = (BeanField) form.getBinder().getFields()
				.filter(f -> f instanceof BeanField)
				.findFirst().get();

		beanField.getBinder().getFields()
				.filter(f -> f instanceof TextField)
				.findFirst()
				.ifPresent(f -> ((HasValue) f).setValue("changedValue"));

		String rxValue = (String) formHandler.getBean().get("test").get("name").getValue();

		Assert.assertEquals("changedValue", rxValue);
	}


	public class TextFieldRenderer implements BeanRenderer<TestData>
	{
		@PropertyId("name")
		private TextField nameField;

		@Override
		public Component render(@Nonnull TestData bean)
		{
			nameField = new TextField();
			return nameField;
		}
	}

	public class FormRenderer implements BeanRenderer<TestData>
	{

		@PropertyId("test")
		private BeanField<TestData> testField;

		@Override
		public Component render(@Nonnull TestData bean)
		{
			testField = new BeanField<>(TestData.class, TextFieldRenderer::new);
			return testField;
		}
	}

}
