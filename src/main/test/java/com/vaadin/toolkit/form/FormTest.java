package com.vaadin.toolkit.form;

import javax.annotation.Nonnull;

import com.vaadin.annotations.PropertyId;
import com.vaadin.data.HasValue;
import com.vaadin.toolkit.common.RxField;
import com.vaadin.toolkit.common.Organization;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import org.junit.Assert;
import org.junit.Test;

public class FormTest
{
    public class OrgFormHandler extends FormHandler<Organization>
    {
        @PropertyId("name")
        public RxField<String> name = new RxField<>("name");
    }

    private class OrgForm extends Form<Organization>
    {
        @PropertyId("name")
        private TextField nameField;

        public OrgForm(FormHandler<Organization> handler)
        {
            super(handler, Organization.class);
        }

        @Override
        public Component render(@Nonnull Organization bean)
        {
            nameField = new TextField();
            return new FormLayout(nameField);
        }
    }

    @Test
    public void testDefaultEntityDataInRxValue()
    {
        OrgFormHandler formHandler = new OrgFormHandler();
        OrgForm form = new OrgForm(formHandler);

        form.setBean(new Organization("default"));

        Assert.assertEquals("default", formHandler.name.getValue());
    }

    @Test
    public void testChangingFieldValueAndExpectingValueInRxField()
    {
        OrgFormHandler formHandler = new OrgFormHandler();
        OrgForm form = new OrgForm(formHandler);

        form.setBean(new Organization("default"));

        // set fieldValue
        form.getBinder().getFields()
                .filter(f -> f instanceof TextField)
                .findFirst()
                .ifPresent(f -> ((HasValue)f).setValue("test"));

        Assert.assertEquals("test", formHandler.name.getValue());
    }

    @Test
    public void testChangingRxValueAndExpectingValueInField()
    {
        OrgFormHandler formHandler = new OrgFormHandler();
        OrgForm form = new OrgForm(formHandler);

        form.setBean(new Organization("default"));

        formHandler.name.setValue("test");

        String value = (String) form.getBinder().getFields()
                .filter(f -> f instanceof TextField)
                .findFirst().get().getValue();

        Assert.assertEquals("test", value);
    }

    @Test
    public void testRxField()
    {
        RxField<String> fieldProperty = new RxField<>(true, true, "lul", "lul");

        Assert.assertNull(fieldProperty.getValue());

        fieldProperty.getObservable().subscribe(val -> System.out.println(" val: " + val));

        fieldProperty.setValue("xxx");

        Assert.assertEquals("xxx", fieldProperty.getValue());
    }
}
