package shootingEditor;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import shootingEditor.database.AccessOfEnemyData;
import shootingEditor.database.AccessOfEventData;
import shootingEditor.enemy.EnemyData;
import shootingEditor.enemy.EnemyData.EnemyCategory;
import shootingEditor.stage.EventData;
import shootingEditor.stage.StageData;
import shootingEditor.treeView.TreeContent;
import shootingEditor.treeView.enemy.EnemyTreeUtil;

public class TableModule {
	
	private CallbackOfMainApp cbOfMainApp;
	
	private TableView<EventData> eventTable;
	private TableView<EnemyData> enemyTable;
	private Tab tabEnemy;
	private TabPane tabPane;
	private Slider slider;
	
	public TableModule(MainApp mainApp){
		
		cbOfMainApp = mainApp;
	
		this.eventTable = mainApp.eventTable;
		this.enemyTable = mainApp.enemyTable;
		this.tabEnemy = mainApp.tabEnemy;
		this.tabPane = mainApp.tabPane;
		this.slider = mainApp.slider;
	}
	
	public int getTabIndex(){
		
		return tabPane.getSelectionModel().selectedIndexProperty().get();
	}
	
	public void setEventTableData(ArrayList<EventData> list){
		
		ObservableList<EventData> data = FXCollections.observableArrayList();
		data.setAll(list);
		eventTable.itemsProperty().setValue(data);
	}
	
	public void setEnemyTableData(ArrayList<EnemyData> list){
		
		ObservableList<EnemyData> data = FXCollections.observableArrayList();
		data.setAll(list);
		enemyTable.itemsProperty().setValue(data);
	}
	
	public void onClickedEventTable(MouseEvent e){
		
		if(e.getButton() == MouseButton.PRIMARY){
		
			if(e.getClickCount() == 1) {
				movePosition();
				setEventToTreeView();
			}
			if(e.getClickCount() == 2) searchObject();
		}
		
		if(e.getButton() == MouseButton.SECONDARY){
		
			if(e.getClickCount() == 1) openEventContextMenu();
		}
	}
	
	public void movePosition(){
		
		GameTestModule gameTestModule = cbOfMainApp.getgameTestModule();
		
		EventData eventData = eventTable.getSelectionModel().getSelectedItem();
		slider.setValue(eventData.scrollPoint);
		
		gameTestModule.updateSlider();
	}
	
	private void setEventToTreeView(){
		
		EventData eventData = eventTable.getSelectionModel().getSelectedItem();
		cbOfMainApp.getTreeModule().setEventTree(eventData);
	}
	
	public void searchObject(){
		
		EventData eventData = eventTable.getSelectionModel().getSelectedItem();
		int objectID = eventData.eventObjectID;	
		int enemyIndex = StageData.getIndexOfEnemyList(objectID);

		if(enemyIndex != -1){
			tabPane.getSelectionModel().select(tabEnemy);
			scrollEnemyTableTo(objectID);
			setEnemyToTreeView();
		}
	}
	
	private void openEventContextMenu(){
		
		ContextMenu contextMenu = new ContextMenu();
		MenuItem[] menuItem  = new MenuItem[2];
		for(int i=0; i<menuItem.length; i++) menuItem[i] = new MenuItem();
		
		String[] menuText = {"add", "delete"};
		
		for(int i=0; i<menuItem.length; i++) menuItem[i].setText(menuText[i]);
		menuItem[0].setOnAction(e ->{addNewEventData();});
		menuItem[1].setOnAction(e ->{deleteEventData();});
	
		contextMenu.getItems().addAll(menuItem);
		eventTable.setContextMenu(contextMenu);
	}
	
	private void addNewEventData(){
		
		AccessOfEventData.addNewEventData();
		
		GameTestModule gameTestModule = cbOfMainApp.getgameTestModule();
		gameTestModule.refreshEventList();
		
		setEventTableData(StageData.eventList);
		eventTable.scrollTo(0);
	}
	
	private void deleteEventData(){
		
		EventData eventData = eventTable.getSelectionModel().getSelectedItem();
		AccessOfEventData.deleteEventData(eventData);
		
		GameTestModule gameTestModule = cbOfMainApp.getgameTestModule();
		gameTestModule.refreshEventList();
		
		setEventTableData(StageData.eventList);
	}
	
	public void onClickedEnemyTable(MouseEvent e){
	
		if(e.getButton() == MouseButton.PRIMARY){
			
			if(e.getClickCount() == 1) setEnemyToTreeView();
		}
		
		if(e.getButton() == MouseButton.SECONDARY){
		
			if(e.getClickCount() == 1) openEnemyContextMenu();
		}
	}
	
	public void setEnemyToTreeView(){
		
		EnemyData enemyData = enemyTable.getSelectionModel().getSelectedItem();
		cbOfMainApp.getTreeModule().setEnemyTree(enemyData);
	}
	private void openEnemyContextMenu(){
		
		ContextMenu contextMenu = new ContextMenu();
		MenuItem[] menuItem  = new MenuItem[3];
		for(int i=0; i<menuItem.length; i++) menuItem[i] = new MenuItem();
		
		String[] menuText = {"add", "delete", "add copy"};
		
		for(int i=0; i<menuItem.length; i++) menuItem[i].setText(menuText[i]);
		menuItem[0].setOnAction(e ->{addNewEnemyData();});
		menuItem[1].setOnAction(e ->{deleteEnemyData();});
		menuItem[2].setOnAction(e ->{addCopyEnemyData();});
	
		contextMenu.getItems().addAll(menuItem);
		enemyTable.setContextMenu(contextMenu);
	}
	
	private void addNewEnemyData(){
		
		EnemyData selectedData = enemyTable.getSelectionModel().getSelectedItem();
		EnemyCategory newDataCategory = 
					(selectedData == null) ? EnemyCategory.FLYING : selectedData.getCategory();
		
		int newID = AccessOfEnemyData.addNewEnemyData(newDataCategory);
		
		GameTestModule gameTestModule = cbOfMainApp.getgameTestModule();
		gameTestModule.refreshEnemyList();
		
		setEnemyTableData(StageData.enemyList);
		scrollEnemyTableTo(newID);
	}
	
	public void scrollEnemyTableTo(int objectID){
		
		int enemyIndex = StageData.getIndexOfEnemyList(objectID);
		enemyTable.getSelectionModel().select(enemyIndex);
		enemyTable.scrollTo(enemyIndex);
	}
	
	private void addCopyEnemyData(){
		
		EnemyData enemyData = enemyTable.getSelectionModel().getSelectedItem();
		int newID = AccessOfEnemyData.addCopyEnemyData(enemyData);
		
		GameTestModule gameTestModule = cbOfMainApp.getgameTestModule();
		gameTestModule.refreshEnemyList();
		
		setEnemyTableData(StageData.enemyList);
		scrollEnemyTableTo(newID);
	}
	
	private void deleteEnemyData(){
		
		EnemyData enemyData = enemyTable.getSelectionModel().getSelectedItem();
		AccessOfEnemyData.deleteEnemyData(enemyData);
		
		GameTestModule gameTestModule = cbOfMainApp.getgameTestModule();
		gameTestModule.refreshEnemyList();
		
		setEnemyTableData(StageData.enemyList);
	}
}
