package ru.smartsolutions.mesgi.planner.uicomponents;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class MessageBox extends Window {

	public MessageBox(String message) {

		this.setDraggable(false);
		this.setCaption("Внимание");
		this.setResizable(false);
		this.setClosable(false);
		this.setModal(true);
		this.center();
		
		this.setWidth(50, Unit.PERCENTAGE);
		this.setHeight(50, Unit.PERCENTAGE);
		
		VerticalLayout mainVL = new VerticalLayout();
		mainVL.setSizeFull();
		this.setContent(mainVL);
		
		TextArea messageTA = new TextArea();
		messageTA.setSizeFull();
		messageTA.setValue(message);
		messageTA.setReadOnly(true);
		mainVL.addComponent(messageTA);
		mainVL.setComponentAlignment(messageTA, Alignment.BOTTOM_CENTER);
		
		Button confirmButton = new Button("Ок");
		confirmButton.setStyleName("friendly");
		mainVL.addComponent(confirmButton);
		mainVL.setComponentAlignment(confirmButton, Alignment.BOTTOM_CENTER);
		
		mainVL.setExpandRatio(messageTA, 5);
		mainVL.setExpandRatio(confirmButton, 1);
		
		
		confirmButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				MessageBox.this.close();
			}
		});
	
	}
	
}
