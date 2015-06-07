package ru.jnanovaadin.widgets.timeline.client;

import org.joda.time.Interval;

import com.vaadin.shared.communication.ClientRpc;

public interface GetIntervalClientRcp extends ClientRpc{
	public Interval getCurrentInterval();
}
