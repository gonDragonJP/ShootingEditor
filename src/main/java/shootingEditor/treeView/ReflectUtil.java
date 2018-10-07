package shootingEditor.treeView;

import java.lang.reflect.Field;

public class ReflectUtil {
	
	public static String getFieldType(Object object, String fieldName) {
		
		Class clazz = object.getClass();
		Field fielz = null;
		
		try {
			fielz = clazz.getDeclaredField(fieldName);
			
		} catch (NoSuchFieldException | SecurityException e) {e.printStackTrace();}
		
		return fielz.getType().getName();
	}

	
	public static String getReflectedSimpleValue
	(Object object, String fieldName){
		
		Class<?> clazz = object.getClass();
		
		try{
		
			Field field = clazz.getDeclaredField(fieldName);
			
			return field.get(object).toString();
		
		}catch(ReflectiveOperationException e){
		
		}
		return "null";
	}
	
	public static String getReflectedVectorValue
	(Object object, String fieldName, String subFieldName){
		
		Class<?> clazz = object.getClass();
		
		try{
			Field field = clazz.getDeclaredField(fieldName);
			Object vectorObject = field.get(object);
		
		
			clazz = vectorObject.getClass();
			field = clazz.getDeclaredField(subFieldName);
			
			return field.get(vectorObject).toString();
			
		}catch(ReflectiveOperationException e){
			
		}
		return null;
	}
	
	public static boolean setReflectedSimpleEnemyData
	(Object object, String text, String fieldName){
		
		Class<?> clazz = object.getClass();
		
		try {
			Field field = clazz.getDeclaredField(fieldName);
			
			switch(field.getType().getName()){
			
			case "java.lang.String":
				field.set(object, text);	break;

			case "int":
				field.set(object, Integer.valueOf(text));	break;
				
			case "boolean": 
				field.set(object, Boolean.valueOf(text));	break;
				
			case "double":
				field.set(object, Double.valueOf(text));	break;
				
			default:
				return false;
			}
			
		}catch(Exception e) {return false;}
		
		return true;
	}
	
	public static boolean setReflectedVectorEnemyData
	(Object object, String text, String fieldName, String subFieldName){
		
		
		Class<?> clazz = object.getClass();
		
		try{
			Field field = clazz.getDeclaredField(fieldName);
			Object vectorObject = field.get(object);
		
			clazz = vectorObject.getClass();
			field = clazz.getDeclaredField(subFieldName);
			String typeName = getFieldType(vectorObject, subFieldName);
			
			switch(typeName) {
			
			case "int":
				field.set(vectorObject, Integer.valueOf(text));
				return true;
			case "double":
				field.set(vectorObject, Double.valueOf(text));
				return true;
			}
			
		}catch(Exception e){}
		
		return false;
	}
}
