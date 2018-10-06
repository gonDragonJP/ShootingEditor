package shootingEditor.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import shootingEditor.stage.EventData;
import shootingEditor.stage.StageData;

public class AccessOfEventData {
	
	private static String databasePath = AbsoluteDirectryHolder.getStageManagerDBPath();
		
	public static void setEventList(ArrayList<EventData> eventList, int stage){
		
		SQLiteManager.initDatabase(databasePath);
		
		String sql;
		ResultSet resultSet;
		
		sql = "select * from EventData_Stage_"
				+String.valueOf(stage)+" order by scrollPoint;";
		resultSet = SQLiteManager.getResultSet(sql);
		 
		try {
			while(resultSet.next()){
				
				eventList.add(generateEventData(resultSet));
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		SQLiteManager.closeDatabase();
	}
	
	private static EventData generateEventData(ResultSet resultSet){
		
		EventData eventData = new EventData();
		
		setEventData(eventData, resultSet);
		
		return eventData;
	}
	
	private static void setEventData(EventData eventData, ResultSet resultSet){
		
		try {
			eventData.setDatabaseID(resultSet.getInt("ID"));
			
			eventData.scrollPoint = resultSet.getInt("scrollPoint");
			eventData.eventCategory = EventData.EventCategory.getFromID
											(resultSet.getInt("EventCategory"));
			eventData.eventObjectID = resultSet.getInt("eventObjectID");
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}
	
	public static void addEventList(ArrayList<EventData> eventList, int stage){
		
		SQLiteManager.initDatabase(databasePath);
		
		for(EventData e: eventList){
			
			add(e, stage);
		}
		
		SQLiteManager.closeDatabase();
	}
	
	public static void addEventData(EventData eventData, int stage){
		
		SQLiteManager.initDatabase(databasePath);
			
		add(eventData, stage);
		
		SQLiteManager.closeDatabase();
	}
	
	private static void add(EventData eventData, int stage){
		
		String sql = "insert into EventData_Stage_"
				+String.valueOf(stage)+" values(";
		
		sql += "NULL,";
		sql += String.valueOf(eventData.scrollPoint) +",";
		sql += String.valueOf(eventData.eventCategory.getID()) +",";
		sql += String.valueOf(eventData.eventObjectID);
		
		sql += ");";
		
		System.out.println(sql);
		
		SQLiteManager.update(sql);
	}
	
	public static void addNewEventData(int stage){
		
		SQLiteManager.initDatabase(databasePath);
		
		add(generateNewEventData(), stage);
		
		SQLiteManager.closeDatabase();
	}
	
	private static EventData generateNewEventData(){
		
		EventData eventData = new EventData();
		eventData.initialize();
		
		return eventData;
	}
	
	public static void deleteEventData(EventData eventData, int stage){
		
		SQLiteManager.initDatabase(databasePath);
		
		String sql = "delete from EventData"
				+String.valueOf(stage)+" where ID=";
		
		sql += String.valueOf(eventData.getDatabaseID());
		
		sql += ";";
		
		System.out.println(sql);
		
		SQLiteManager.update(sql);
		
		SQLiteManager.closeDatabase();
	}
}
