package shootingEditor.treeView.enemy.content;

public class NodeAnimeKeyContent extends EntryContent{

	private NodeAnimeKeyContent(String name, ContentCategory category) {
		super(name, category);
	}

	int keyNode;
	
	public static EntryContent create
	(int keyNode){
		
		NodeAnimeKeyContent content = 
				new NodeAnimeKeyContent("KeyNode",ContentCategory.ENTRY);
	
		content.keyNode = keyNode;
		content.valueText = String.valueOf(keyNode);
		
		return content;
	}
	
	public void setValueTextByEnemyData(){
		
	}
	
	public boolean setEnemyDataByAText(String text) {
		
		return false;
	}
}
