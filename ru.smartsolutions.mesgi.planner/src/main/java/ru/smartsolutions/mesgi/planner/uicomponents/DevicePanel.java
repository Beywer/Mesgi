package ru.smartsolutions.mesgi.planner.uicomponents;

import java.util.HashMap;
import java.util.Map;

import ru.smartsolutions.mesgi.model.Device;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class DevicePanel extends VerticalLayout {

//	Список устройств
	private Map<String, Button> deviceList;
	private VerticalLayout devicePanelVL;
	
	private TextArea deviceDescription;
	
	public DevicePanel() {
		super();
		
		deviceList = new HashMap<String, Button>();
		
		buildInterface();
	}

	
	private void buildInterface(){
	
		this.setSizeFull();
		
//		VerticalLayout, в который сложатся кнопки устройств
		devicePanelVL = new VerticalLayout();
//		devicePanelVL.setSizeFull();

//		Описание устрйоств
		deviceDescription = new TextArea("Описание устройств");
		deviceDescription.setSizeFull();
		deviceDescription.setReadOnly(true);
		
//		Иницаилизация кнопок устройств, если устройства уже есть
		Map<String, Device> devices = DataProvider.getDevices();
		
		for(String key : devices.keySet()){
			Device device = devices.get(key);
			addDevice(device);
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
	
	public void addDevice(final Device device) {
		
//		новая кнопка и именем устройства
		Button deviceBut = new Button(device.getName());
		deviceBut.setSizeFull();
		final String descr = device.getDescription();
		
//		цвет кнопки основан на доступности
		if(device.isAvailability()) deviceBut.setStyleName("available-device");
		else deviceBut.setStyleName("unavailable-device");
		
//		описание задачи по нажатию
		deviceBut.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				deviceDescription.setReadOnly(false);
				deviceDescription.setValue(descr);
				deviceDescription.setReadOnly(true);
			}
		});
		
//		заполнение списка устройств
		devicePanelVL.addComponent(deviceBut);
		deviceList.put(device.getAddress(), deviceBut);
		
//		обновление UI, которому принаделжит панель
		HasComponents component = getParent();
		if(component instanceof MainComponent){
			((MainComponent) component).updateUI();
		}
	}
	
	public void updateDevice(String ip, boolean availability){
		
		Button deviceBut = deviceList.get(ip);
//		цвет кнопки
		if(availability) deviceBut.setStyleName("available-device");
		else deviceBut.setStyleName("unavailable-device");
		
//		обновление UI, которому принаделжит панель
		HasComponents component = getParent();
		if(component instanceof MainComponent){
			((MainComponent) component).updateUI();
		}
	}
}
