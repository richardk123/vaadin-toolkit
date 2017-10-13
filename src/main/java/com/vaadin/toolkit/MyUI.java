package com.vaadin.toolkit;

import javax.annotation.Nonnull;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.PropertyId;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.toolkit.common.BeanRenderer;
import com.vaadin.toolkit.common.BindingProvider;
import com.vaadin.toolkit.field.BeanField;
import com.vaadin.toolkit.form.Form;
import com.vaadin.toolkit.form.FormHandler;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import rx.Observable;

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

        FormHandler<Organization> formHandler = new FormHandler<>();

        formHandler.withSave(org ->
        {
            System.out.println(org.getName());
        });

        Form<Organization> form = new Form<>(formHandler, Organization.class)
                .withFormRenderer(new OrganizationRenderer());

        Organization org = new Organization("root");
        form.setBean(org);

        BindingProvider<Organization> bean = formHandler.getBean();
        Observable<String> firstName = (Observable<String>)(Observable<?>) bean.get("owner").get("firstName").getValue();
        Observable<String> lastName = (Observable<String>)(Observable<?>) bean.get("owner").get("lastName").getValue();

        BindingProvider<String> userName = bean.get("owner").get("userName");

        Observable.combineLatest(firstName, lastName, (s1, s2) -> {
            return s1 + "." + s2;
        }).subscribe(userName::setValue);

        setContent(form);
    }

    public class OrganizationRenderer implements BeanRenderer<Organization>
    {

        @PropertyId("name")
        private TextField nameField;

        @PropertyId("owner")
        private BeanField<User> testField;

        @Override
        public Component render(Organization bean)
        {
            nameField = new TextField("Organization name");
            testField = new BeanField<>(User.class, UserRenderer::new);
            testField.setCaption("Organization owner");
            return new FormLayout(nameField, testField);
        }
    }

    public class UserRenderer implements BeanRenderer<User>
    {
        @PropertyId("firstName")
        private TextField firstNameField;

        @PropertyId("lastName")
        private TextField lastNameField;

        @PropertyId("userName")
        private TextField userNameField;

        @Override
        public Component render(@Nonnull User bean)
        {
            firstNameField = new TextField("First name");
            lastNameField = new TextField("Last name");
            userNameField = new TextField("Username");
            return new HorizontalLayout(userNameField, firstNameField, lastNameField);
        }
    }

    public class User
    {
        private String firstName = "";
        private String lastName = "";
        private String userName = "";

        public String getFirstName()
        {
            return firstName;
        }

        public void setFirstName(String firstName)
        {
            this.firstName = firstName;
        }

        public String getLastName()
        {
            return lastName;
        }

        public void setLastName(String lastName)
        {
            this.lastName = lastName;
        }

        public String getUserName()
        {
            return userName;
        }

        public void setUserName(String userName)
        {
            this.userName = userName;
        }
    }

    public class Organization
    {
        private String name;
        private User owner = new User();

        public Organization(String name)
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

        public User getOwner()
        {
            return owner;
        }

        public void setOwner(User owner)
        {
            this.owner = owner;
        }
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
