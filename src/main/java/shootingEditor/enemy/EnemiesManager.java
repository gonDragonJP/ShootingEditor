package shootingEditor.enemy;


import java.util.ArrayList;
import java.util.Iterator;

import javafx.scene.canvas.Canvas;
import shootingEditor.CallbackOfMyPlane;
import shootingEditor.enemy.EnemyData.EnemyCategory;
import shootingEditor.enemy.derivativeType.DerivativeEnemyFactory;
import shootingEditor.vector.Int2Vector;

public class EnemiesManager implements EnemyCommunicable{
	
	private DerivativeEnemyFactory derivativeEnemyFactory;
	private CallbackOfMyPlane cbOfMyPlanePos;
	
	ArrayList<EnemyData> enemyDataList;
	ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
	
	public EnemiesManager(
			
			CallbackOfMyPlane cbOfMyPlanePos,
			ArrayList<EnemyData> enemyDataList,
			DerivativeEnemyFactory derivativeEnemyFactory
			){	
		
		this.cbOfMyPlanePos = cbOfMyPlanePos;
		
		this.enemyDataList = enemyDataList;
		this.derivativeEnemyFactory = derivativeEnemyFactory;
	}
	
	public void initialize(){
		
	}
	
	public void resetAllEnemies(){
		
		enemyList.clear();
	}
	
	public int getEnemyCount(){
		
		return enemyList.size();
	}
	
	@Override
	public Enemy getGeneratingChild(Enemy parent) {
		
		return addChildEnemy(parent);
	}

	@Override
	public void generateExplosiveObject(Enemy parent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Int2Vector getMyPlanePos() {
		
		return cbOfMyPlanePos.getMyPlanePos();
	}
	

	@Override
	public void setMyPlanePos(Int2Vector myPlanePos) {
		
		cbOfMyPlanePos.setMyPlanePos(myPlanePos);
	}
	
	/*synchronized public void onDrawShadow(GL10 gl){
		
		int j = list.size();
		for(int i=0; i<j; i++){
			
			list.get(i).onDrawShadow(gl);
		}
	}*/
	
	synchronized public void onDrawEnemies(Canvas canvas, boolean isEnableTex){
		
		//System.out.println("List Size : " + String.valueOf(enemyList.size()));
		
		EnemyDrawer.setContext(canvas);
		EnemyDrawer.setEnableTex(isEnableTex);
		
		for(Enemy e : enemyList){
			
			EnemyDrawer.onDrawIfGrounder(e);
		}
		
		for(Enemy e : enemyList){
			
			EnemyDrawer.onDrawIfAir(e);
		}
	}
	
	synchronized public void periodicalProcess(){
			
		enemiesPeriodicalProcess();
		checkPositionLimit();
	}
	
	private void enemiesPeriodicalProcess()
	{	//ループの途中でリストサイズが変わるためコレクションループは使えません(生成で増えます)
		
		int size = enemyList.size();
		
		for(int i=0; i<size; i++){
			
			enemyList.get(i).periodicalProcess();
		}
	}
	
	private void checkPositionLimit(){
		
		Iterator<Enemy> it = enemyList.iterator();
		while(it.hasNext()) if(!it.next().isInScreen) it.remove();
	}
	
	public void addRootEnemy(int objectID){
		
		EnemyData srcData = getEnemyDataFromObjectID(objectID);
		
		try {
			
			if (srcData == null) throw new Exception();
			
		} catch (Exception e) {
				
			System.out.println("Listに該当objectIDが見つかりません");		
			return;
		}
		generateEnemy(srcData, null, null);
	}
	
	public void addRootEnemy(EnemyData srcData){ // Editorにおけるデータテスト用のメソッドです
		
		generateEnemy(srcData, null, null);
	}
	
	public void explodeTestEnemy(){ // Editorにおける単独対象の爆破指令です
		
		enemyList.get(0).setExplosion();
	}
	
	private Enemy addChildEnemy(Enemy parent){
		
		GeneratingChild gen = parent.myData.generator.get(parent.genIndex);
		int objectID = gen.objectID;
		
		EnemyData srcData = getEnemyDataFromObjectID(objectID);
		
		Int2Vector startPos = new Int2Vector(gen.centerX, gen.centerY);
		
		return generateEnemy(srcData, startPos, parent);
	}
	
	private Enemy generateEnemy(
			
				EnemyData enemyData,
				Int2Vector requestStartPos, 
				Enemy parentEnemy
			){
		
		Enemy enemy;
		
		if(enemyData.isDerivativeType){
			
			EnemyCategory category = enemyData.getCategory();
			
			enemy = derivativeEnemyFactory.getDerivativeEnemy(enemyData.name, category, this);
		}
		else{
			
			enemy = new Enemy(this);
		}
		
		enemy.setData(enemyData, requestStartPos, parentEnemy);
		enemyList.add(enemy);
		
		return enemy;
	}
	
	private EnemyData getEnemyDataFromObjectID(int objectID){
		
		EnemyData result = null;
		
		for(EnemyData e : enemyDataList){
			
			if(e.objectID == objectID){
				result = e;
				break;
			}
		}
		return result;
	}

}
