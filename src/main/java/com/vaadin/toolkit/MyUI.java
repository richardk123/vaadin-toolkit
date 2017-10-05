package com.vaadin.toolkit;

import javax.servlet.annotation.WebServlet;

import com.vaadin.toolkit.common.FormRenderer;
import com.vaadin.toolkit.field.BeanCollectionField;
import com.vaadin.toolkit.field.BeanField;
import com.vaadin.toolkit.form.Form;
import com.vaadin.toolkit.form.FormHandler;
import com.vaadin.annotations.PropertyId;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

import java.util.ArrayList;
import java.util.List;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        FormHandler<Test> formHandler = new FormHandler<>(Test.class);

        formHandler.withSave(test ->
        {
            System.out.println(test.getName());
            System.out.println(test.getTest().getName());
            System.out.println(test.getTestList().get(0).getName());
            System.out.println(test.getTestList().get(1).getName());
            System.out.println(test.getTestList().get(2).getName());
        });

        Form<Test> form = new Form<>(formHandler)
                .withFormRenderer(new Renderer());

        Test test = new Test("root");
        test.setTest(new Test("inner"));
        test.addTest(new Test("1"));
        test.addTest(new Test("2"));
        test.addTest(new Test("3"));
        form.setBean(test);

        setContent(form);
    }

    public class Renderer implements FormRenderer<Test>
    {

        @PropertyId("name")
        private TextField nameField;

        @PropertyId("test")
        private BeanField<Test> testField;

        @PropertyId("testList")
        private BeanCollectionField<Test> listField;

        @Override
        public Component render(Test bean)
        {
            nameField = new TextField();
            testField = new BeanField<>(Test.class, Renderer::new);
            listField = new BeanCollectionField<>(Test.class, Renderer::new);
            return new FormLayout(nameField, testField, listField);
        }
    }

    public final class Test
    {
        private String name;
        private Test test;
        private List<Test> testList = new ArrayList<>();

        public Test(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public Test getTest()
        {
            return test;
        }

        public void setTest(Test test)
        {
            this.test = test;
        }

        public List<Test> getTestList()
        {
            return testList;
        }

        public void setTestList(List<Test> testList)
        {
            this.testList = testList;
        }

        public void addTest(Test test)
        {
            this.testList.add(test);
        }
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
