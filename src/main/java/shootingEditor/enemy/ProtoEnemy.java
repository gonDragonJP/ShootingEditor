package shootingEditor.enemy;

import shootingEditor.Global;
import shootingEditor.enemy.EnemyData.EnemyCategory;
import shootingEditor.vector.Int2Vector;
import shootingEditor.vector.IntRect;

public class ProtoEnemy {
	
	protected EnemyData myData;
	protected Enemy parentEnemy;
	
	protected IntRect screenLimit;
	
	public boolean isInScreen, isInExplosion, hasShadow, isGrounder;
	
	public int x, y;
	
	protected int hitPoints, atackPoints;
	protected boolean neededCheckingWithBullet;

	public ProtoEnemy(){
		
		initialize();
	}
	
	private void initialize(){
		
		screenLimit = new IntRect();	
		setScreenLimit();
		
		isInScreen = true;
		isInExplosion = false;
	}
	
	protected void setExplosion(){

        isInExplosion = true;
        hasShadow = false;
    }

    protected  void setOutOfScreen(){

        isInScreen = false;
    }
	
	private void setScreenLimit(){
		
		screenLimit.left  = Global.virtualScreenLimit.left;
		screenLimit.right = Global.virtualScreenLimit.right;
		screenLimit.top   = Global.virtualScreenLimit.top;
		screenLimit.bottom= Global.virtualScreenLimit.bottom;
	}
	
	protected void checkScreenLimit(){
		
		if(	x<screenLimit.left || y<screenLimit.top || 
			x>screenLimit.right || y>screenLimit.bottom
		){
			isInScreen = false;
		}
	}
	
	public void setData(
			
			EnemyData enemyData, 
			Int2Vector requestPos, 
			Enemy parentEnemy
		){
	
		this.myData = enemyData;
		this.parentEnemy = parentEnemy;
		
		EnemyCategory category = EnemyCategory.getFromID(myData.objectID/1000);
		this.hasShadow = (category==EnemyCategory.FLYING);
		this.isGrounder = (category==EnemyCategory.GROUND);
		
		this.hitPoints = myData.hitPoints;
		this.neededCheckingWithBullet = (hitPoints != 0);
	}
	
	public int getObjectID(){
		
		return myData.objectID;
	}
	
	public EnemyCategory getCategory(){
		
		return myData.getCategory();
	}
	
	public Enemy getParentEnemy(){
		
		return parentEnemy;
	}
}
