package com.vaadin.toolkit.form;

import com.vaadin.data.HasValue;
import com.vaadin.toolkit.common.TestData;
import com.vaadin.toolkit.common.TestDataRenderer;
import com.vaadin.ui.TextField;
import org.junit.Assert;
import org.junit.Test;

public class FormTest
{

    @Test
    public void testSimpleFormValue()
    {
        FormHandler<TestData> formHandler = new FormHandler<>(TestData.class);

        formHandler.withSave(testData ->
        {
            Assert.assertEquals("test", testData.getName());
        });

        Form<TestData> form = new Form<>(formHandler)
                .withFormRenderer(new TestDataRenderer());

        form.setBean(new TestData("default"));

        // set fieldValue
        formHandler.getBinder().getValue().getFields()
                .filter(f -> f instanceof TextField)
                .findFirst()
                .ifPresent(f -> ((HasValue)f).setValue("test"));

        formHandler.saveBean();
    }
}
