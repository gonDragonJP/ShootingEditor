package shootingEditor.treeView.enemy.content;

import java.util.Map;

import javafx.util.Callback;
import shootingEditor.animation.AnimationData;

public class NodeAnimeKeyContent extends EntryContent{

	private NodeAnimeKeyContent(String name, ContentCategory category) {
		super(name, category);
	}

	int keyNode;
	Callback<Integer, Boolean> setKeyToGroup;
	
	public static EntryContent create
	(int keyNode, Map<Integer,AnimationData> animeMap, Callback<Integer, Boolean> setKeyToGroup){
		
		NodeAnimeKeyContent content = 
				new NodeAnimeKeyContent("KeyNode",ContentCategory.ENTRY);
		
		content.referObject = animeMap;
		content.keyNode = keyNode;
		content.valueText = String.valueOf(keyNode);
		content.setKeyToGroup = setKeyToGroup;
		
		return content;
	}
	
	@Override
	public void setValueByText(String text) {
		
		int newKeyNode = Integer.valueOf(text);
		if(changeKeyNode(newKeyNode)) {
			
			keyNode = newKeyNode;
			valueText = text;
			setKeyToGroup.call(keyNode);
		}
	}
	
	private boolean changeKeyNode(int newKeyNode){
		
		Map<Integer,AnimationData> animeMap = (Map<Integer,AnimationData>)referObject;
		AnimationData currentAnimeData = animeMap.get(keyNode);
		
		if(animeMap.containsKey(newKeyNode)) return false;
		
		animeMap.put(newKeyNode, currentAnimeData);
		animeMap.remove(keyNode);
		
		return true;
	}
}
