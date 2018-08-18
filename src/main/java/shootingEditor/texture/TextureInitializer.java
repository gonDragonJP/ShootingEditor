package shootingEditor.texture;

import java.util.ArrayList;
import java.util.List;
import shootingEditor.database.AccessOfTextureData;

public class TextureInitializer {
	
	public static TextureSheet[] getStageEnemyTexSheets(int stageNumber){
		
		// �z���sheet�������ׂɃ��X�g��db����ǂ݂���sheetList�͔z��ɃZ�b�g������A�j�����܂�
		// �z���db��Aindex���ߕs���������������Œ�`����Ă��Ȃ��\�����l���ď璷�ɒ�`���Ă��܂�
		
		List<TextureSheet> texSheetList = new ArrayList<TextureSheet>();
		AccessOfTextureData.setTexDataList(texSheetList, stageNumber);
		if (texSheetList.size() ==0) return null;
		
		int maxIndex = getMaxIndex(texSheetList);
		TextureSheet[] sheets = new TextureSheet[maxIndex + 1];
		for(TextureSheet e: texSheetList){
			
			e.initialize();
			sheets[e.textureID] = new TextureSheet(e);
		}
		
		return sheets;
	}
	
	private static int getMaxIndex(List<TextureSheet> list){
		
		int result = 0;
		for(TextureSheet e: list){
			
			result = Math.max(result, e.textureID);
		}
		return result;
	}
}
