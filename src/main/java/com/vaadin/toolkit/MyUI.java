package com.vaadin.toolkit;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.toolkit.form.FormHandler;
import com.vaadin.ui.UI;
import rx.Observable;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
public class MyUI extends UI {


    @Override
    protected void init(VaadinRequest vaadinRequest)
    {
        FormHandler<Organization,OrganizationRxBean> handler = new FormHandler<>();
        handler.withRxBean(new OrganizationRxBean());

        OrganizationRxBean rxBean = handler.getRxBean();

        // title of form
        rxBean.name.getObservable().subscribe(
                val -> handler.getTitleState().setCaption("Title for form: " + val));

        // username for owner
        fillUserName(rxBean.owner);

        // userName for users
        rxBean.users.getAddedSubject().subscribe(rx ->
        {
            fillUserName((UserRxBean) rx);
        });

        OrganizationForm orgForm = new OrganizationForm(this, handler);
        orgForm.setBean(new Organization());


        setContent(orgForm);
    }

    private void fillUserName(UserRxBean userRxBean)
    {
        Observable.combineLatest(
                userRxBean.firstName.getObservable(),
                userRxBean.lastName.getObservable(),
                (s1, s2) -> s1 + "." + s2)
                .subscribe(userRxBean.userName::setValue);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
