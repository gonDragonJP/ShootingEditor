package shootingEditor.database;

import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import shootingEditor.texture.TextureSheet;

public class AccessOfTextureData {
	
	private static String databaseDir = AbsoluteDirectryHolder.getTextureDBPath();
	
	//private static final String databaseDir = ".\\texDataBase\\";
	private static final String databasePath = databaseDir + "texDB.db";
	private static final String imageDir = databaseDir + "image\\";
	
	private static final String basicTextureTableName = "TextureTable_Stage_";
	public static int stage = 1;
	
	private AccessOfTextureData(){
	
	}
	
	public static String getTexImageDir(){
		
		return imageDir;
	}
	
	private static String getTableName(){
		
		return basicTextureTableName + String.valueOf(stage);
	}
	
	public static void setTexDataList(List<TextureSheet> texSheetList, int stageNumber){
		
		SQLiteManager.initDatabase(databasePath);
		
		String sql;
		ResultSet resultSet;
		
		sql = "select * from "+getTableName()+" order by textureID;";
		resultSet = SQLiteManager.getResultSet(sql);
		 
		try {
			while(resultSet.next()){
				
				texSheetList.add(generateTexSheet(resultSet));
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		SQLiteManager.closeDatabase();
	}
	
	private static TextureSheet generateTexSheet(ResultSet resultSet){
		
		TextureSheet TextureSheet = new TextureSheet();
		
		setTextureSheet(TextureSheet, resultSet);
		
		return TextureSheet;
	}
	
	private static void setTextureSheet(TextureSheet textureSheet, ResultSet resultSet){
		
		try {
			textureSheet.textureID = resultSet.getInt("textureID");
			textureSheet.pictureName = resultSet.getString("pictureName");
			textureSheet.gridSizeX = resultSet.getInt("gridSizeX");
			textureSheet.gridSizeY = resultSet.getInt("gridSizeY");
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
}

