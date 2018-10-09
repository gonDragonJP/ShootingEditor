package shootingEditor.treeView.enemy.content;

import shootingEditor.animation.AnimationData;
import shootingEditor.animation.AnimationData.RepeatAttribute;
import shootingEditor.animation.AnimationData.RotateAttribute;
import shootingEditor.enemy.CollisionRegion;
import shootingEditor.enemy.EnemyData;
import shootingEditor.enemy.CollisionRegion.CollisionShape;
import shootingEditor.treeView.ReflectUtil;
import shootingEditor.treeView.TreeContent;

	public class EntryContent extends TreeContent{

	public Object referObject;
	
	public String fieldName, subFieldName;
	public String valueText;
	public int attribID; //扱うのが選択式のフィールドであれば利用されます
	
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
		
		if(setEnemyDataByMultipleChoice(index, text)) {
			this.valueText = text;
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
			
		default:	//enum型も数値型などと同じように名称が取り出せます
			valueText = 
				ReflectUtil.getReflectedSimpleValue(referObject, fieldName);
		}
	}
	
	private boolean setEnemyDataByAText(String text) {
		
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
	
	private boolean setEnemyDataByMultipleChoice(int index, String text){
		
		switch (fieldName){
		
		case "startPosAttrib":
		case "startVelAttrib":
		case "startAccAttrib":
			return ReflectUtil.setReflectedVectorEnemyData
					(referObject, String.valueOf(index), fieldName, subFieldName);	
			
		case "repeatAttribute":
			((AnimationData)referObject).repeatAttribute
				= RepeatAttribute.getFromID(index);
			return true;
		
		case "rotateAction":
			((AnimationData)referObject).rotateAction
				= RotateAttribute.getFromID(index);
			return true;
			
		case "collisionShape":
			((CollisionRegion)referObject).collisionShape 
				= CollisionShape.getFromID(index);
			return true;
		
		default:
		}
		
		return false;
	}
}
