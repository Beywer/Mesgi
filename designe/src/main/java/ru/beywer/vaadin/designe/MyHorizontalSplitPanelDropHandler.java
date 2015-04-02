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

import fi.jasoft.dragdroplayouts.DDHorizontalSplitPanel;
import fi.jasoft.dragdroplayouts.DDHorizontalSplitPanel.HorizontalSplitPanelTargetDetails;
import fi.jasoft.dragdroplayouts.drophandlers.AbstractDefaultLayoutDropHandler;
import fi.jasoft.dragdroplayouts.events.HorizontalLocationIs;
import fi.jasoft.dragdroplayouts.events.LayoutBoundTransferable;

/**
 * Defalt drop handler for horizontal split panels
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.6.0
 */
@SuppressWarnings("serial")
public class MyHorizontalSplitPanelDropHandler extends AbstractDefaultLayoutDropHandler {

  @Override
  public AcceptCriterion getAcceptCriterion() {
    // Only allow dropping in slots, not on the center bar
    return new Not(HorizontalLocationIs.CENTER);
  }

  @Override
  protected void handleComponentReordering(DragAndDropEvent event) {
    handleDropFromLayout(event);
  }

  @Override
  protected void handleDropFromLayout(DragAndDropEvent event) {
    LayoutBoundTransferable transferable = (LayoutBoundTransferable) event.getTransferable();
    ComponentContainer source = (ComponentContainer) transferable.getSourceComponent();
    HorizontalSplitPanelTargetDetails details =
        (HorizontalSplitPanelTargetDetails) event.getTargetDetails();
    Component component = transferable.getComponent();
    DDHorizontalSplitPanel panel = (DDHorizontalSplitPanel) details.getTarget();

    // Remove component from its source
    source.removeComponent(component);
    
    if (details.getDropLocation() == HorizontalDropLocation.LEFT) {
      // Dropped in the left area
      Component otherComponent = panel.getFirstComponent();
      panel.setFirstComponent(component);
      panel.setSecondComponent(otherComponent);

    } else if (details.getDropLocation() == HorizontalDropLocation.RIGHT) {
      // Dropped in the right area
      Component otherComponent = panel.getSecondComponent();
      panel.setSecondComponent(component);
      panel.setFirstComponent(otherComponent);
    }
  }

  @Override
  protected void handleHTML5Drop(DragAndDropEvent event) {
    HorizontalSplitPanelTargetDetails details =
        (HorizontalSplitPanelTargetDetails) event.getTargetDetails();
    DDHorizontalSplitPanel panel = (DDHorizontalSplitPanel) details.getTarget();

    if (details.getDropLocation() == HorizontalDropLocation.LEFT) {
      // Dropped in the left area
      panel.setFirstComponent(resolveComponentFromHTML5Drop(event));

    } else if (details.getDropLocation() == HorizontalDropLocation.RIGHT) {
      // Dropped in the right area
      panel.setSecondComponent(resolveComponentFromHTML5Drop(event));
    }

  }
}
