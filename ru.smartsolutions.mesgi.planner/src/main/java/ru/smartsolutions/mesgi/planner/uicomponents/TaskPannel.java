package ru.smartsolutions.mesgi.planner.uicomponents;

import java.util.Map;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextArea;
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
	}

	private void buildInterface(){
		
		this.setSizeFull();
		
//		VerticalLayout для списка задач
		taskPanelVL = new VerticalLayout();
		taskPanelVL.setSizeFull();
		
		//TODO выпилить список всех задач из DataProvider

		buttonHL = new HorizontalLayout();
		buttonHL.setSizeFull();
		addTaskBut = new Button("Добавить");
		removeTaskBut = new Button("Удалить");
		
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

		Tab tab0 = tabSheet.getTab(taskPanel);
		System.out.println("Таб задач  " + tab0);
		System.out.println("Таб задач  " + tabSheet.getTabPosition(tab0));
		
		Tab tab1 = tabSheet.getTab(compTaskPanel);
		System.out.println("Таб выполненных задач  " + tab1);
		System.out.println("Таб выполненных задач  " + tabSheet.getTabPosition(tab1));
		
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
