package shootingEditor.texture;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import shootingEditor.database.AccessOfTextureData;

public class TextureInitializer {
	
	public static TextureSheet[] getStageEnemyTexSheets(int stageNumber){
		
		// Array��sheet�������ׂɃ��X�g��db����ǂ݂���sheetList�͔z��ɃZ�b�g������A�j�����܂�
		// Array�Y����db��Aindex���ߕs���������������Œ�`����Ă��Ȃ��\�����l���ď璷�ɒ�`���Ă��܂�
		// index1000�ԑ�͔w�i�p�̃e�N�X�`���Ȃ̂ŏ��O���Ē�`���܂�
		
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
			
			if(e.textureID<1000)	// index1000�ԑ�͔w�i�p�e�N�X�`���ł�
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
