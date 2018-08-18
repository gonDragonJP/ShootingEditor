package shootingEditor;

import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import shootingEditor.enemy.EnemyData;
import shootingEditor.stage.StageData;
import shootingEditor.stage.StageManager;
import shootingEditor.vector.Int2Vector;

public class GameTestModule {
	
	private CallbackOfMainApp cbOfMainApp;
	
	private Canvas canvas;
	private CheckBox checkEnableTex;
	private Slider slider;
	private Button resetButton;
	private Button startButton;
	private Button stopButton;
	private TableView<EnemyData> enemyTable;
	
	private Timer timer;
	private TimerTask timerTask;
	private boolean isTestMode;
	
	private StageManager stageManager = new StageManager(
			
			new CallbackOfMyPlane(){

				@Override
				public Int2Vector getMyPlanePos() {
					
					return new Int2Vector(160,400);
				}

				@Override
				public void setMyPlanePos(Int2Vector requestPos) {
					
				}
			}
	);
	
	public GameTestModule(MainApp mainApp){
		
		cbOfMainApp = mainApp;
		
		this.canvas = mainApp.canvas;
		this.checkEnableTex = mainApp.checkEnableTex;
		this.slider = mainApp.slider;
		this.resetButton = mainApp.resetButton;
		this.startButton = mainApp.startButton;
		this.stopButton = mainApp.stopButton;
		this.enemyTable = mainApp.enemyTable;
	}
	
	public void setGameStage(int stageNumber){
		
		TableModule tableModule = cbOfMainApp.getTableModule();
		
		stageManager.setStage(stageNumber);
		
		tableModule.setEventTableData(StageData.eventList);
		tableModule.setEnemyTableData(StageData.enemyList);
		
		//AccessOfEventData.addEventList(StageData.eventList);
		//AccessOfEnemyData.addEnemyList(StageData.enemyList); データベース製作用
	}
	
	public void refreshEventList(){
		
		stageManager.refreshEventList();
	}
	
	public void refreshEnemyList(){
		
		stageManager.refreshEnemyList();
	}
	
	synchronized public void testEnemy(EnemyData enemyData){
		
		startButton.setDisable(true);
		resetButton.setDisable(true);
		
		stageManager.resetAllEnemies();
		stageManager.addRootEnemy(enemyData);
		
		cancelTimer();
		timer = new Timer();
		makeTimerTask();
		
		isTestMode = true;
		timer.schedule(timerTask, 0, Global.frameIntervalTime);
	}
	
	public void refreshQueueOfEvent(){
		//slider操作時やtableによるpoint変更に伴い呼び出されイベント位置を再設定します
		
		double sliderValue = slider.getValue();
		
		stageManager.updateEventIndex(sliderValue);
		stageManager.resetAllEnemies();
	}
	
	synchronized public void updateSlider(){
		
		DrawModule drawModule = cbOfMainApp.getdrawModule();
		
		drawModule.drawScreen();
		refreshQueueOfEvent();
	}
	
	synchronized public void pushResetButton(){
		
		slider.setValue(0);
		updateSlider();
	}
	
	synchronized public void pushStartButton(){
		
		cancelTimer();
		timer = new Timer();
		makeTimerTask();
		
		isTestMode = false;
		timer.schedule(timerTask, 0, Global.frameIntervalTime);
	}
	
	synchronized public void pushStopButton(){
		
		cancelTimer();
		if(isTestMode){
			
			stageManager.resetAllEnemies();
			startButton.setDisable(false);
			resetButton.setDisable(false);
		}
	}
	
	public void cancelTimer(){
		
		if(timer != null) timer.cancel();
	}
	
	private void makeTimerTask(){
		
		DrawModule drawModule = cbOfMainApp.getdrawModule();
			
		timerTask = new TimerTask(){
			
			double sliderValue = slider.getValue();
			int scrollMax = StageData.stageEndPoint;
			
			@Override
			synchronized public void run() {
				
				if(isTestMode){
					
					drawModule.clearScreen();
				}
				else{
					
					sliderValue += Global.scrollSpeedPerFrame;
					
					if(sliderValue > scrollMax) {
					
						sliderValue = scrollMax;
						this.cancel();
					}
					slider.setValue(sliderValue);
					drawModule.drawScreen();
				}
				
				stageManager.periodicalProcess(sliderValue, isTestMode);
				stageManager.drawEnemies(canvas, checkEnableTex.isSelected());
				
				if(isTestMode){
					
					if(stageManager.getEnemyCount()==0) pushStopButton();
				}
			}
		};
	}
}
