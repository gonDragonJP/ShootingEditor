package shootingEditor.texture;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import shootingEditor.database.AccessOfTextureData;

public class TextureInitializer {
	
	public static TextureSheet[] getStageEnemyTexSheets(int stageNumber){
		
		// Arrayでsheetを扱う為にリストでdbから読みこんだsheetListは配列にセットした後、破棄します
		// Array添字はdb上、indexが過不足無い序数順序で定義されていない可能性を考えて冗長に定義しています
		// index1000番台は背景用のテクスチャなので除外して定義します
		
		List<TextureSheet> texSheetList = new ArrayList<TextureSheet>();
		AccessOfTextureData.setTexDataList(texSheetList, stageNumber);
		if (texSheetList.size() ==0) return null;
		
		int maxIndex = getMaxIndexOfEnemyTex(texSheetList);
		TextureSheet[] sheets = new TextureSheet[maxIndex+1];
		for(TextureSheet e: texSheetList){
			
			if(e.textureID<1000) {
				e.initialize();
				sheets[e.textureID] = new TextureSheet(e);
			}
		}
		
		return sheets;
	}
	
	private static int getMaxIndexOfEnemyTex(List<TextureSheet> list){
		
		int result = 0;
		for(TextureSheet e: list){
			
			if(e.textureID<1000)	// index1000番台は背景用テクスチャです
				result = Math.max(result, e.textureID);
		}
		return result;
	}
	
	public static TextureSheet[] getBackgroundTexSheets(int stageNumber){
		
		List<TextureSheet> texSheetList = new ArrayList<TextureSheet>();
		AccessOfTextureData.setTexDataList(texSheetList, stageNumber);
		if (texSheetList.size() ==0) return null;
		
		Predicate<TextureSheet> p = new Predicate<TextureSheet>() {

			@Override
			public boolean test(TextureSheet t) {
				
				return t.textureID<1000;
			}};
		
		Comparator<TextureSheet> c = new Comparator<TextureSheet>() {

			@Override
			public int compare(TextureSheet o1, TextureSheet o2) {
				
				return o1.textureID - o2.textureID;
			}};
			
		texSheetList.removeIf(p);
		texSheetList.sort(c);
		
		TextureSheet[] sheets = new TextureSheet[texSheetList.size()];
		for(int i=0; i<texSheetList.size(); i++){
			
			TextureSheet e = texSheetList.get(i);
			e.initialize();
			sheets[i] = new TextureSheet(e);
		}
		
		return sheets;
	}
}
