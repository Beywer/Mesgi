package ru.jnanovaadin.widgets.timeline.client;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.vaadin.shared.annotations.DelegateToWidget;

public class VTimeLineState extends com.vaadin.shared.AbstractComponentState {
	
	@DelegateToWidget
	public List<Map<String,String>> datatable = new ArrayList<>();
	
	
	

}