package shootingEditor;

import java.util.logging.Logger;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import shootingEditor.enemy.EnemyData;
import shootingEditor.enemy.EnemyData.EnemyCategory;
import shootingEditor.stage.EventData;
import shootingEditor.stage.EventData.EventCategory;
import shootingEditor.treeView.TreeContent;
import shootingEditor.treeView.enemy.TreeEnemyCell;
import sun.rmi.runtime.Log;

public class MainSceneUtil {
	
	public static final int CanvasX = 320;
	public static final int CanvasY = 500;
	
	public static final int stageNumber = 5;
	
	public static Canvas canvas = new Canvas(CanvasX, CanvasY);
	public static CheckBox checkEnableTex = new CheckBox("Enable Texture");
	public static CheckBox checkEnableBG = new CheckBox("Show Background");
	public static Slider slider = new Slider(0,0,0);
	
	public static ChoiceBox<String> stageChoiceBox = new ChoiceBox<>();
	public static TextField scrollTextField = new TextField();
	public static Button resetButton = new Button();
	public static Button startButton = new Button();
	public static Button stopButton = new Button();
	public static Button testEnemyButton = new Button();
	public static Button storeTreeDataButton = new Button();
	
	public static TableView<EventData> eventTable = new TableView<>();
	public static TableView<EnemyData> enemyTable = new TableView<>();
	public static TreeView<TreeContent> treeView = new TreeView<>();
	public static Tab tabEvent = new Tab();
	public static Tab tabEnemy = new Tab();
	public static TabPane tabPane = new TabPane();
	public static Alert alertBox = new Alert(Alert.AlertType.CONFIRMATION);
	
	private static MainApp mainApp;
	
	public static void setScene(MainApp appArg, Stage stage){
		
		mainApp = appArg;
		
		VBox consoleBox = new VBox();
		consoleBox.setSpacing(20);
		addConsole(consoleBox);
		consoleBox.getChildren().add(tabPane);
		
		VBox treeBox = new VBox();
		treeBox.setSpacing(20);
		addTreeConsole(treeBox);
		addTreeView(treeBox);
		
		VBox canvasBox = new VBox();
		canvasBox.setSpacing(10);
		canvasBox.getChildren().addAll(canvas, checkEnableTex, checkEnableBG);
		
		HBox box = new HBox();
		box.setPadding(new Insets(30));
		box.setSpacing(50);
		
		box.getChildren().add(canvasBox);
		addSlider(box);
		box.getChildren().addAll(consoleBox, treeBox);
		
		Pane root = box;
		Scene scene = new Scene(root);	
		stage.setScene(scene);
		
		setEventTable();
		setEnemyTable();
		setTabs();
	}
	
	public enum TabState {enemyTable, eventTable}
	
	public static void setTabPane(TabState state) {
		
		Tab tab = tabEvent;
		
		switch(state) {
		case enemyTable: tab = tabEnemy; break;
		case eventTable: tab = tabEvent; break;
		}
		tabPane.getSelectionModel().select(tab);
	}
	
	private static void addTreeView(Pane pane){
		
		treeView.setPrefWidth(350);
		treeView.setPrefHeight(500);
		treeView.setEditable(true);
		
		pane.getChildren().add(treeView);
	}
	
	private static void addTreeConsole(Pane pane){
		
		HBox box = new HBox();
		box.setPadding(new Insets(0));
		box.setSpacing(20);
		
		addTreeConsoleButtons(box);
		pane.getChildren().add(box);
	}
	
	private static void addTreeConsoleButtons(Pane pane){
		
		testEnemyButton.setText("test Enemy");
		storeTreeDataButton.setText("store To DB");
	
		testEnemyButton.setOnAction
							(event -> mainApp.treeModule.testTreeEnemy());
		storeTreeDataButton.setOnAction(event -> 
			{
				if(mainApp.tableModule.getTabIndex()==0){
					
					mainApp.treeModule.storeEventDataToDB();
				}
				else mainApp.treeModule.storeEnemyDataToDB();
			}
		);
		
		pane.getChildren().addAll(testEnemyButton, storeTreeDataButton);	
	}
	
	private static void addSlider(Pane pane){
		
		slider.setOrientation(Orientation.VERTICAL);
		slider.setShowTickMarks(true);
		slider.setShowTickLabels(true);
		slider.setMajorTickUnit(1000);
		slider.setOnMouseDragged(event -> updateSlider());
		
		pane.getChildren().add(slider);
	}
	
	synchronized static private void updateSlider(){
		
		mainApp.drawModule.drawScreen();
		mainApp.gameTestModule.refreshQueueOfEvent();
	}
	
	private static void addConsole(Pane pane){
		
		VBox box = new VBox();
		box.setPadding(new Insets(0));
		box.setSpacing(20);
		
		addStageChoiceBox(box);
		
		HBox box2 = new HBox();
		box2.setPadding(new Insets(0));
		box2.setSpacing(20);
		
		addScrollTextField(box2);
		addConsoleButtons(box2);
		
		box.getChildren().add(box2);
		pane.getChildren().add(box);
	}
	
	private static void addStageChoiceBox(Pane pane){
		
		for(int i=0; i<stageNumber; i++){
			
			stageChoiceBox.getItems().add(
					"stage_"+String.valueOf(i+1)
					);
		}
		
		stageChoiceBox.getSelectionModel().selectedItemProperty().addListener(
				new ChangeListener<String>(){

					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue,
							String newValue) {
						
						int stage = Integer.valueOf(newValue.substring(newValue.length()-1));
						mainApp.gameTestModule.setGameStage(stage);
					}}
				);
		
		stageChoiceBox.getSelectionModel().select(0);
		
		pane.getChildren().add(stageChoiceBox);
	}
	
	private static void addScrollTextField(Pane pane){
		
		scrollTextField.setEditable(false);
		
		pane.getChildren().add(scrollTextField);
	}
	
	private static void addConsoleButtons(Pane pane){
		
		resetButton.setText("|<");
		startButton.setText(">");
		stopButton.setText("stop");
	
		resetButton.setOnAction
							(event -> mainApp.gameTestModule.pushResetButton());
		startButton.setOnAction
							(event -> mainApp.gameTestModule.pushStartButton());
		stopButton.setOnAction
							(event -> mainApp.gameTestModule.pushStopButton());
		
		pane.getChildren().addAll(resetButton, startButton, stopButton);	
	}
	
	@SuppressWarnings("unchecked")
	private static void setEventTable(){
		
		TableColumn<EventData, String> column1 = new TableColumn<>("Position");
		column1.setCellValueFactory
			(param -> {
				int scrollPoint = param.getValue().scrollPoint;
				return new SimpleStringProperty(String.valueOf(scrollPoint));
			});
		
		TableColumn<EventData, String> column2 = new TableColumn<>("Category");
		column2.setCellValueFactory
		(param -> {
			EventCategory category = param.getValue().eventCategory;
			return new SimpleStringProperty(category.toString());
		});
		
		TableColumn<EventData, String> column3 = new TableColumn<>("ObjectID");
		column3.setCellValueFactory
		(param -> {
			int objectID = param.getValue().eventObjectID;
			return new SimpleStringProperty(String.valueOf(objectID));
		});
		
		column1.setPrefWidth(60);
		column2.setPrefWidth(130);
		column3.setPrefWidth(60);
		
		eventTable.getColumns().addAll(column1, column2, column3);
		
		eventTable.setOnMouseClicked
			(e -> mainApp.tableModule.onClickedEventTable(e));
	}
	
	@SuppressWarnings("unchecked")
	private static void setEnemyTable(){
		
		TableColumn<EnemyData, String> column1 = new TableColumn<>("ObjectID");
		column1.setCellValueFactory
			(param -> {
				int scrollPoint = param.getValue().objectID;
				return new SimpleStringProperty(String.valueOf(scrollPoint));
			});
		
		TableColumn<EnemyData, String> column2 = new TableColumn<>("Name");
		column2.setCellValueFactory
		(param -> {
			String name = param.getValue().name;
			return new SimpleStringProperty(name);
		});
		
		TableColumn<EnemyData, String> column3 = new TableColumn<>("Category");
		column3.setCellValueFactory
		(param -> {
			EnemyCategory category = param.getValue().getCategory();
			return new SimpleStringProperty(category.toString());
		});
		column1.setPrefWidth(60);
		column2.setPrefWidth(120);
		column3.setPrefWidth(100);
		
		enemyTable.getColumns().addAll(column1, column2, column3);
		
		enemyTable.setOnMouseClicked
			(e -> mainApp.tableModule.onClickedEnemyTable(e));
	}

	private static void setTabs(){
		
		tabEvent.setText("Event");
		tabEnemy.setText("Enemy");
		
		tabEvent.setContent(eventTable);
		tabEnemy.setContent(enemyTable);
		
		tabEvent.setClosable(false);
		tabEnemy.setClosable(false);
		
		tabPane.getTabs().addAll(tabEvent,tabEnemy);
	}
}
