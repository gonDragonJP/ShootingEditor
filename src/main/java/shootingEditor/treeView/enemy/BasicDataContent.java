package shootingEditor.treeView.enemy;

import java.lang.reflect.Field;

import shootingEditor.enemy.EnemyData;
import shootingEditor.treeView.EntryContent;
import shootingEditor.treeView.ReflectUtil;
import shootingEditor.treeView.TreeContent;
import shootingEditor.treeView.enemy.TreeEnemyEntry.ValueType;

public class BasicDataContent extends EntryContent{

	public enum Fields{
		NAME("name",null), 
		OBJECT_ID("objectID",null), 
		IS_DERIVATIVE("isDerivativeType",null), 
		TEXTURE_ID("textureID",null),
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
	
	private Fields field;
	
	private BasicDataContent(String name, ContentCategory category) {
		super(name, category);
	}

	static public BasicDataContent create(Fields field, EnemyData enemyData) {
		
		BasicDataContent content 
		= new BasicDataContent(field.fieldName, ContentCategory.ENTRY);
		
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
