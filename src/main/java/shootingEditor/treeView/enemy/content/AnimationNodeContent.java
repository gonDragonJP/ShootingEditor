package shootingEditor.treeView.enemy.content;

import shootingEditor.animation.AnimationData;
import shootingEditor.enemy.EnemyData;
import shootingEditor.treeView.enemy.TreeEnemyEntry.EntryCategory;
import shootingEditor.treeView.enemy.TreeEnemyEntry.ValueType;
import shootingEditor.treeView.enemy.content.MovingNodeContent.Fields;

public class AnimationNodeContent extends EntryContent{

	private AnimationNodeContent(String name, ContentCategory category) {
		super(name, category);
	}

	public enum Fields{
		ANI_TEXTURE_ID("textureID",null),
		ANI_DRAWSIZE_X("drawSize","x"),
		ANI_DRAWSIZE_Y("drawSize","y"),
		ANI_REPEAT_ATTRIB("repeatAttribute",null),
		ANI_FRAME_OFFSET("frameOffset",null),
		ANI_FRAME_NUMBER("frameNumber",null),
		ANI_ROTATE_ATTRIB("rotateAction",null),
		ANI_ROTATE_OFFSET("rotateOffset",null),
		ANI_ANGULAR_VELOCITY("angularVelocity",null);
		
		public String fieldName, subFieldName;
		
		Fields(String argField, String argSubField){
			
			fieldName = argField;
			subFieldName = argSubField; 
		}
	}
	
	public static EntryContent create
	(Fields field, AnimationData node){
		
		return EntryContent.create
				(field.toString(), field.fieldName, field.subFieldName, node);
	}
}
