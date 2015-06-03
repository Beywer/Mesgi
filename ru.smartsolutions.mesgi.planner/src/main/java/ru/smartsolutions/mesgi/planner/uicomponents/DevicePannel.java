package ru.smartsolutions.mesgi.planner.uicomponents;

import java.util.Map;
import java.util.Set;

import ru.smartsolutions.mesgi.planner.model.Device;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class DevicePannel extends VerticalLayout {

//	Список устройств
	private Map<String, Button> deviceList;
	private VerticalLayout devicePanelVL;
	
	private TextArea deviceDescription;
	
	public DevicePannel() {
		super();
		buildInterface();
	}

	
	private void buildInterface(){
		
		this.setSizeFull();
		
//		VerticalLayout, в который сложатся кнопки устройств
		devicePanelVL = new VerticalLayout();
		devicePanelVL.setSizeFull();

//		Описание устрйоств
		deviceDescription = new TextArea("Описание устройств");
		deviceDescription.setSizeFull();
		deviceDescription.setReadOnly(true);
		
//		Иницаилизация кнопок устройств, если устройства уже есть
		Set<Device> devices = DataProvider.getDevices();
		
		for(final Device device : devices){
			Button deviceBut = new Button(device.getName());//имя кнопки
//			цвет кнопки
			if(device.getAvailability()) deviceBut.setStyleName("available-device");
			else deviceBut.setStyleName("unavailable-device");
//			описание задачи по нажатию
			deviceBut.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					deviceDescription.setReadOnly(false);
					deviceDescription.setValue(device.getDescription());
					deviceDescription.setReadOnly(true);
				}
			});
//			заполнение списка устройств
			devicePanelVL.addComponent(deviceBut);
		}
		
//		панель, в которой будет лежать список
		Panel deviceListPanel = new Panel("Устройства");
		deviceListPanel.setContent(devicePanelVL);
		deviceListPanel.setSizeFull();
		
		this.addComponent(deviceListPanel);
		this.addComponent(deviceDescription);
		
		this.setExpandRatio(deviceListPanel, 5);
		this.setExpandRatio(deviceDescription, 3);
	}
	
}
