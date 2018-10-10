package shootingEditor.treeView.enemy.content;

import shootingEditor.enemy.EnemyData;
import shootingEditor.enemy.MovingNode;
import shootingEditor.treeView.TreeContent.ContentCategory;

public class MovingNodeContent extends EntryContent{
	
	private MovingNodeContent(String name, ContentCategory category) {
		super(name, category);
	}

	public enum Fields{
		STARTVELOCITY_X("startVelocity","x"),
		STARTVELOCITY_Y("startVelocity","y"),
		STARTACCELERATION_X("startAcceleration","x"),
		STARTACCELERATION_Y("startAcceleration","y"),
		HOMINGACCELERATION_X("homingAcceleration","x"),
		HOMINGACCELERATION_Y("homingAcceleration","y"),
		NODEDURATIONFRAME("nodeDurationFrame",null), 
		STARTVELOCITY_ATTRIBE_X("startVelAttrib","x"), 
		STARTVELOCITY_ATTRIBE_Y("startVelAttrib","y"),
		STARTACCELERATION_ATTRIBE_X("startAccAttrib","x"), 
		STARTACCELERATION_ATTRIBE_Y("startAccAttrib","y");
		
		public String fieldName, subFieldName;
		
		Fields(String argField, String argSubField){
			
			fieldName = argField;
			subFieldName = argSubField; 
		}
	}
	
	public static EntryContent create
	(Fields field, MovingNode node){
		
		MovingNodeContent content = new MovingNodeContent(field.toString(), ContentCategory.ENTRY);
		content.referObject = node;
		content.fieldName = field.fieldName;
		content.subFieldName = field.subFieldName;
		content.setValueTextByEnemyData(); 
			//MovingNodeContentのメソッドを呼ぶ為に親クラスのcreateを使っていません
		
		return content;
	}
	
	public void setValueTextByEnemyData(){
		super.setValueTextByEnemyData();
		
		if((fieldName == "startVelAttrib") || (fieldName == "startAccAttrib")) 
			valueText = EnemyData.StartVectorAtrib
			.getFromID(Integer.valueOf(valueText)).toString();
	}
}
