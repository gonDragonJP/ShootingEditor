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
import shootingEditor.MainSceneUtil.TabState;
import shootingEditor.database.AccessOfEnemyData;
import shootingEditor.database.AccessOfEventData;
import shootingEditor.enemy.EnemyData;
import shootingEditor.enemy.EnemyData.EnemyCategory;
import shootingEditor.stage.EventData;
import shootingEditor.stage.StageData;
import shootingEditor.treeView.TreeContent;
import shootingEditor.treeView.enemy.EnemyTreeUtil;

public class TableModule {
	
	private MainApp mainApp;
	
	private EnemyData clipEnemyData = null;
	
	public TableModule(MainApp mainApp){
		
		this.mainApp = mainApp;
	}
	
	public int getTabIndex(){
		
		return MainSceneUtil.tabPane.getSelectionModel().selectedIndexProperty().get();
	}
	
	public void setEventTableData(ArrayList<EventData> list){
		
		ObservableList<EventData> data = FXCollections.observableArrayList();
		data.setAll(list);
		MainSceneUtil.eventTable.itemsProperty().setValue(data);
	}
	
	public void setEnemyTableData(ArrayList<EnemyData> list){
		
		ObservableList<EnemyData> data = FXCollections.observableArrayList();
		data.setAll(list);
		MainSceneUtil.enemyTable.itemsProperty().setValue(data);
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
		
		EventData eventData = MainSceneUtil.eventTable.getSelectionModel().getSelectedItem();
		MainSceneUtil.slider.setValue(eventData.scrollPoint);
		
		mainApp.gameTestModule.updateSlider();
	}
	
	private void setEventToTreeView(){
		
		EventData eventData = MainSceneUtil.eventTable.getSelectionModel().getSelectedItem();
		mainApp.treeModule.setEventTree(eventData);
	}
	
	public void searchObject(){
		
		EventData eventData = MainSceneUtil.eventTable.getSelectionModel().getSelectedItem();
		int objectID = eventData.eventObjectID;	
		int enemyIndex = StageData.getIndexOfEnemyList(objectID);

		if(enemyIndex != -1){
			MainSceneUtil.setTabPane(TabState.enemyTable);
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
		MainSceneUtil.eventTable.setContextMenu(contextMenu);
	}
	
	private void addNewEventData(){
		
		AccessOfEventData.addNewEventData(StageData.stage);
		
		mainApp.gameTestModule.refreshEventList();
		
		setEventTableData(StageData.eventList);
		MainSceneUtil.eventTable.scrollTo(0);
	}
	
	private void deleteEventData(){
		
		EventData eventData = MainSceneUtil.eventTable.getSelectionModel().getSelectedItem();
		AccessOfEventData.deleteEventData(eventData, StageData.stage);
	
		mainApp.gameTestModule.refreshEventList();
		
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
		
		EnemyData enemyData = MainSceneUtil.enemyTable.getSelectionModel().getSelectedItem();
		mainApp.treeModule.setEnemyTree(enemyData);
	}
	private void openEnemyContextMenu(){
		
		ContextMenu contextMenu = new ContextMenu();
		MenuItem[] menuItem  = new MenuItem[4];
		for(int i=0; i<menuItem.length; i++) menuItem[i] = new MenuItem();
		
		String[] menuText = {"Add New", "Delete", "Copy", "Add Copy"};
		
		for(int i=0; i<menuItem.length; i++) menuItem[i].setText(menuText[i]);
		menuItem[0].setOnAction(e ->addNewEnemyData());
		menuItem[1].setOnAction(e ->deleteEnemyData());
		menuItem[2].setOnAction(e->copyToClipEnemyData());
		menuItem[3].setOnAction(e ->addCopyEnemyData());
		
		for(int i=0; i<menuItem.length; i++)
			if(menuText[i] != "Add Copy" || clipEnemyData != null)
				contextMenu.getItems().add(menuItem[i]);
		
		MainSceneUtil.enemyTable.setContextMenu(contextMenu);
	}
	
	private void addNewEnemyData(){
		
		EnemyData selectedData = MainSceneUtil.enemyTable.getSelectionModel().getSelectedItem();
		EnemyCategory newDataCategory = 
					(selectedData == null) ? EnemyCategory.FLYING : selectedData.getCategory();
		
		int newID = AccessOfEnemyData.addNewEnemyData(newDataCategory, StageData.stage);
		
		mainApp.gameTestModule.refreshEnemyList();
		
		setEnemyTableData(StageData.enemyList);
		scrollEnemyTableTo(newID);
	}
	
	public void scrollEnemyTableTo(int objectID){
		
		int enemyIndex = StageData.getIndexOfEnemyList(objectID);
		MainSceneUtil.enemyTable.getSelectionModel().select(enemyIndex);
		MainSceneUtil.enemyTable.scrollTo(enemyIndex);
	}
	
	private void copyToClipEnemyData(){
		
		clipEnemyData = MainSceneUtil.enemyTable.getSelectionModel().getSelectedItem();
	}
	
	private void addCopyEnemyData(){
		
		int newID = AccessOfEnemyData.addCopyEnemyData(clipEnemyData, StageData.stage);
		clipEnemyData = null;
		mainApp.gameTestModule.refreshEnemyList();
		
		setEnemyTableData(StageData.enemyList);
		scrollEnemyTableTo(newID);
	}
	
	private void deleteEnemyData(){
		
		EnemyData enemyData = MainSceneUtil.enemyTable.getSelectionModel().getSelectedItem();
		AccessOfEnemyData.deleteEnemyData(enemyData, StageData.stage);
		
		mainApp.gameTestModule.refreshEnemyList();
		
		setEnemyTableData(StageData.enemyList);
	}
}
