package ru.jnanovaadin.widgets.timeline.client;


import ru.jnanovaadin.widgets.timeline.VTimeLine;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;


@Connect(VTimeLine.class)
public class VTimeLineConnector extends AbstractComponentConnector {

	public VTimeLineConnector() {
		registerRpc(SetIntervalClientRcp.class, new SetIntervalClientRcp() {
			
			@Override
			public void setCurrentInterval(long startDate, long endDate) {
				getWidget().setCurrentInterval(startDate, endDate);
				
			}
		});
	}

	
	@Override
	protected Widget createWidget() {
		return GWT.create(VTimeLineWidget.class);
	}

	
	
	@Override
	public VTimeLineWidget getWidget() {
		return (VTimeLineWidget) super.getWidget();
	}

	
	@Override
	public VTimeLineState getState() {
		return (VTimeLineState) super.getState();
	}

	

}
