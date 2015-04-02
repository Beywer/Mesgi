/*
 * Copyright 2014 John Ahlroos
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package ru.beywer.vaadin.designe;

import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.Not;
import com.vaadin.shared.ui.dd.HorizontalDropLocation;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TabSheet.Tab;

import fi.jasoft.dragdroplayouts.DDTabSheet;
import fi.jasoft.dragdroplayouts.DDTabSheet.TabSheetTargetDetails;
import fi.jasoft.dragdroplayouts.drophandlers.AbstractDefaultLayoutDropHandler;
import fi.jasoft.dragdroplayouts.events.HorizontalLocationIs;
import fi.jasoft.dragdroplayouts.events.LayoutBoundTransferable;

/**
 * Default drop handler for tabsheets
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.6.0
 */
@SuppressWarnings("serial")
public class MyTabSheetDropHandler extends AbstractDefaultLayoutDropHandler {

  @Override
  public AcceptCriterion getAcceptCriterion() {
    // Only allow drops between tabs
    return new Not(HorizontalLocationIs.CENTER);
  }

  @Override
  protected void handleComponentReordering(DragAndDropEvent event) {
    LayoutBoundTransferable transferable = (LayoutBoundTransferable) event.getTransferable();
    TabSheetTargetDetails details = (TabSheetTargetDetails) event.getTargetDetails();
    DDTabSheet tabSheet = (DDTabSheet) details.getTarget();
    Component c = transferable.getComponent();
    Tab tab = tabSheet.getTab(c);
    HorizontalDropLocation location = details.getDropLocation();
    int idx = details.getOverIndex();
    System.out.println("New index: " + idx);
    
    if (location == HorizontalDropLocation.LEFT) {
      // Left of previous tab
      int originalIndex = tabSheet.getTabPosition(tab);
      System.out.println("originalIndex: " + originalIndex);
      if(originalIndex != idx)
	      if (originalIndex > idx) {
	        tabSheet.setTabPosition(tab, idx);
	      } else if (idx - 1 >= 0) {
	        tabSheet.setTabPosition(tab, idx - 1);
	      }

    } else if (location == HorizontalDropLocation.RIGHT) {
      // Right of previous tab
      int originalIndex = tabSheet.getTabPosition(tab);
      System.out.println("originalIndex: " + originalIndex);
      if(originalIndex != idx)
	      if (originalIndex > idx) {
	        tabSheet.setTabPosition(tab, idx + 1);
	      } else {
	        tabSheet.setTabPosition(tab, idx);
	      }
    }
  }

  @Override
  protected void handleDropFromLayout(DragAndDropEvent event) {
    LayoutBoundTransferable transferable = (LayoutBoundTransferable) event.getTransferable();
    TabSheetTargetDetails details = (TabSheetTargetDetails) event.getTargetDetails();
    DDTabSheet tabSheet = (DDTabSheet) details.getTarget();
    Component c = transferable.getComponent();
    HorizontalDropLocation location = details.getDropLocation();
    int idx = details.getOverIndex();
    ComponentContainer source = (ComponentContainer) transferable.getSourceComponent();
    System.out.println("New tab");
    System.out.println("New index: " + idx);
    
    TabSheet sourceTabsheet = null;
    if(source instanceof TabSheet)
    	sourceTabsheet = (TabSheet) source;

	String caption = c.getCaption();
    if(sourceTabsheet != null){
    	caption = sourceTabsheet.getTab(c).getCaption();
    	System.out.println(caption);
    }
    source.removeComponent(c);
    if (location == HorizontalDropLocation.LEFT) {
      tabSheet.addTab(c, idx);
      tabSheet.getTab(idx).setCaption(caption);
    } else if (location == HorizontalDropLocation.RIGHT) {
      tabSheet.addTab(c, idx + 1);
      tabSheet.getTab(idx + 1).setCaption(caption);
    }
    if( idx == -1){
    	int count = tabSheet.getComponentCount();
    	tabSheet.addTab(c, count);
        tabSheet.getTab(count).setCaption(caption);
    }

//    tabSheet.getTab(0).setCaption("asdasd");
//    int componentCount = tabSheet.getComponentCount();
//    for(int i = 0 ; i < componentCount; i++){
//		System.out.println(i + "  " + tabSheet.getTab(i));
//    }
    
  }

  protected String resolveCaptionFromHTML5Drop(DragAndDropEvent event) {
    return event.getTransferable().getData("html5Data").toString();
  }

  @Override
  protected void handleHTML5Drop(DragAndDropEvent event) {
    TabSheetTargetDetails details = (TabSheetTargetDetails) event.getTargetDetails();
    HorizontalDropLocation location = details.getDropLocation();
    DDTabSheet tabSheet = (DDTabSheet) details.getTarget();
    int idx = details.getOverIndex();

    Component c = resolveComponentFromHTML5Drop(event);
    c.setCaption(resolveCaptionFromHTML5Drop(event));

    if (location == HorizontalDropLocation.LEFT) {
      tabSheet.addTab(c, idx);
    } else if (location == HorizontalDropLocation.RIGHT) {
      tabSheet.addTab(c, idx + 1);
    } 
  }
}
