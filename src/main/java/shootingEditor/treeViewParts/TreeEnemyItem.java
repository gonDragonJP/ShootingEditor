package shootingEditor.treeViewParts;
import java.lang.reflect.Field;
import java.util.Map;

import javafx.util.Callback;
import shootingEditor.enemy.EnemyData;
import shootingEditor.enemy.MovingNode;
import shootingEditor.animation.AnimationData;
import shootingEditor.animation.AnimationData.RepeatAttribute;
import shootingEditor.animation.AnimationData.RotateAttribute;
import shootingEditor.animation.AnimationSet.AnimeKind;
import shootingEditor.enemy.CollisionRegion;
import shootingEditor.enemy.CollisionRegion.CollisionShape;

public class TreeEnemyItem extends TreeContent{
	
	interface IndexCallback{
		
		int getChildIndex();
		int getKeyNode();
		void setKeyNode(int keyNode);
	}
	
	private IndexCallback indexCallback;
	
	public enum ItemCategory{
		
		BASIC_DATA,
		MOVING_DATA,
		GENERATOR_DATA,
		COLLISION_DATA,
		ANIME_DATA,
		NODEANIME_KEY
	}
	
	public enum ValueType{
		
		STRING,
		NUMBER,
		BOOLEAN,
		INT2VECTOR_ELEMENT,
		DOUBLE2VECTOR_ELEMENT,
		STARTPOSITION_ATTRIB,
		STARTVELOCITY_ATTRIB,
		STARTACCELERATION_ATTRIB,
		ANI_REPEAT_ATTRIB,
		ANI_ROTATE_ATTRIB,
		COLLISION_SHAPE,
		TEXTURE_SHEET
	}
	
	public enum ItemType{
		
		NAME("name",ItemCategory.BASIC_DATA,ValueType.STRING), 
		OBJECT_ID("objectID",ItemCategory.BASIC_DATA,ValueType.NUMBER), 
		IS_DERIVATIVE("isDerivativeType",ItemCategory.BASIC_DATA,ValueType.BOOLEAN), 
		TEXTURE_ID("textureID",ItemCategory.BASIC_DATA,ValueType.NUMBER),
		HITPOINT("hitPoints",ItemCategory.BASIC_DATA,ValueType.NUMBER), 
		ATACKPOINT("atackPoints",ItemCategory.BASIC_DATA,ValueType.NUMBER),
		STARTPOSITION_X("startPosition_x",ItemCategory.BASIC_DATA,ValueType.INT2VECTOR_ELEMENT), 
		STARTPOSITION_Y("startPosition_y",ItemCategory.BASIC_DATA,ValueType.INT2VECTOR_ELEMENT),
		STARTPOSITION_ATTRIB_X("startPosAttrib_x",ItemCategory.BASIC_DATA,ValueType.STARTPOSITION_ATTRIB), 
		STARTPOSITION_ATTRIB_Y("startPosAttrib_y",ItemCategory.BASIC_DATA,ValueType.STARTPOSITION_ATTRIB),
		
		STARTVELOCITY_X("startVelocity_x",ItemCategory.MOVING_DATA,ValueType.DOUBLE2VECTOR_ELEMENT),
		STARTVELOCITY_Y("startVelocity_y",ItemCategory.MOVING_DATA,ValueType.DOUBLE2VECTOR_ELEMENT),
		STARTACCELERATION_X("startAcceleration_x",ItemCategory.MOVING_DATA,ValueType.DOUBLE2VECTOR_ELEMENT),
		STARTACCELERATION_Y("startAcceleration_y",ItemCategory.MOVING_DATA,ValueType.DOUBLE2VECTOR_ELEMENT),
		HOMINGACCELERATION_X("homingAcceleration_x",ItemCategory.MOVING_DATA,ValueType.DOUBLE2VECTOR_ELEMENT),
		HOMINGACCELERATION_Y("homingAcceleration_y",ItemCategory.MOVING_DATA,ValueType.DOUBLE2VECTOR_ELEMENT),
		NODEDURATIONFRAME("nodeDurationFrame",ItemCategory.MOVING_DATA,ValueType.NUMBER), 
		STARTVELOCITY_ATTRIBE_X("startVelAttrib_x",ItemCategory.MOVING_DATA,ValueType.STARTVELOCITY_ATTRIB), 
		STARTVELOCITY_ATTRIBE_Y("startVelAttrib_y",ItemCategory.MOVING_DATA,ValueType.STARTVELOCITY_ATTRIB),
		STARTACCELERATION_ATTRIBE_X("startAccAttrib_x",ItemCategory.MOVING_DATA,ValueType.STARTACCELERATION_ATTRIB), 
		STARTACCELERATION_ATTRIBE_Y("startAccAttrib_y",ItemCategory.MOVING_DATA,ValueType.STARTACCELERATION_ATTRIB),
			
		GEN_OBJECT_ID("objectID",ItemCategory.GENERATOR_DATA,ValueType.NUMBER),
		GEN_REPEAT("repeat",ItemCategory.GENERATOR_DATA,ValueType.NUMBER),
		GEN_STARTFRAME("startFrame",ItemCategory.GENERATOR_DATA,ValueType.NUMBER),
		GEN_INTERVALFRAME("intervalFrame",ItemCategory.GENERATOR_DATA,ValueType.NUMBER),
		GEN_CENTER_X("centerX",ItemCategory.GENERATOR_DATA,ValueType.NUMBER),
		GEN_CENTER_Y("centerY",ItemCategory.GENERATOR_DATA,ValueType.NUMBER),
		
		COL_CENTER_X("centerX",ItemCategory.COLLISION_DATA,ValueType.NUMBER),
		COL_CENTER_Y("centerY",ItemCategory.COLLISION_DATA,ValueType.NUMBER),
		COL_SIZE("size",ItemCategory.COLLISION_DATA,ValueType.NUMBER),
		COL_SHAPE("collisionShape",ItemCategory.COLLISION_DATA,ValueType.COLLISION_SHAPE),
		
		ANI_DRAWSIZE_X("drawSize_x",ItemCategory.ANIME_DATA,ValueType.DOUBLE2VECTOR_ELEMENT),
		ANI_DRAWSIZE_Y("drawSize_y",ItemCategory.ANIME_DATA,ValueType.DOUBLE2VECTOR_ELEMENT),
		ANI_TEXTURESHEET("textureSheet",ItemCategory.ANIME_DATA,ValueType.TEXTURE_SHEET),
		ANI_REPEAT_ATTRIB("repeatAttribute",ItemCategory.ANIME_DATA,ValueType.ANI_REPEAT_ATTRIB),
		ANI_FRAME_OFFSET("frameOffset",ItemCategory.ANIME_DATA,ValueType.NUMBER),
		ANI_FRAME_NUMBER("frameNumber",ItemCategory.ANIME_DATA,ValueType.NUMBER),
		ANI_ROTATE_ATTRIB("rotateAction",ItemCategory.ANIME_DATA,ValueType.ANI_ROTATE_ATTRIB),
		ANI_ROTATE_OFFSET("rotateOffset",ItemCategory.ANIME_DATA,ValueType.NUMBER),
		ANI_ANGULAR_VELOCITY("angularVelocity",ItemCategory.ANIME_DATA,ValueType.NUMBER),
		
		ANI_NODEANIME_KEY("keyNode",ItemCategory.NODEANIME_KEY,ValueType.NUMBER);
		
		public String name;
		public ItemCategory category;
		public ValueType dataType;
		
		ItemType(String argName, ItemCategory argCategory, ValueType argDataType){
			
			name = argName;
			category = argCategory;
			dataType = argDataType;
		}
	}
	
	EnemyData enemyData;
	ItemType itemType;
	String textValue;
	
	int attribID = -1;
	AnimeKind animeKind = null;
	
	public TreeEnemyItem(IndexCallback indexCallback, EnemyData enemyData, ItemType itemType) {
		super(itemType.name);
		
		this.indexCallback = indexCallback;
		this.enemyData = enemyData;
		this.itemType = itemType;
		
		int childIndex = -1, keyNode = -1;

		if(indexCallback!=null){
			childIndex = indexCallback.getChildIndex();
			keyNode = indexCallback.getKeyNode();
		}
		
		switch (itemType.category){
		
			case BASIC_DATA:
				setValueFromEnemyData(enemyData);
				break;
				
			case MOVING_DATA:
				setValueFromEnemyData(enemyData.node.get(childIndex));
				break;
				
			case GENERATOR_DATA:
				setValueFromEnemyData(enemyData.generator.get(childIndex));
				break;
				
			case COLLISION_DATA:
				setValueFromEnemyData(enemyData.collision.get(childIndex));
				break;
				
			case NODEANIME_KEY:
				textValue = String.valueOf(keyNode);
				break;
				
			case ANIME_DATA:
				this.animeKind = AnimeKind.NODEACTION;
				setValueFromEnemyData(enemyData.animationSet.nodeActionAnime.get(keyNode));
				break;
				
			default:
		}
	}
	
	public TreeEnemyItem(EnemyData enemyData, ItemType itemType, AnimeKind animeKind){
		super(itemType.name);
		
		this.enemyData = enemyData;
		this.itemType = itemType;
		
		if(animeKind == AnimeKind.NORMAL){
			
			this.animeKind = animeKind;
			setValueFromEnemyData(enemyData.animationSet.normalAnime);
		}
		else{
			
			this.animeKind = AnimeKind.EXPLOSION;
			setValueFromEnemyData(enemyData.animationSet.explosionAnime);
		}
	}
	
	@Override
	public String toString(){
		
		String value = (itemType.dataType == ValueType.STRING) 
									? "'"+ this.textValue +"'" : this.textValue;
		
		return this.name + " ="+ value;
	}
	
	public void setValueByText(String text){
		
		if(setValueToEnemyData(text)) this.textValue = text;
	}
	
	public void setChoicedValue(int index, String text){
		
		if(setValueToEnemyData(String.valueOf(index))) {
			this.textValue = text;
			this.attribID = index;
		}
	}

	private void setValueFromEnemyData(Object targetLayer){
		
		switch (itemType.dataType){
		
		case STRING:
		case NUMBER:
		case BOOLEAN:
			textValue = getReflectedSimpleValue(targetLayer);
			break;
			
		case INT2VECTOR_ELEMENT:
		case DOUBLE2VECTOR_ELEMENT:
			textValue = getReflectedVectorValue(targetLayer).toString();
			break;
	
		case STARTPOSITION_ATTRIB:
			attribID = (itemType == ItemType.STARTPOSITION_ATTRIB_X)?
				 ((EnemyData)targetLayer).startPosAttrib.x
				:((EnemyData)targetLayer).startPosAttrib.y;
			textValue = EnemyData.StartPositionAtrib.getFromID(attribID).name();
			break;
			
		case STARTVELOCITY_ATTRIB:
			attribID = (itemType == ItemType.STARTVELOCITY_ATTRIBE_X)?
				 ((MovingNode)targetLayer).startVelAttrib.x
				:((MovingNode)targetLayer).startVelAttrib.y;
			textValue = EnemyData.StartVectorAtrib.getFromID(attribID).name();
			break;
			
		case STARTACCELERATION_ATTRIB:
			attribID = (itemType == ItemType.STARTACCELERATION_ATTRIBE_X)?
				 ((MovingNode)targetLayer).startAccAttrib.x
				:((MovingNode)targetLayer).startAccAttrib.y;
			textValue = EnemyData.StartVectorAtrib.getFromID(attribID).name();
			break;
			
		case ANI_REPEAT_ATTRIB:
			attribID = ((AnimationData)targetLayer).repeatAttribute.getID();
			textValue =((AnimationData)targetLayer).repeatAttribute.name();
			break;
			
		case ANI_ROTATE_ATTRIB:
			attribID = ((AnimationData)targetLayer).rotateAction.getID();
			textValue =((AnimationData)targetLayer).rotateAction.name();
			break;
			
		case COLLISION_SHAPE:
			attribID = ((CollisionRegion)targetLayer).collisionShape.getID();
			textValue = ((CollisionRegion)targetLayer).collisionShape.name();
			break;
		
		default:
			textValue = "null";
		}
	}
	
	private String getReflectedSimpleValue(Object object){
		
		Class<?> clazz = object.getClass();
		
		try{
		
			Field field = clazz.getDeclaredField(itemType.name);
			
			return field.get(object).toString();
		
		}catch(ReflectiveOperationException e){
		
		}
		return "null";
	}
	
	private Object getReflectedVectorValue(Object object){
		
		String[] parts = itemType.name.split("_");
		String field1 = parts[0];
		String field2 = parts[1];
		
		Class<?> clazz = object.getClass();
		
		try{
			Field field = clazz.getDeclaredField(field1);
			Object object2 = field.get(object);
		
		
			clazz = object2.getClass();
			field = clazz.getDeclaredField(field2);
			
			return field.get(object2);
			
		}catch(ReflectiveOperationException e){
			
		}
		return null;
	}
	
	private boolean setValueToEnemyData(String text){
		
		int childIndex = -1, keyNode = -1;
		
		if(indexCallback!=null){
			childIndex = indexCallback.getChildIndex();
			keyNode = indexCallback.getKeyNode();
		}
		
		switch (itemType.category){
		
		case BASIC_DATA:
			return changeEnemyData(enemyData,text);
		
		case MOVING_DATA:
			return changeEnemyData(enemyData.node.get(childIndex),text);
			
		case GENERATOR_DATA:
			return changeEnemyData(enemyData.generator.get(childIndex),text);
			
		case COLLISION_DATA:
			return changeEnemyData(enemyData.collision.get(childIndex),text);
			
		case ANIME_DATA:
			switch(animeKind){
			
			case NORMAL:
				return changeEnemyData(enemyData.animationSet.normalAnime, text);
			case EXPLOSION:
				return changeEnemyData(enemyData.animationSet.explosionAnime, text);
			case NODEACTION:
				return changeEnemyData(enemyData.animationSet.nodeActionAnime.get(keyNode), text);
			default:
			}
			
		case NODEANIME_KEY:
			return changeKeyNode(Integer.valueOf(text));
		default:
		}
		return false;
	}
	
	private boolean changeKeyNode(int newKeyNode){
		
		int keyNode = indexCallback.getKeyNode();
		
		Map<Integer,AnimationData> animeMap = enemyData.animationSet.nodeActionAnime;
		AnimationData currentAnimeData = animeMap.get(keyNode);
		
		if(animeMap.containsKey(newKeyNode)) return false;
		
		animeMap.put(newKeyNode, currentAnimeData);
		animeMap.remove(keyNode);
		
		indexCallback.setKeyNode(newKeyNode);
		
		MyTreeUtil.refresh();
		
		return true;
	}
	
	private boolean changeEnemyData(Object targetLayer,String text){
		
		switch (itemType.dataType){
		
		case STRING:
		case NUMBER:
		case BOOLEAN:
			return setReflectedSimpleEnemyData(targetLayer, text);
		
		case INT2VECTOR_ELEMENT:
		case DOUBLE2VECTOR_ELEMENT:
			return setReflectedVectorEnemyData(targetLayer, text);
			
		case STARTPOSITION_ATTRIB:
		case STARTVELOCITY_ATTRIB:
		case STARTACCELERATION_ATTRIB:
			return setReflectedVectorEnemyData(targetLayer, text);	
			
		case ANI_REPEAT_ATTRIB:
			((AnimationData)targetLayer).repeatAttribute
				= RepeatAttribute.getFromID(Integer.valueOf(text));
			return true;
		
		case ANI_ROTATE_ATTRIB:
			((AnimationData)targetLayer).rotateAction
				= RotateAttribute.getFromID(Integer.valueOf(text));
			return true;
			
		case COLLISION_SHAPE:
			((CollisionRegion)targetLayer).collisionShape 
				= CollisionShape.getFromID(Integer.valueOf(text));
			return true;
		
		default:
		}
		return false;
	}
	
	private boolean setReflectedSimpleEnemyData(Object object, String text){
		
		Class<?> clazz = object.getClass();
		
		try{
		
			Field field = clazz.getDeclaredField(itemType.name);
			
			Class<?> type = field.getType();
			
			switch(type.getName()){
			
			case "java.lang.String":
				field.set(object, text);
				break;

			case "int":
				field.set(object, Integer.valueOf(text));
				break;
				
			case "boolean": 
				field.set(object, Boolean.valueOf(text));
				break;
				
			case "double":
				field.set(object, Double.valueOf(text));
				break;
				
			default:
				return false;
			}
			
		}catch(Exception e){
		
			return false;
		}
		return true;
	}
	
	private boolean setReflectedVectorEnemyData(Object object, String text){
		
		String[] parts = itemType.name.split("_");
		String field1 = parts[0];
		String field2 = parts[1];
		
		Class<?> clazz = object.getClass();
		
		try{
			Field field = clazz.getDeclaredField(field1);
			Object object2 = field.get(object);
		
		
			clazz = object2.getClass();
			field = clazz.getDeclaredField(field2);
			Class<?> type = field.getType();
			
			if(type.getName() == "int") field.set(object2, Integer.valueOf(text));
			else if(type.getName() == "double") field.set(object2, Double.valueOf(text));
			else return false;
			
		}catch(Exception e){
			
			return false;
		}
		return true;
	}
}
