package ru.beywer.vaadin.designe;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 *
 */
@Theme("DesigneTheme")
@Widgetset("ru.beywer.vaadin.designe.DesigneWidgetset")
public class DesigneUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
    	setContent(new MainPage());
    }

    @WebServlet(urlPatterns = "/*", name = "DesigneUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = DesigneUI.class, productionMode = false)
    public static class DesigneUIServlet extends VaadinServlet {
    }
}
