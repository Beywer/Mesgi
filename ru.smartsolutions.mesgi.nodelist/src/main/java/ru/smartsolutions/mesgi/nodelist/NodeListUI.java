package ru.smartsolutions.mesgi.nodelist;

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
@Theme("NodeListTheme")
@Widgetset("ru.smartsolutions.mesgi.ru.smartsolutions.mesgi.nodelist.NodeListWidgetset")
public class NodeListUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
    	Activator.mainPage.setUi(this);
    	setContent(Activator.mainPage);
    }

    @WebServlet(urlPatterns = "/*", name = "NodeListUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = NodeListUI.class, productionMode = false)
    public static class NodeListUIServlet extends VaadinServlet {
    }
}
