package shootingEditor.enemy.derivativeType;

import shootingEditor.CallbackOfMyPlane;
import shootingEditor.animation.AnimationManager;
import shootingEditor.enemy.EnemyCommunicable;
import shootingEditor.enemy.Enemy;
import shootingEditor.enemy.EnemyData;
import shootingEditor.enemy.GeneratingChild;
import shootingEditor.enemy.MovingNode;
import shootingEditor.vector.Int2Vector;

	public class DerivativeEnemy extends Enemy{

	public DerivativeEnemy(){
		
		super(null);
	}
	
	public void initialize(	// リフレクションで簡単にデフォルトインストラクタによる生成を行うために必要となったイニシャライザです
			
			EnemyCommunicable enemyManager
			){
		
		this.enemyManager = enemyManager;
	}
}
