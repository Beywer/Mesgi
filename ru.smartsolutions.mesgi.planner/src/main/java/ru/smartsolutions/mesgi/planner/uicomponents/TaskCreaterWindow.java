package ru.smartsolutions.mesgi.planner.uicomponents;

import java.util.Date;

import org.joda.time.Interval;

import ru.smartsolutions.mesgi.model.Task;

import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class TaskCreaterWindow extends Window {
	
	private VerticalLayout mainVL;
	
	private TextField name;
	private TextField duration;
	private DateField fromField;
	private DateField toField;
	private Button add;
	private Button back;
	
	private Task task;
	
	public TaskCreaterWindow(){

		System.out.println(this);
		
		this.setDraggable(false);
		this.center();
		this.setModal(true);
		this.setResizable(false);
		this.setClosable(false);
		
		mainVL = new VerticalLayout();
		mainVL.setMargin(true);
		mainVL.setSpacing(true);
		this.setContent(mainVL);
		
		FormLayout formLayout = new FormLayout();
		formLayout.setSizeUndefined();
		mainVL.addComponent(formLayout);
		
		name = new TextField("Название");
		name.setWidth(200, Unit.PIXELS);
		formLayout.addComponent(name);
		
		fromField = new DateField("От");
		fromField.setWidth(200, Unit.PIXELS);
		formLayout.addComponent(fromField);
		
		toField = new DateField("До");
		toField.setWidth(200, Unit.PIXELS);
		formLayout.addComponent(toField);
		
		duration = new TextField("Длительность, мин");
		duration.setWidth(200, Unit.PIXELS);
		formLayout.addComponent(duration);
		
		HorizontalLayout buttonsHL = new HorizontalLayout();
		buttonsHL.setSizeFull();
		buttonsHL.setSpacing(true);
		mainVL.addComponent(buttonsHL);
		
		add = new Button("Добавить");
		add.setStyleName("friendly");
//		add.setSizeFull();
		buttonsHL.addComponent(add);
		buttonsHL.setComponentAlignment(add, Alignment.MIDDLE_CENTER);
		
		back = new Button("Отмена");
		back.setStyleName("danger");
//		add.setSizeFull();
		buttonsHL.addComponent(back);
		buttonsHL.setComponentAlignment(back, Alignment.MIDDLE_CENTER);
		
		fromField.setResolution(Resolution.SECOND);
		fromField.setValue(new Date());
		toField.setResolution(Resolution.SECOND);
		toField.setValue(new Date(System.currentTimeMillis()+20*60*1000));
		add.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				String message = "";
				if(name.getValue().equals("")) 
					message += "\nНе введено название задачи";
				if(duration.getValue() == null) 
					message += "\nНе введена продолжительность задачи";
				else try{
					long duretionMillis = Integer.valueOf(duration.getValue())*60*1000;
					long intervalDuration = toField.getValue().getTime() - 
							fromField.getValue().getTime();
					if(duretionMillis >= intervalDuration) 
						message += "\nДлительность задачи больше, чем заданный интервал";
				} catch (NumberFormatException e){
					message += "\nДлительность введена не корректно";
				} 
				if(fromField.getValue().compareTo(toField.getValue()) >= 0) 
					message += "\nДата окончания должна быть больше даты начала";
				
				if(message.equals("")){
					System.out.println("Все введено правильно");
					
					task = new Task(name.getValue());
					task.setDuration(Integer.valueOf(duration.getValue()));
					task.setIntervalAllowed(new Interval(fromField.getValue().getTime(),
							toField.getValue().getTime()));
					
					System.out.println("О! Таска !   " +task);
					
					TaskCreaterWindow.this.close();
				}
				else
					UI.getCurrent().addWindow(new MessageBox(message));
			}
		});
		back.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				TaskCreaterWindow.this.close();
			}
		});
	}
	
	public Task getTask(){
		return task;
	}
}
