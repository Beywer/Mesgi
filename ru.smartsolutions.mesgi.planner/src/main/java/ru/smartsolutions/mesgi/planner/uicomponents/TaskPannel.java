package ru.smartsolutions.mesgi.planner.uicomponents;

import java.util.Map;

import ru.smartsolutions.mesgi.model.Task;

import com.vaadin.server.ClientConnector.DetachEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class TaskPannel extends VerticalLayout {
	
	private VerticalLayout taskPanelVL;
	private VerticalLayout complTaskPanelVL;
	private TabSheet tabSheet;
	
	private TextArea taskDescription;
	private TextArea complTaskDescription;
	
	private Map<String, Button> tasks;
	private Map<String, Button> compTasks;
	
	private Button addTaskBut;
	private Button removeTaskBut;
	private HorizontalLayout buttonHL;

	public TaskPannel() {
		super();
		buildInterface();	
		createListeners();
	}
	
	private void createListeners() {
		
//		переключение описаний вместе с табшитом
		tabSheet.addSelectedTabChangeListener(new SelectedTabChangeListener() {
			
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				Component tabComponet = event.getTabSheet().getSelectedTab();
				Tab tab = event.getTabSheet().getTab(tabComponet);
				int index = event.getTabSheet().getTabPosition(tab);
				
				if(index == 0){
					//поменять результат на описание заявки
					TaskPannel.this.removeComponent(complTaskDescription);
					TaskPannel.this.addComponent(taskDescription);
					//восстановить размеры
					TaskPannel.this.setExpandRatio(tabSheet, 5);
					TaskPannel.this.setExpandRatio(taskDescription, 3);
				}else{
					//поменять описание на результат
					TaskPannel.this.removeComponent(taskDescription);
					TaskPannel.this.addComponent(complTaskDescription);
					//восстановить размеры
					TaskPannel.this.setExpandRatio(tabSheet, 5);
					TaskPannel.this.setExpandRatio(complTaskDescription, 3);
				}
			}
		});
		
//		добавлиена задачи
		addTaskBut.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				//окно создания задачи
				final TaskCreaterWindow taskCreater = new TaskCreaterWindow();
				//дейтсвия по закрытию
				taskCreater.addDetachListener(new DetachListener() {
					
					@Override
					public void detach(DetachEvent event) {
						DataProvider.addTask(taskCreater.getTask());
					}
				});
				
				UI.getCurrent().addWindow(taskCreater);
				
			}
		});
	}

	public void addTask(Task task){

//		создается кнопка
		Button taskButton = new Button(task.getName());
		taskButton.setWidth(100, Unit.PERCENTAGE);
		
//		выбор цвета в зависимости от заплаинрованности задачи
		if(task.getPlannedInterval() == null) taskButton.setStyleName("available-device");
		else taskButton.setStyleName("unavailable-device");

//		создание и отображение информации
		final StringBuilder sb = new StringBuilder();
		sb.append("Наименование: " + task.getName());
		sb.append("Id: " + task.getId());
		
		taskButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				taskDescription.setReadOnly(false);
				taskDescription.setValue(sb.toString());
				taskDescription.setReadOnly(true);
			}
		});
		
//		добаление на форму
		taskPanelVL.addComponent(taskButton);
		
//		обновления родителя
		HasComponents component =  getParent();
		if(component instanceof MainComponent){
			((MainComponent)component).updateUI();
		}
		
	}
	
	private void buildInterface(){
		
		this.setSizeFull();
		
//		VerticalLayout для списка задач
		taskPanelVL = new VerticalLayout();
		taskPanelVL.setWidth(100, Unit.PERCENTAGE);
		
		//TODO выпилить список всех задач из DataProvider

		//создание кнопок
		addTaskBut = new Button("Добавить");
		addTaskBut.setWidth(100,Unit.PERCENTAGE);
		addTaskBut.setStyleName("friendly");
		removeTaskBut = new Button("Удалить");
		removeTaskBut.setWidth(100,Unit.PERCENTAGE);
		removeTaskBut.setStyleName("danger");
		//расположить кнопки горизонтально
		buttonHL = new HorizontalLayout();
		buttonHL.setSizeFull();
		buttonHL.setSpacing(true);
		buttonHL.addComponent(addTaskBut);
		buttonHL.addComponent(removeTaskBut);
		//расположить кнопки над задачами
		taskPanelVL.addComponent(buttonHL);
		
		//Панель заявок
		Panel taskPanel = new Panel();
		taskPanel.setSizeFull();
		taskPanel.setContent(taskPanelVL);
		
		
//		VerticalLayout для выполнненных задач
		complTaskPanelVL = new VerticalLayout();
		complTaskPanelVL.setSizeFull();

		//Панель выполненных задач
		Panel compTaskPanel = new Panel();
		compTaskPanel.setSizeFull();
		compTaskPanel.setContent(complTaskPanelVL);
		
//		Для переключения между задчами-заявками и выполненными
		tabSheet = new TabSheet();
		tabSheet.setSizeFull();
		tabSheet.addTab(taskPanel, "Задачи");
		tabSheet.addTab(compTaskPanel, "Выполненные задачи");
		
//		описание заявки
		taskDescription = new TextArea("Описание задачи");
		taskDescription.setSizeFull();
		taskDescription.setReadOnly(true);
		
//		описание выполненной задачи
		complTaskDescription = new TextArea("Результат задачи");
		complTaskDescription.setSizeFull();
		complTaskDescription.setReadOnly(true);
		
//		расположение табшита над описаниями
		this.addComponent(tabSheet);
		this.addComponent(taskDescription);
		
		this.setExpandRatio(tabSheet, 5);
		this.setExpandRatio(taskDescription, 3);
	}
}
