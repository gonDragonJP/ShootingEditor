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
	
	public void initialize(	// ���t���N�V�����ŊȒP�Ƀf�t�H���g�C���X�g���N�^�ɂ�鐶�����s�����߂ɕK�v�ƂȂ����C�j�V�����C�U�ł�
			
			EnemyCommunicable enemyManager
			){
		
		this.enemyManager = enemyManager;
	}
}
