package ru.smartsolutions.mesgi;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

public class NodeReciver implements EventHandler {

  public void handleEvent(Event event) {
    System.out.println(event.getProperty("property1"));
  }

  public NodeReciver() {
  }

}
