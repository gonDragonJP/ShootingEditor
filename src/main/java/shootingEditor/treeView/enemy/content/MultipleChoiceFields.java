package shootingEditor.treeView.enemy.content;

import java.util.ArrayList;

import shootingEditor.animation.AnimationData.RepeatAttribute;
import shootingEditor.animation.AnimationData.RotateAttribute;
import shootingEditor.enemy.CollisionRegion;
import shootingEditor.enemy.EnemyData;

public class MultipleChoiceFields {

	private static String fieldNames[] ={
		
			"isDerivativeType",
			"startPosAttrib",
			"startVelAttrib",
			"startAccAttrib",
			"collisionShape",
			"repeatAttribute",
			"rotateAction"
	};
	
	public static boolean isMultipleChoiceField(String fieldName){
		
		for(String e: fieldNames){
			
			if (e == fieldName) return true;
		}
		return false;
	}
	
	public static ArrayList<String> 
		getMultipleChoiceList(String fieldName){
	
		ArrayList<String> list = new ArrayList<>();
	
		switch(fieldName){
		
		case "isDerivativeType":
			list.add("true"); list.add("false");
			break;
			
		case "startPosAttrib":
			for(EnemyData.StartPositionAtrib e: EnemyData.StartPositionAtrib.values()){
				list.add(e.name());
			}
			break;
			
		case "startVelAttrib":
		case "startAccAttrib":
			for(EnemyData.StartVectorAtrib e: EnemyData.StartVectorAtrib.values()){
				list.add(e.name());
			}
			break;
			
		case "collisionShape":
			for(CollisionRegion.CollisionShape e: CollisionRegion.CollisionShape.values()){
				list.add(e.name());
			}
			break;
			
		case "repeatAttribute":
			for(RepeatAttribute e: RepeatAttribute.values()){
				list.add(e.name());
			}
			break;
			
		case "rotateAction":
			for(RotateAttribute e: RotateAttribute.values()){
				list.add(e.name());
			}
			break;
			
		default:
			return null;
		}
	
		return list;
	}
}
