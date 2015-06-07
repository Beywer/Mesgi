package ru.jnanovaadin.widgets.timeline.client;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joda.time.Interval;

import com.chap.links.client.Timeline;
import com.chap.links.client.Timeline.DateRange;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;

// Extend any GWT Widget
public class VTimeLineWidget extends ScrollPanel {
	
	private Timeline timeline;
	
	public VTimeLineWidget() {
		
		Runnable onLoadCallback = new Runnable() {
		      public void run() {
		    	 timeline = new Timeline(DataTable.create(),createDefaultTimelineOptions());
		    	 VTimeLineWidget.this.add(timeline);
		      }
		};
		//VisualizationUtils.loadVisualizationApi(onLoadCallback);
		onLoadCallback.run();
	}
	
	public void setCurrentInterval(final long startDate, final long endDate){
		Runnable onLoadCallback = new Runnable() {
		      public void run() {
		    	  timeline.setVisibleChartRange(new Date(startDate), new Date(endDate));
		      }
		};
		//VisualizationUtils.loadVisualizationApi(onLoadCallback);
		onLoadCallback.run();
	}
	
	public Interval getCurrentInteval(){

		long start = timeline.getVisibleChartRange().getStart().getTime();
		long end = timeline.getVisibleChartRange().getEnd().getTime();
		
		return new Interval(start, end);
		
	}

	public void setDatatable(final List<Map<String,String>> datatable){
		Runnable onLoadCallback = new Runnable() {
		      public void run() {
		    	 DataTable dataTable = DataTable.create();
		  		
		  		 dataTable.addColumn(DataTable.ColumnType.DATETIME, "startdate");
		  		 dataTable.addColumn(DataTable.ColumnType.DATETIME, "enddate");
		  		 dataTable.addColumn(DataTable.ColumnType.STRING, "content");
		  		 dataTable.addColumn(DataTable.ColumnType.STRING, "group");
		  		
		  		 if (datatable!=null){
		  			int currentRow = 0;
		  			for(Map<String,String> row:datatable){
		  				dataTable.addRow();
		  						  				
		  				dataTable.setValue(currentRow, 0, new Date(Long.parseLong(row.get("startdate"))));
		  				dataTable.setValue(currentRow, 1, new Date(Long.parseLong(row.get("enddate"))));
		  				dataTable.setValue(currentRow, 2, row.get("content"));
		  				dataTable.setValue(currentRow, 3, row.get("group"));
		  							
		  				currentRow++;
		  				
		  			 }
		  		  }
		    	  timeline.setData(dataTable);
			      timeline.setVisibleChartRangeAuto();  
		      }
		};
		//VisualizationUtils.loadVisualizationApi(onLoadCallback);
		onLoadCallback.run();
	}
	
		
	private static Timeline.Options createDefaultTimelineOptions(){
		Timeline.Options options = Timeline.Options.create();
        options.setWidth("100%");
        options.setStyle(Timeline.Options.STYLE.BOX);
        options.setGroupsWidth(100);
        return options;
	}

}