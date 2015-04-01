package ru.smartsolutions.mesgi;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

public class NodeReciver implements EventHandler {

  public void handleEvent(Event event) {
    System.out.println(event.getProperty("IPv6") + "  " + event.getProperty("Availability"));
  }

  public NodeReciver() {
  }

}
