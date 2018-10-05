package shootingEditor;

import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import shootingEditor.enemy.EnemyData;
import shootingEditor.stage.EventData;
import shootingEditor.stage.StageData;
import shootingEditor.treeView.TreeContent;


public class MainApp extends Application{
	
	private static final int WinX = 1280;
	private static final int WinY = 640;
	
	public DrawModule drawModule;
	public TableModule tableModule;
	public TreeModule treeModule;
	public GameTestModule gameTestModule;
	
	public static void main(String[] args){
		
		Application.launch(args);
	}
	
	//static GeneratingTest test = new GeneratingTest();

	@Override
	public void start(Stage stage) throws Exception {
		
		drawModule = new DrawModule(this);
		tableModule = new TableModule(this);
		treeModule = new TreeModule(this);
		gameTestModule = new GameTestModule(this);
		
		initStage(stage);
	}
	
	private void initStage(Stage stage){
		
		MainSceneUtil.setScene(this, stage);
	
		stage.setTitle("ShootingEditor ver1.0");
		stage.setWidth(WinX);
		stage.setHeight(WinY);
		stage.setOnCloseRequest(event->{gameTestModule.cancelTimer();});
		stage.show();
		
		drawModule.drawScreen();
	}
}
