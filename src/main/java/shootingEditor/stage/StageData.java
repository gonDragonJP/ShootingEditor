package shootingEditor.stage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import shootingEditor.animation.AnimationSet;
import shootingEditor.database.AccessOfEnemyData;
import shootingEditor.database.AccessOfEventData;
import shootingEditor.database.SQLiteManager;
import shootingEditor.enemy.EnemyData;
import shootingEditor.enemy.EnemyData.EnemyCategory;
import shootingEditor.enemy.derivativeType.DerivativeEnemyFactory;
import shootingEditor.texture.TextureInitializer;
import shootingEditor.texture.TextureSheet;

public class StageData {
	
		private static final int stageLength[] 
				= {8000,8000,8000,8000,8000};
		
		private static final boolean isStageShadowOn[] 
				= {false,true,false,false,false};
		
		private static FileAccess fileAccess = new FileAccess();
		
		public static int stage;

		public static boolean isShadowOn;
		public static int stageEndPoint;
		
		public static ArrayList<EnemyData> enemyList = new ArrayList<EnemyData>();
		public static ArrayList<EventData> eventList = new ArrayList<EventData>();
		
		public static DerivativeEnemyFactory derivativeEnemyFactory; 
		
		public static TextureSheet[] textureSheets;
		public static TextureSheet[] backgroundTexSheets;
		
		private StageData(){
		
		}
		
		public static void initialize(int stageNumber){
			
			stage = stageNumber;
			isShadowOn = isStageShadowOn[stageNumber -1];
			stageEndPoint = stageLength [stageNumber -1];
			
			//AnimationInitializer.setStageEnemyTexSheet(stageNumber);
			//ToDo) dbにテクスチャマッピングテーブルを作りイニシャライザからは切り離して自身でマップすること！ →　(Done
			
			textureSheets 
				= TextureInitializer.getStageEnemyTexSheets(stageNumber);
			backgroundTexSheets
				= TextureInitializer.getBackgroundTexSheets(stageNumber);
			
			//fileAccess.setEventList(eventList, stageNumber);
			//fileAccess.setEnemyList(enemyList, stageNumber); //独自ファイルフォーマット読み込み用
			
			refreshEventListFromDB();
			refreshEnemyListFromDB();
			
			//AccessOfEventData.addEventList(eventList, stage);
			//AccessOfEnemyData.addEnemyList(enemyList, stage); //DBへリスト書き込み
			
			derivativeEnemyFactory = new DerivativeEnemyFactory(stageNumber);
		}
		
		public static void refreshEventListFromDB(){
			
			eventList.clear();
			AccessOfEventData.setEventList(eventList, stage);
		}
		
		public static void refreshEnemyListFromDB(){
			
			enemyList.clear();
			AccessOfEnemyData.setEnemyList(enemyList, stage);
		}
		
		public static boolean isLastStage(){
			
			return (stage == stageLength.length);
		}
		
		public static int getIndexOfEnemyList(int objectID){
			
			int result = -1;
			
			for(int i=0; i<enemyList.size(); i++){
				
				EnemyData enemyData = enemyList.get(i);
				if(enemyData.objectID == objectID){
					result = i;
					break;
				}
			}
			return result;
		}
		
		public static int getLastIDinEnemyList(EnemyCategory category){
			
			int lastID =-1;
			
			for(EnemyData e: enemyList){
				
				if(e.objectID >= (category.getID() +1)*1000) break;
				lastID = e.objectID;
			}
			return lastID;
		}
}
