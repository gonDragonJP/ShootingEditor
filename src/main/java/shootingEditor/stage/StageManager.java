package shootingEditor.stage;

import javafx.scene.canvas.Canvas;
import shootingEditor.CallbackOfMyPlane;
import shootingEditor.Global;
import shootingEditor.animation.AnimationManager;
import shootingEditor.enemy.EnemiesManager;
import shootingEditor.enemy.EnemyData;
import shootingEditor.enemy.derivativeType.DerivativeEnemyFactory;
import shootingEditor.vector.Int2Vector;

public class StageManager {
	
	private class CurrentStageState{
		
		public boolean isPreparedScroll, isPreparedStageData;		
		public boolean isStarted;
		
		public int scrollPoint;
		public int eventIndex;
		
		public CurrentStageState(){
			
			initialize();
		}
		
		public void initialize(){
			
			this.isStarted = false;
			this.isPreparedScroll = false;
			this.isPreparedStageData = false; 
			this.scrollPoint = 0;
			this.eventIndex = 0;
		}
	}
	
	private CallbackOfMyPlane cbOfMyPlane;
	private EnemiesManager enemiesManager;
	private AnimationManager animationManager;
	//private StageEffect stageEffect;	

	private CurrentStageState stageState = new CurrentStageState();
	
	public StageManager(CallbackOfMyPlane cbOfMyPlane){
		
		this.cbOfMyPlane = cbOfMyPlane;
		
		initialize();
		
		//stageEffect = new StageEffect(this);	
		//ScrollGraphicManager.setResources(context.getResources());
	}
	
	public void initialize(){
		
		enemiesManager = null;
		stageState.initialize();
		
		//stageEffect.initialize(objectsContainer);
		//ScrollGraphicManager.initialize();
		
		//InitGL.setupFont(
		//		context.getResources(), R.drawable.chrsheet,
		//		16, 16, 0
		//);
	}
	
	public void setStage(int stageNumber){
		
		StageData.initialize(stageNumber);
		stageState.initialize();

		stageState.isPreparedStageData = true;
			
		enemiesManager = new EnemiesManager 
				(cbOfMyPlane, StageData.enemyList, StageData.derivativeEnemyFactory);
	}
	
	public void refreshEventList(){
		
		StageData.refreshEventListFromDB();
	}
	
	public void refreshEnemyList(){
		
		StageData.refreshEnemyListFromDB();
	}
	
	public void resetAllEnemies(){
		
		enemiesManager.resetAllEnemies();
	}
	
	public void addRootEnemy(int objectID){
		
		enemiesManager.addRootEnemy(objectID);
	}
	
	public void addRootEnemy(EnemyData srcData){ // Editor�ɂ�����f�[�^�e�X�g�p�̃��\�b�h�ł�
		
		enemiesManager.addRootEnemy(srcData);
	}
	
	public void explodeTestEnemy(){// Editor�ɂ�����e�X�g�Ώۂ̔����w�߃��\�b�h�ł�
		
		enemiesManager.explodeTestEnemy();
	}
	
	public int getEnemyCount(){
		
		return enemiesManager.getEnemyCount();
	}
	
	public void updateEventIndex(double scrollPoint){
		
		int i =0;
		
		while(i < StageData.eventList.size()){
			
			EventData eventData = StageData.eventList.get(i);
			
			if(eventData.scrollPoint >= scrollPoint) break;
			
			i++;
		}
		
		stageState.eventIndex = i;
	}
	
	synchronized public void drawEnemies(Canvas canvas, boolean isEnableTex){
		
		enemiesManager.onDrawEnemies(canvas, isEnableTex);
	}
	
	synchronized public void periodicalProcess(){ 
		//game���s�p�̃v���Z�X�ł������̂��̂ł�
		
		stageState.scrollPoint += Global.scrollSpeedPerFrame;
		checkStageFinish();
		checkEvent();
		
		enemiesManager.periodicalProcess();
	}
	
	synchronized public void periodicalProcess(double scrollPoint, boolean isTestMode){
		
		if(!isTestMode){
		
			stageState.scrollPoint = (int)scrollPoint;
			checkEvent();
		}
		
		enemiesManager.periodicalProcess();
	}
	
	private void checkEvent(){
		
		while(stageState.eventIndex < StageData.eventList.size()){
			
			EventData eventData = StageData.eventList.get(stageState.eventIndex);
		
			if (eventData.scrollPoint <= stageState.scrollPoint){ 
				
				switch(eventData.eventCategory){
				
				case ENEMYAPPEARANCE:
					addRootEnemy(eventData.eventObjectID);
					break;
					
				case BRIEFING:
					//stageEffect.briefing(eventData.eventObjectID);
					//stageEffect.stageEndPlaneCruising();
					break;
					
				case BOSSAPPEARANCE:
					addRootEnemy(eventData.eventObjectID);
					break;
					
				case PLAYBGM:
					//BGMManager.playBGM(eventData.eventObjectID);
					break;
				}
				
				stageState.eventIndex++;
			}
			else break;	
		};
	}

	private void checkStageFinish(){
	
		if(stageState.scrollPoint > StageData.stageEndPoint){
			
			clearStage();
		}
	}
	
	private void clearStage(){
		
		if(StageData.isLastStage()){
		
			setStage(++StageData.stage);
			
		}else{
			
			clearGame();
		}
	}
	
	private void clearGame(){
		//�����߂�
		
		setStage(1);
	}

	public void prepareStarting(){
		/*
		if(!place.isPreparedScroll){
			ScrollGraphicManager.setupScroll(currentPlace);
		}
		
		if(!place.isPreparedStageData){
			setStageData();
		}
		
		stageEffect.startStageEffect();*/
	}
	
	/*synchronized public void onDraw(GL10 gl){
	
		ScrollGraphicManager.onDraw(place);
		
		tempRect.set(
				0, Global.virtualScreenSize.y, 
				200, Global.virtualScreenSize.y - 40);
		InitGL.drawText(
				tempRect,
				"ListSize:"+enemyGenerator.enemyList.size()
				//"Stage:"+currentPlace.stage+" Scroll:"+currentPlace.scrollPoint
		);
		tempRect.set(0, screenY - 50, 200, screenY - 90);
		InitGL.drawText(
				tempRect,
				"ScreenX:"+screenX+" Y:"+screenY
		);
	}*/
}
