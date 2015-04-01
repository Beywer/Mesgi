package ru.beywer.vaadin.ru.beywer.vaadin.consoleecho;

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
@Theme("ConsoleEchoTheme")
@Widgetset("ru.beywer.vaadin.ru.beywer.vaadin.consoleecho.ConsoleEchoWidgetset")
public class ConsoleEchoUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
//        final VerticalLayout layout = new VerticalLayout();
//        layout.setMargin(true);
//        setContent(layout);
//
//        Button button = new Button("Click Me");
//        button.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(ClickEvent event) {
//                layout.addComponent(new Label("Thank you for clicking"));
//            }
//        });
//        layout.addComponent(button);
    	setContent(new MainPage());
    }

    @WebServlet(urlPatterns = "/*", name = "ConsoleEchoUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = ConsoleEchoUI.class, productionMode = false)
    public static class ConsoleEchoUIServlet extends VaadinServlet {
    }
}
