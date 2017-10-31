package com.vaadin.toolkit.form;

import javax.annotation.Nonnull;

import com.vaadin.annotations.PropertyId;
import com.vaadin.data.HasValue;
import com.vaadin.toolkit.common.RxBean;
import com.vaadin.toolkit.common.RxField;
import com.vaadin.toolkit.common.Organization;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import org.junit.Assert;
import org.junit.Test;

public class FormTest
{

    public class OrgRxBean extends RxBean<Organization>
    {
        public RxField<String> name = new RxField<>();
    }

    private class OrgForm extends Form<Organization>
    {
        @PropertyId("name")
        private TextField nameField;

        public OrgForm(FormHandler<Organization, OrgRxBean> handler)
        {
            super(handler);
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
        FormHandler<Organization, OrgRxBean> formHandler = new FormHandler<>();
        formHandler.withRxBean(new OrgRxBean());

        OrgForm form = new OrgForm(formHandler);
        form.setBean(new Organization("default"));

        Assert.assertEquals("default", formHandler.getRxBean().name.getValue());
    }

    @Test
    public void testNullBean()
    {
        FormHandler<Organization, OrgRxBean> formHandler = new FormHandler<>();
        formHandler.withRxBean(new OrgRxBean());

        OrgForm form = new OrgForm(formHandler);

        form.setBean(null);
    }

    @Test
    public void testChangingFieldValueAndExpectingValueInRxField()
    {
        FormHandler<Organization, OrgRxBean> formHandler = new FormHandler<>();
        formHandler.withRxBean(new OrgRxBean());

        OrgForm form = new OrgForm(formHandler);

        form.setBean(new Organization("default"));

        // set fieldValue
        form.getBinder().getFields()
                .filter(f -> f instanceof TextField)
                .findFirst()
                .ifPresent(f -> ((HasValue)f).setValue("test"));

        Assert.assertEquals("test", formHandler.getRxBean().name.getValue());
    }

    @Test
    public void testChangingRxValueAndExpectingValueInField()
    {
        FormHandler<Organization, OrgRxBean> formHandler = new FormHandler<>();
        formHandler.withRxBean(new OrgRxBean());

        OrgForm form = new OrgForm(formHandler);

        form.setBean(new Organization("default"));

        formHandler.getRxBean().name.setValue("test");

        String value = (String) form.getBinder().getFields()
                .filter(f -> f instanceof TextField)
                .findFirst().get().getValue();

        Assert.assertEquals("test", value);
    }

    @Test
    public void testRxField()
    {
        RxField<String> fieldProperty = new RxField<>();

        Assert.assertNull(fieldProperty.getValue());

        fieldProperty.getObservable().subscribe(val -> System.out.println(" val: " + val));

        fieldProperty.setValue("xxx");

        Assert.assertEquals("xxx", fieldProperty.getValue());
    }
}
