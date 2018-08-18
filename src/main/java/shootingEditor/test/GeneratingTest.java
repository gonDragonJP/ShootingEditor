package shootingEditor.test;
import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import shootingEditor.CallbackOfMyPlane;
import shootingEditor.enemy.EnemiesManager;
import shootingEditor.enemy.EnemyData;
import shootingEditor.stage.EventData;
import shootingEditor.stage.FileAccess;
import shootingEditor.vector.Int2Vector;

public class GeneratingTest {
	
	FileAccess fileAccess;
	ArrayList<EventData> eventList;
	ArrayList<EnemyData> enemyList;
	EnemiesManager enemiesManager;
	
	public GeneratingTest(){
		
		fileAccess = new FileAccess();
		eventList = new ArrayList<EventData>();
		enemyList = new ArrayList<EnemyData>();
		enemiesManager = new EnemiesManager(
				
				new CallbackOfMyPlane(){

					@Override
					public Int2Vector getMyPlanePos() {
						
						return new Int2Vector(160,400);
					}

					@Override
					public void setMyPlanePos(Int2Vector requestPos) {
						
					}
				}, 
				enemyList, null
		);
	}
	
	public void initialize(){
	
	}
	
	public void loadData(int stageNumber){
		
		fileAccess.setEventList(eventList, stageNumber);
		fileAccess.setEnemyList(enemyList, stageNumber);
	}
	
	public void addEnemy(int objectID){
		
		enemiesManager.addRootEnemy(objectID);
	}
	
	public void drawEnemies(Canvas canvas, boolean isEnableTex){
		
		enemiesManager.onDrawEnemies(canvas, isEnableTex);
	}
	
	public void periodicalProcess(){
		
		enemiesManager.periodicalProcess();
	}

	public void resetAllEnemies(){
		
		enemiesManager.resetAllEnemies();
	}
}
