package shootingEditor.treeView.enemy;

import shootingEditor.enemy.EnemyData;
import shootingEditor.treeView.TreeContent;
import shootingEditor.treeView.TreeContent.ContentCategory;

public class TreeEnemyGroup extends TreeContent 
							implements TreeEnemyEntry.IndexCallback{

	public enum GroupType{
		
		MOVING         ("Moving",        ContentCategory.MUTABLE_GROUP),
		GENERATOR      ("Generator",     ContentCategory.MUTABLE_GROUP),
		COLLISION      ("Collision",     ContentCategory.MUTABLE_GROUP),
		ANIMATION      ("Animation",     ContentCategory.FIXED_GROUP),
		NORMAL_ANIME   ("NormalAnime",   ContentCategory.FIXED_GROUP),
		EXPLOSION_ANIME("ExplosionAnime",ContentCategory.FIXED_GROUP),
		NODE_ANIME     ("NodeAnime",     ContentCategory.MUTABLE_GROUP),
		MOVING_CHILD   ("Node",ContentCategory.CHILD_GROUP),
		GENERATOR_CHILD("Node",ContentCategory.CHILD_GROUP),
		COLLISION_CHILD("Node",ContentCategory.CHILD_GROUP),
		NODEANIME_CHILD("Node",ContentCategory.CHILD_GROUP);
		
		public String name;
		public ContentCategory contentCategory;
		
		GroupType(String argName, ContentCategory argType){
			
			name = argName;
			contentCategory = argType;
		}
	}
	
	public EnemyData enemyData;
	public GroupType groupType;
	
	public int childIndex;
	public int keyNode;
	
	public TreeEnemyGroup(EnemyData enemyData, GroupType groupType) {
		
		super(groupType.name, groupType.contentCategory);
		
		this.enemyData = enemyData;
		this.groupType = groupType;
		this.childIndex = -1;
		this.keyNode = -1;
	}
	
	public TreeEnemyGroup(EnemyData enemyData, GroupType groupType,int childIndex, int keyNode){
		//childGroupで使われるコンストラクタ
		
		this(enemyData, groupType);
		
		this.childIndex = childIndex;
		this.keyNode = keyNode;
	}
	
	@Override
	public String toString(){
		
		String index = childIndex >=0 ?
				"["+ String.valueOf(childIndex) + "]" : "";
		
		String key = keyNode >=0 ?
				"[key:"+ String.valueOf(keyNode) + "]" : "";
		
		return this.name + index + key;
	}

	@Override
	public int getChildIndex() {
		
		return childIndex;
	}

	@Override
	public int getKeyNode() {
		
		return keyNode;
	}

	@Override
	public void setKeyNode(int keyNode) {
		
		this.keyNode = keyNode;
	}
}
