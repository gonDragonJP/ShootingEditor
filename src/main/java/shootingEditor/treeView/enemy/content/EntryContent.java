package shootingEditor.treeView.enemy.content;

import shootingEditor.enemy.EnemyData;
import shootingEditor.treeView.ReflectUtil;
import shootingEditor.treeView.TreeContent;
import shootingEditor.treeView.TreeContent.ContentCategory;

	public class EntryContent extends TreeContent{

	public Object referObject;
	
	public String fieldName, subFieldName;
	public String valueText;
	
	public EntryContent(String name, ContentCategory category) {
		super(name, category);	
	}
	
	public static EntryContent create
	(String name, String fieldName, String subFieldName, Object referObject){
		
		EntryContent content = new EntryContent(name, ContentCategory.ENTRY);
		content.referObject = referObject;
		content.fieldName = fieldName;
		content.subFieldName = subFieldName;
		content.setValueTextByEnemyData();
		
		return content;
	}
	
	public void setValueByText(String text){
		
		if(setEnemyDataByAText(text)) this.valueText = text;
	};
	
	public void setChoicedValue(int index, String text){
		
		if(setEnemyDataByAText(String.valueOf(index))) {
			this.valueText = text;
			//this.attribID = index;
		}
	};
	
	@Override
	public String toString(){
		
		return this.name + " = "+ valueText;
	}
	
	public void setValueTextByEnemyData(){
		
		String fieldType = ReflectUtil.getFieldType(referObject, fieldName);
		
		switch(fieldType) {
			
		case "java.lang.String": case "int": case "double": case "boolean":
			
			valueText = 
				ReflectUtil.getReflectedSimpleValue(referObject, fieldName);
			break;
		
		case "shootingEditor.vector.Int2Vector":
		case "shootingEditor.vector.Double2Vector":
			
			valueText = ReflectUtil.getReflectedVectorValue
				(referObject, fieldName, subFieldName);
			break;
			
		default:	//enumå^Ç‡êîílå^Ç»Ç«Ç∆ìØÇ∂ÇÊÇ§Ç…ñºèÃÇ™éÊÇËèoÇπÇ‹Ç∑
			valueText = 
				ReflectUtil.getReflectedSimpleValue(referObject, fieldName);
		}
		
		if(fieldName == "startPosAttrib") 
			valueText = EnemyData.StartPositionAtrib
			.getFromID(Integer.valueOf(valueText)).toString();
	}
	
	public boolean setEnemyDataByAText(String text) {
		
		String fieldType = ReflectUtil.getFieldType(referObject, fieldName);
		
		switch(fieldType) {
		
		case "java.lang.String": case "int": case "double": case "boolean":
			
			return ReflectUtil.setReflectedSimpleEnemyData
					(referObject, text, fieldName);
		
		case "shootingEditor.vector.Int2Vector":
		case "shootingEditor.vector.Double2Vector":
			
			return ReflectUtil.setReflectedVectorEnemyData
					(referObject, text, fieldName, subFieldName);
		}
		
		return false;
	}
}
