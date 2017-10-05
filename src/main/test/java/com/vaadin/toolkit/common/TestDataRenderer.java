package com.vaadin.toolkit.common;

import com.vaadin.annotations.PropertyId;
import com.vaadin.toolkit.MyUI;
import com.vaadin.toolkit.field.BeanCollectionField;
import com.vaadin.toolkit.field.BeanField;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class TestDataRenderer implements FormRenderer<TestData>
{

    @PropertyId("name")
    private TextField nameField;

    @PropertyId("test")
    private BeanField<TestData> testField;

    @PropertyId("testList")
    private BeanCollectionField<TestData> listField;

    @Override
    public Component render(TestData bean)
    {
        nameField = new TextField();
        testField = new BeanField<>(TestData.class, TestDataRenderer::new);
        listField = new BeanCollectionField<>(TestData.class, TestDataRenderer::new);
        return new FormLayout(nameField, testField, listField);
    }
}
