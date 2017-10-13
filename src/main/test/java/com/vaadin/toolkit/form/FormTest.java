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
    public void testChangingFieldValueAndExpectingValueInRfField()
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

        Assert.assertEquals("test", formHandler.getBean().get("name").getValueReal());
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
    public void testFieldProperty()
    {
        RxField<String> fieldProperty = new FieldProperty<>(true, true, "lul", "lul");

        Assert.assertNull(fieldProperty.getValueReal());

        fieldProperty.getValue().subscribe(val -> System.out.println(" val: " + val));

        fieldProperty.setValue("xxx");

        Assert.assertEquals("xxx", fieldProperty.getValueReal());
    }
}
