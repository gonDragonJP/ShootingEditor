package shootingEditor.enemy.derivativeType;

import shootingEditor.CallbackOfMyPlane;
import shootingEditor.Global;
import shootingEditor.enemy.CallbackOfGeneratingChild;
import shootingEditor.enemy.Enemy;
import shootingEditor.enemy.EnemyData;
import shootingEditor.vector.Double2Vector;
import shootingEditor.vector.Int2Vector;


public class DE_Parts extends DerivativeEnemy{
	
	Double2Vector partsProportion = new Double2Vector();
	Int2Vector tempChildPosition = new Int2Vector();

	@Override
	public void setData(	
			
				EnemyData enemyData, 
				Int2Vector requestPos, 
				Enemy parentEnemy
			){
		
		super.setData(enemyData, requestPos, parentEnemy);
		
		partsProportion.x = enemyData.startPosition.x / 100f;
		partsProportion.y = enemyData.startPosition.y / 100f;
		
		setPartsPosition();
		
		fx = x;		fy = y;
	}

	@Override
	protected void variableProcess(){
		
		checkPartsParameter();
	}
		
	private void checkPartsParameter(){
		
		if(parentEnemy.isInScreen == false){
			
			isInScreen = false;
			return;
		}
		
		if(parentEnemy.isInExplosion == true){
			
			setExplosion();
			return;
		}
		
		setPartsPosition();
		
		velocity.copy(parentEnemy.velocity); // 自分の移動では使わないが生成オブジェクトで必要になる事あり
	}
	
	private void setPartsPosition(){
		
		Double2Vector drawSize = parentEnemy.getDrawSizeOfNormalAnime();
	
		int relativeX = (int)(drawSize.x * partsProportion.x);
		int relativeY = (int)(drawSize.y * partsProportion.y);
	
		double c = Math.cos(parentEnemy.drawAngle * Global.radian);
		double s = Math.sin(parentEnemy.drawAngle * Global.radian);
	
		x = parentEnemy.x + (int)(relativeX * c - relativeY * s);
		y = parentEnemy.y + (int)(relativeX * s + relativeY * c);
		
	}
}
