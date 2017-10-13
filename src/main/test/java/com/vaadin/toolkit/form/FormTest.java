package com.vaadin.toolkit.form;

import com.vaadin.data.HasValue;
import com.vaadin.toolkit.common.FieldProperty;
import com.vaadin.toolkit.common.RxField;
import com.vaadin.toolkit.common.TestData;
import com.vaadin.toolkit.common.TestDataRenderer;
import com.vaadin.ui.TextField;
import org.junit.Assert;
import org.junit.Test;

public class FormTest
{
    @Test
    public void testDefaultEntityDataInRxValue()
    {
        FormHandler<TestData> formHandler = new FormHandler<>();
        Form<TestData> form = new Form<>(formHandler, TestData.class)
                .withFormRenderer(new TestDataRenderer());

        form.setBean(new TestData("default"));

        Assert.assertEquals("default", formHandler.getBean().get("name").getValue());
    }

    @Test
    public void testChangingFieldValueAndExpectingValueInRxField()
    {
        FormHandler<TestData> formHandler = new FormHandler<>();
        Form<TestData> form = new Form<>(formHandler, TestData.class)
                .withFormRenderer(new TestDataRenderer());

        form.setBean(new TestData("default"));

        // set fieldValue
        form.getBinder().getFields()
                .filter(f -> f instanceof TextField)
                .findFirst()
                .ifPresent(f -> ((HasValue)f).setValue("test"));

        Assert.assertEquals("test", formHandler.getBean().get("name").getValue());
    }

    @Test
    public void testChangingRxValueAndExpectingValueInField()
    {
        FormHandler<TestData> formHandler = new FormHandler<>();
        Form<TestData> form = new Form<>(formHandler, TestData.class)
                .withFormRenderer(new TestDataRenderer());

        form.setBean(new TestData("default"));

        formHandler.getBean().get("name").setValue("test");

        String value = (String) form.getBinder().getFields()
                .filter(f -> f instanceof TextField)
                .findFirst().get().getValue();

        Assert.assertEquals("test", value);
    }

    @Test
    public void testRxField()
    {
        RxField<String> fieldProperty = new FieldProperty<>(true, true, "lul", "lul");

        Assert.assertNull(fieldProperty.getValue());

        fieldProperty.getObservable().subscribe(val -> System.out.println(" val: " + val));

        fieldProperty.setValue("xxx");

        Assert.assertEquals("xxx", fieldProperty.getValue());
    }
}
