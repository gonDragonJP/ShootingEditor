package shootingEditor.treeView.enemy.content;

import shootingEditor.enemy.CollisionRegion;
import shootingEditor.enemy.EnemyData;

public class CollisionNodeContent extends EntryContent{

	private CollisionNodeContent(String name, ContentCategory category) {
		super(name, category);
	}

	public enum Fields{
		COL_CENTER_X("centerX",null),
		COL_CENTER_Y("centerY",null),
		COL_SIZE("size",null),
		COL_SHAPE("collisionShape",null);
		
		public String fieldName, subFieldName;
		
		Fields(String argField, String argSubField){
			
			fieldName = argField;
			subFieldName = argSubField; 
		}
	}
	
	public static EntryContent create
	(Fields field, CollisionRegion node){
		
		return EntryContent.create
				(field.toString(), field.fieldName, field.subFieldName, node);
	}
}
