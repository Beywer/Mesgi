package ru.jnanovaadin.widgets.timeline.client;

import com.vaadin.shared.communication.ClientRpc;

public interface SetIntervalClientRcp extends ClientRpc{
	public void setCurrentInterval(long startDate, long endDate);
}
