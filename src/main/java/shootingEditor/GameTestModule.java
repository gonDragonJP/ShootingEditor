package shootingEditor;

import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import shootingEditor.enemy.EnemyData;
import shootingEditor.stage.Background;
import shootingEditor.stage.StageData;
import shootingEditor.stage.StageManager;
import shootingEditor.vector.Int2Vector;

public class GameTestModule {
	
	private MainApp mainApp;
	
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
		
		this.mainApp = mainApp;
	}
	
	public void setGameStage(int stageNumber){
		
		stageManager.setStage(stageNumber);
		
		mainApp.tableModule.setEventTableData(StageData.eventList);
		mainApp.tableModule.setEnemyTableData(StageData.enemyList);
		
		//AccessOfEventData.addEventList(StageData.eventList);
		//AccessOfEnemyData.addEnemyList(StageData.enemyList); データベース製作用
		
		MainSceneUtil.slider.setMax(StageData.stageEndPoint);
		
		Background.setGraphicsContext(MainSceneUtil.canvas);
		Background.initStage();
	}
	
	public void refreshEventList(){
		
		stageManager.refreshEventList();
	}
	
	public void refreshEnemyList(){
		
		stageManager.refreshEnemyList();
	}
	
	synchronized public void testEnemy(EnemyData enemyData){
		
		MainSceneUtil.startButton.setDisable(true);
		MainSceneUtil.resetButton.setDisable(true);
		
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
		
		double sliderValue = MainSceneUtil.slider.getValue();
		
		stageManager.updateEventIndex(sliderValue);
		stageManager.resetAllEnemies();
	}
	
	synchronized public void updateSlider(){
		
		mainApp.drawModule.drawScreen();
		refreshQueueOfEvent();
	}
	
	synchronized public void pushResetButton(){
		
		MainSceneUtil.slider.setValue(0);
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
			MainSceneUtil.startButton.setDisable(false);
			MainSceneUtil.resetButton.setDisable(false);
		}
	}
	
	public void cancelTimer(){
		
		if(timer != null) timer.cancel();
	}
	
	private void makeTimerTask(){
			
		timerTask = new TimerTask(){
			
			double sliderValue = MainSceneUtil.slider.getValue();
			int scrollMax = StageData.stageEndPoint;
			
			@Override
			synchronized public void run() {
				
				if(isTestMode){
					
					mainApp.drawModule.clearScreen();
				}
				else{
					
					sliderValue += Global.scrollSpeedPerFrame;
					
					if(sliderValue > scrollMax) {
					
						sliderValue = scrollMax;
						this.cancel();
					}
					MainSceneUtil.slider.setValue(sliderValue);
					mainApp.drawModule.drawScreen();
				}
				
				Background.onDraw((int)sliderValue);
				
				stageManager.periodicalProcess(sliderValue, isTestMode);
				stageManager.drawEnemies
					(MainSceneUtil.canvas, MainSceneUtil.checkEnableTex.isSelected());
				
				if(isTestMode){
					
					if(stageManager.getEnemyCount()==0) pushStopButton();
				}
			}
		};
	}
}
