package ru.jnanovaadin.widgets.timeline;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.Interval;

import ru.jnanovaadin.widgets.timeline.client.GetIntervalClientRcp;
import ru.jnanovaadin.widgets.timeline.client.SetIntervalClientRcp;
import ru.jnanovaadin.widgets.timeline.client.VTimeLineState;

import com.vaadin.annotations.JavaScript;


@JavaScript("timeline-min.js")
public class VTimeLine extends com.vaadin.ui.AbstractComponent {
	
	public static String NAME_COLUMN_STARTTIME	="startdate";
	public static String NAME_COLUMN_ENDTIME	="enddate";
	public static String NAME_COLUMN_CONTENT	="content";
	public static String NAME_COLUMN_GROUP		="group";

	public VTimeLine() {
		super();
		setSizeFull();
	}

	@Override
	public VTimeLineState getState() {
		return (VTimeLineState) super.getState();
	}
	
	
	public void setDatatable(List<Map<String,String>> datatable){
		getState().datatable=datatable;
	}
	
	public List<Map<String,String>> getDatatable(){
		return getState().datatable;
	}
	
	public void setCurrentInterval(long startDate, long endDate){
		getRpcProxy(SetIntervalClientRcp.class).setCurrentInterval(startDate, endDate);
	}
	
	public Interval getCurrentInterval(){
		return getRpcProxy(GetIntervalClientRcp.class).getCurrentInterval();
	}
	
		
	public static Map<String,String> createRow(long starttime,long endtime,String content,String group){
		Map<String,String> row = new HashMap<>();
		row.put(NAME_COLUMN_STARTTIME, String.valueOf(starttime));
		row.put(NAME_COLUMN_ENDTIME, String.valueOf(endtime));
		row.put(NAME_COLUMN_CONTENT, content);
		row.put(NAME_COLUMN_GROUP, group);
		return row;
	}
	
	
}
