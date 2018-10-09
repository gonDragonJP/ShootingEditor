package shootingEditor.treeView.event;
import java.lang.reflect.Field;
import java.util.Map;

import javafx.util.Callback;
import shootingEditor.enemy.EnemyData;
import shootingEditor.enemy.MovingNode;
import shootingEditor.stage.EventData;
import shootingEditor.stage.EventData.EventCategory;
import shootingEditor.animation.AnimationData;
import shootingEditor.animation.AnimationData.RepeatAttribute;
import shootingEditor.animation.AnimationData.RotateAttribute;
import shootingEditor.animation.AnimationSet.AnimeKind;
import shootingEditor.enemy.CollisionRegion;
import shootingEditor.enemy.CollisionRegion.CollisionShape;
import shootingEditor.treeView.TreeContent;
import shootingEditor.treeView.trash.TreeEnemyEntry.Entry;

public class TreeEventEntry extends TreeContent{
	
	public enum EntryCategory{
		
		EVENT_DATA,
	}
	
	public enum ValueType{
		
		STRING,
		NUMBER,
		BOOLEAN,
		INT2VECTOR_ELEMENT,
		DOUBLE2VECTOR_ELEMENT,
		EVENT_CATEGORY
	}
	
	public enum Entry{
		
		SCROLL_POINT("scrollPoint",EntryCategory.EVENT_DATA,ValueType.NUMBER),
		EVENT_CATEGORY("eventCategory",EntryCategory.EVENT_DATA,ValueType.EVENT_CATEGORY),
		EVENT_OBJECT_ID("eventObjectID",EntryCategory.EVENT_DATA,ValueType.NUMBER);
		
		public String name;
		public EntryCategory category;
		public ValueType valueType;
		
		Entry(String argName, EntryCategory argCategory, ValueType argDataType){
			
			name = argName;
			category = argCategory;
			valueType = argDataType;
		}
	}
	
	EventData eventData;
	Entry entry;
	String textValue;
	
	int attribID = -1;
	
	public TreeEventEntry(EventData eventData, Entry entry) {
		
		super(entry.name);
		
		this.eventData = eventData;
		this.entry = entry;
		
		setValueFromEventData(eventData);
	}
	
	@Override
	public String toString(){
		
		String value = (entry.valueType == ValueType.STRING) 
									? "'"+ this.textValue +"'" : this.textValue;
		
		return this.name + " ="+ value;
	}
	
	public void setValueByText(String text){
		
		if(setValueToEventData(text)) this.textValue = text;
	}
	
	public void setChoicedValue(int index, String text){
		
		if(setValueToEventData(String.valueOf(index))) {
			this.textValue = text;
			this.attribID = index;
		}
	}

	private void setValueFromEventData(Object targetLayer){
		
		switch (entry.valueType){
		
		case STRING:
		case NUMBER:
		case BOOLEAN:
			textValue = getReflectedSimpleValue(targetLayer);
			break;
			
		case INT2VECTOR_ELEMENT:
		case DOUBLE2VECTOR_ELEMENT:
			textValue = getReflectedVectorValue(targetLayer).toString();
			break;
			
		case EVENT_CATEGORY:
			attribID = ((EventData)targetLayer).eventCategory.getID();
			textValue = ((EventData)targetLayer).eventCategory.name();
			break;
		
		default:
			textValue = "null";
		}
	}
	
	private String getReflectedSimpleValue(Object object){
		
		Class<?> clazz = object.getClass();
		
		try{
		
			Field field = clazz.getDeclaredField(entry.name);
			
			return field.get(object).toString();
		
		}catch(ReflectiveOperationException e){
		
		}
		return "null";
	}
	
	private Object getReflectedVectorValue(Object object){
		
		String[] parts = entry.name.split("_");
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
	
	private boolean setValueToEventData(String text){
		
		
		changeEventData(eventData,text);
		
		return true;
	}
	
	private boolean changeEventData(Object targetLayer,String text){
		
		switch (entry.valueType){
		
		case STRING:
		case NUMBER:
		case BOOLEAN:
			return setReflectedSimpleEventData(targetLayer, text);
		
		case INT2VECTOR_ELEMENT:
		case DOUBLE2VECTOR_ELEMENT:
			return setReflectedVectorEventData(targetLayer, text);
			
		case EVENT_CATEGORY:
			((EventData)targetLayer).eventCategory
				= EventCategory.getFromID(Integer.valueOf(text));
			return true;
		
		default:
		}
		return false;
	}
	
	private boolean setReflectedSimpleEventData(Object object, String text){
		
		Class<?> clazz = object.getClass();
		
		try{
		
			Field field = clazz.getDeclaredField(entry.name);
			
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
	
	private boolean setReflectedVectorEventData(Object object, String text){
		
		String[] parts = entry.name.split("_");
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
