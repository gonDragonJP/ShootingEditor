package shootingEditor.treeView.enemy.content;

import shootingEditor.enemy.GeneratingChild;

public class GeneratorNodeContent extends EntryContent{

	private GeneratorNodeContent(String name, ContentCategory category) {
		super(name, category);
	}
	
	public enum Fields{
		GEN_OBJECT_ID("objectID",null),
		GEN_REPEAT("repeat",null),
		GEN_STARTFRAME("startFrame",null),
		GEN_INTERVALFRAME("intervalFrame",null),
		GEN_CENTER_X("centerX",null),
		GEN_CENTER_Y("centerY",null);
		
		public String fieldName, subFieldName;
		
		Fields(String argField, String argSubField){
			
			fieldName = argField;
			subFieldName = argSubField; 
		}
	}
	
	public static EntryContent create
	(Fields field, GeneratingChild node){
		
		return EntryContent.create
				(field.toString(), field.fieldName, field.subFieldName, node);
	}

}
