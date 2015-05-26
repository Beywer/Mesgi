package ru.smartsolutions.mesgi.planner;

import javax.servlet.annotation.WebServlet;

import ru.jnanovaadin.widgets.timeline.VTimeLine;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 *
 */

@JavaScript ({ "1.js", "2.js"})
@Theme("plannertheme")
@Widgetset("ru.smartsolutions.mesgi.planner.PlannerWidgetset")
@Push(value=PushMode.MANUAL)
//@Push
public class PlannerUI extends UI {

	private static UI ui;
	
    @Override
    protected void init(VaadinRequest vaadinRequest) {
    	ui = this;
    	setContent(new MainComponent(this));
    }

    @WebServlet(urlPatterns = "/*", name = "PlannerUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = PlannerUI.class, productionMode = false)
    public static class PlannerUIServlet extends VaadinServlet {
    }
}
