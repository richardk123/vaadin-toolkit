package com.vaadin.toolkit;

import javax.servlet.annotation.WebServlet;

import com.vaadin.toolkit.common.FormRenderer;
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
        formHandler.withSave(test -> System.out.println(test.getName()));

        Form<Test> form = new Form<>(formHandler)
                .withFormRenderer(new Renderer());

        form.setBean(new Test());

        setContent(form);
    }

    public class Renderer implements FormRenderer<Test>
    {

        @PropertyId("name")
        private TextField nameField;

        @Override
        public Component render(Test bean)
        {
            nameField = new TextField();
            return new FormLayout(nameField);
        }
    }

    private class Test
    {
        private String name;

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
