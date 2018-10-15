package shootingEditor.treeView.enemy.content;

import shootingEditor.enemy.EnemyData;

public class BasicDataContent extends EntryContent{

	private BasicDataContent(String name, ContentCategory category) {
		super(name, category);
	}
	
	public enum Fields{
		NAME("name",null), 
		OBJECT_ID("objectID",null), 
		IS_DERIVATIVE("isDerivativeType",null), 
		EXPLOSIVE_OBJECT_ID("explosiveObjectID",null),
		HITPOINT("hitPoints",null), 
		ATACKPOINT("atackPoints",null),
		STARTPOSITION_X("startPosition","x"), 
		STARTPOSITION_Y("startPosition","y"),
		STARTPOSITION_ATTRIB_X("startPosAttrib","x"), 
		STARTPOSITION_ATTRIB_Y("startPosAttrib","y");
		
		public String fieldName, subFieldName;
		
		Fields(String argField, String argSubField){
			
			fieldName = argField;
			subFieldName = argSubField; 
		}
	}
	
	public static EntryContent create
	(Fields field, EnemyData enemyData){
		
		BasicDataContent content = new BasicDataContent(field.toString(), ContentCategory.ENTRY);
		content.referObject = enemyData;
		content.fieldName = field.fieldName;
		content.subFieldName = field.subFieldName;
		content.setValueTextByEnemyData(); 
			//BasicDataContentのメソッドを呼ぶ為に親クラスのcreateを使っていません
		
		return content;
	}
	
	public void setValueTextByEnemyData(){
		super.setValueTextByEnemyData();
		
		if(fieldName == "startPosAttrib") 
			valueText = EnemyData.StartPositionAtrib
			.getFromID(Integer.valueOf(valueText)).toString();
	}
}
