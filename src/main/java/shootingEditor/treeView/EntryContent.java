package shootingEditor.treeView;

import shootingEditor.enemy.EnemyData;
import shootingEditor.treeView.enemy.BasicDataContent.Fields;

abstract public class EntryContent extends TreeContent{

	public EnemyData enemyData;
	public String valueText;

	public EntryContent(String name, ContentCategory category) {
		super(name, category);
		
	}
	
	abstract public void setValueByText(String text);
	abstract public void setChoicedValue(int index, String Text);
	
	@Override
	public String toString(){
		
		return this.name + " ="+ valueText;
	}
	
	public void setValueTextByEnemyData(String fieldName, String subFieldName){
		
		String fieldType = ReflectUtil.getFieldType(enemyData, fieldName);
		
		switch(fieldType) {
			
		case "java.lang.String": case "int": case "boolean":
			
			valueText = 
				ReflectUtil.getReflectedSimpleValue(enemyData, fieldName);
			break;
		
		case "shootingEditor.vector.Int2Vector":
			
			valueText = 
				ReflectUtil.getReflectedVectorValue
				(enemyData, fieldName, subFieldName);
		}
		
		if(fieldName == "startPosAttrib") 
			valueText = EnemyData.StartPositionAtrib
			.getFromID(Integer.valueOf(valueText)).toString();
	}
	
	public boolean setEnemyDataByAText(String text, String fieldName, String subFieldName) {
		
		String fieldType = ReflectUtil.getFieldType(enemyData, fieldName);
		
		switch(fieldType) {
		
		case "java.lang.String": case "int": case "boolean":
			
			return ReflectUtil.setReflectedSimpleEnemyData
					(enemyData, text, fieldName);
		
		case "shootingEditor.vector.Int2Vector":
			
			return ReflectUtil.setReflectedVectorEnemyData
					(enemyData, text, fieldName, subFieldName);
		}
		
		return false;
	}
}
