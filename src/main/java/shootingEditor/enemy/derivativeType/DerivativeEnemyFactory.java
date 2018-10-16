package shootingEditor.enemy.derivativeType;

import java.util.Hashtable;
import java.util.Map;

import shootingEditor.CallbackOfMyPlane;
import shootingEditor.enemy.EnemyCommunicable;
import shootingEditor.enemy.Enemy;
import shootingEditor.enemy.EnemyData.EnemyCategory;

public class DerivativeEnemyFactory {

	private Map<String, DerivativeEnemyKind> nameToDEKindMap
	  		= new Hashtable<String, DerivativeEnemyKind>();
	
	public DerivativeEnemyFactory(int stage){
		
		initialize(stage);
	}
	
	private void initialize(int stage){
	
		nameToDEKindMap.clear();
		setMap(stage); 				// �f�[�^�x�[�X�����镔��
	}
	
	private void setMap(int stage){
		
		switch(stage){
		
		case 1: setStage1DE(); break;
		case 2: setStage2DE(); break;
		default:
		}
	}
	
	public Enemy getDerivativeEnemy(
			
			String enemyName, EnemyCategory category,
			EnemyCommunicable enemyManager
		){
		
		DerivativeEnemy derivativeEnemy;
		
		if (category == EnemyCategory.PARTS) 
				// parts�����͔h���^�Ƃ��ď������Ă�@�p�[�c���ɈႤ�������l����Ȃ瑼�Ɠ��l�̏������l����ׂ�
			
			derivativeEnemy = new DE_Parts();
		
		else{
			
			String className = nameToDEKindMap.get(enemyName).getClassName();
			className = "shootingEditor.enemy.derivativeType." + className;
			derivativeEnemy = generateEnemyByReflection(className);
			
		}
		
		derivativeEnemy.initialize(enemyManager);
		// ���t���N�V�����ŊȒP�Ƀf�t�H���g�C���X�g���N�^�ɂ�鐶�����s���Ă��邽�ߌォ��C�j�V�����C�U�Őݒ肵�Ă��܂�
		
		return (Enemy)derivativeEnemy;
	}
	
	private DerivativeEnemy generateEnemyByReflection(String enemyName){
		
		DerivativeEnemy derivativeEnemy = null;
		
		try {
			Class<?> clazz = Class.forName(enemyName);
			derivativeEnemy = (DerivativeEnemy)clazz.newInstance();
			
		} catch (ReflectiveOperationException e) {
			
			e.printStackTrace();
		}
		
		return derivativeEnemy;
	}
	
	private enum DerivativeEnemyKind {
		
		BLOCKER_TYPE0		("DE_blocker_Type0"), 
		BOMB_GAS			("DE_bomb_gas"), 
		LASER_TYPE0			("DE_laser_Type0"), 
		MAKE_SCATTER_TYPE0	("DE_make_scatter_Type0"), 
		CARRIER_BOSS		("DE_carrier_Boss"),
		HARRICANE			("DE_harricane"), 
		MINE_TYPE0			("DE_mine_Type0");
	
		private String className;
		
		DerivativeEnemyKind(String className){
			
			this.className = className;
		}
		
		public String getClassName(){
			
			return className;
		}
	};
	
	private void setStage1DE(){
		
		nameToDEKindMap.put("blocker0", DerivativeEnemyKind.BLOCKER_TYPE0);
		nameToDEKindMap.put("blocker1", DerivativeEnemyKind.BLOCKER_TYPE0);
		nameToDEKindMap.put("blocker2", DerivativeEnemyKind.BLOCKER_TYPE0);
		nameToDEKindMap.put("bomb_gas", DerivativeEnemyKind.BOMB_GAS);
		nameToDEKindMap.put("laser_head", DerivativeEnemyKind.LASER_TYPE0);
		nameToDEKindMap.put("laser_body", DerivativeEnemyKind.LASER_TYPE0);
		nameToDEKindMap.put("laser_tail", DerivativeEnemyKind.LASER_TYPE0);
		nameToDEKindMap.put("mak_scatter0", DerivativeEnemyKind.MAKE_SCATTER_TYPE0);
		nameToDEKindMap.put("bossCarrier", DerivativeEnemyKind.CARRIER_BOSS);
	}
	
	private void setStage2DE(){
		
		nameToDEKindMap.put("blocker0", DerivativeEnemyKind.BLOCKER_TYPE0);
		nameToDEKindMap.put("blocker1", DerivativeEnemyKind.BLOCKER_TYPE0);
		nameToDEKindMap.put("blocker2", DerivativeEnemyKind.BLOCKER_TYPE0);
		nameToDEKindMap.put("bomb_gas", DerivativeEnemyKind.BOMB_GAS);
		nameToDEKindMap.put("laser_head", DerivativeEnemyKind.LASER_TYPE0);
		nameToDEKindMap.put("laser_body", DerivativeEnemyKind.LASER_TYPE0);
		nameToDEKindMap.put("laser_tail", DerivativeEnemyKind.LASER_TYPE0);
		nameToDEKindMap.put("mak_scatter0", DerivativeEnemyKind.MAKE_SCATTER_TYPE0);
		nameToDEKindMap.put("bossCarrier", DerivativeEnemyKind.CARRIER_BOSS);
		
		nameToDEKindMap.put("harricane0", DerivativeEnemyKind.HARRICANE);
		nameToDEKindMap.put("harricane1", DerivativeEnemyKind.HARRICANE);
		nameToDEKindMap.put("mine0", DerivativeEnemyKind.MINE_TYPE0);
	}
}
