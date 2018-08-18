package shootingEditor;


import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import shootingEditor.enemy.EnemyData;
import shootingEditor.enemy.EnemyData.EnemyCategory;
import shootingEditor.stage.EventData;
import shootingEditor.stage.EventData.EventCategory;
import shootingEditor.treeView.enemy.TreeEnemyCell;

public class SetScene {
	
	private static MainApp mainApp;
	
	public static void exec(MainApp appArg, Stage stage){
		
		mainApp = appArg;
		
		VBox consoleBox = new VBox();
		consoleBox.setSpacing(20);
		addConsole(consoleBox);
		consoleBox.getChildren().add(mainApp.tabPane);
		
		VBox treeBox = new VBox();
		treeBox.setSpacing(20);
		addTreeConsole(treeBox);
		addTreeView(treeBox);
		
		VBox canvasBox = new VBox();
		canvasBox.setSpacing(10);
		canvasBox.getChildren().addAll(mainApp.canvas, mainApp.checkEnableTex);
		
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
	
	private static void addTreeView(Pane pane){
		
		mainApp.treeView.setPrefWidth(350);
		mainApp.treeView.setPrefHeight(500);
		mainApp.treeView.setEditable(true);
		
		pane.getChildren().add(mainApp.treeView);
	}
	
	private static void addTreeConsole(Pane pane){
		
		HBox box = new HBox();
		box.setPadding(new Insets(0));
		box.setSpacing(20);
		
		addTreeConsoleButtons(box);
		pane.getChildren().add(box);
	}
	
	private static void addTreeConsoleButtons(Pane pane){
		
		TreeModule treeModule = mainApp.getTreeModule();
		TableModule tableModule = mainApp.getTableModule();
		
		mainApp.testEnemyButton.setText("test Enemy");
		mainApp.storeTreeDataButton.setText("store To DB");
	
		mainApp.testEnemyButton.setOnAction
							(event -> treeModule.testTreeEnemy());
		mainApp.storeTreeDataButton.setOnAction(event -> 
			{
				if(tableModule.getTabIndex()==0){
					
					treeModule.storeEventDataToDB();
				}
				else treeModule.storeEnemyDataToDB();
			}
		);
		
		pane.getChildren().addAll
			(mainApp.testEnemyButton, mainApp.storeTreeDataButton);	
	}
	
	private static void addSlider(Pane pane){
		
		mainApp.slider.setOrientation(Orientation.VERTICAL);
		mainApp.slider.setShowTickMarks(true);
		mainApp.slider.setShowTickLabels(true);
		mainApp.slider.setMajorTickUnit(1000);
		
		mainApp.slider.setOnMouseDragged(event -> mainApp.updateSlider());
		
		pane.getChildren().add(mainApp.slider);
	}
	
	private static void addConsole(Pane pane){
		
		HBox box = new HBox();
		box.setPadding(new Insets(0));
		box.setSpacing(20);
		
		addScrollTextField(box);
		addConsoleButtons(box);
		pane.getChildren().add(box);
	}
	
	private static void addScrollTextField(Pane pane){
		
		mainApp.scrollTextField.setEditable(false);
		
		pane.getChildren().add(mainApp.scrollTextField);
	}
	
	private static void addConsoleButtons(Pane pane){
		
		GameTestModule gameTestModule = mainApp.getgameTestModule();
		
		mainApp.resetButton.setText("|<");
		mainApp.startButton.setText(">");
		mainApp.stopButton.setText("stop");
	
		mainApp.resetButton.setOnAction
							(event -> gameTestModule.pushResetButton());
		mainApp.startButton.setOnAction
							(event -> gameTestModule.pushStartButton());
		mainApp.stopButton.setOnAction
							(event -> gameTestModule.pushStopButton());
		
		pane.getChildren().addAll
			(mainApp.resetButton, mainApp.startButton, mainApp.stopButton);	
	}
	
	@SuppressWarnings("unchecked")
	public static void setEventTable(){
		
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
		
		mainApp.eventTable.getColumns().addAll(column1, column2, column3);
		
		mainApp.eventTable.setOnMouseClicked
			(e -> mainApp.getTableModule().onClickedEventTable(e));
	}
	
	@SuppressWarnings("unchecked")
	public static void setEnemyTable(){
		
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
		
		mainApp.enemyTable.getColumns().addAll(column1, column2, column3);
		
		mainApp.enemyTable.setOnMouseClicked
			(e -> mainApp.getTableModule().onClickedEnemyTable(e));
	}

	private static void setTabs(){
		
		mainApp.tabEvent.setText("Event");
		mainApp.tabEnemy.setText("Enemy");
		
		mainApp.tabEvent.setContent(mainApp.eventTable);
		mainApp.tabEnemy.setContent(mainApp.enemyTable);
		
		mainApp.tabEvent.setClosable(false);
		mainApp.tabEnemy.setClosable(false);
		
		mainApp.tabPane.getTabs().addAll(mainApp.tabEvent,mainApp.tabEnemy);
	}
}
