package shootingEditor.treeView.enemy;

import shootingEditor.enemy.EnemyData;
import shootingEditor.treeView.EntryContent;
import shootingEditor.treeView.TreeContent.ContentCategory;
import shootingEditor.treeView.enemy.BasicDataContent.Fields;
import shootingEditor.treeView.enemy.TreeEnemyEntry.EntryCategory;
import shootingEditor.treeView.enemy.TreeEnemyEntry.ValueType;

public class MovingNodeContent extends EntryContent{
	
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
	
	private Fields field;
	private EnemyData enemyData;

	private MovingNodeContent(String name, ContentCategory category) {
		super(name, category);
	}
	
	static public MovingNodeContent create(Fields field, EnemyData enemyData) {
		
		MovingNodeContent content 
		= new MovingNodeContent(field.fieldName, ContentCategory.ENTRY);
		
		content.field = field;
		content.enemyData = enemyData;
		content.setValueTextByEnemyData(field.fieldName, field.subFieldName);
		
		return content;
	}
	
	@Override
	public void setValueByText(String text){
		
		if(setEnemyDataByAText(text, field.fieldName, field.subFieldName)) this.valueText = text;
	}
	
	@Override
	public void setChoicedValue(int index, String text){
		
		if(setEnemyDataByAText(String.valueOf(index), field.fieldName, field.subFieldName)) {
			this.valueText = text;
			//this.attribID = index;
		}
	}
}
