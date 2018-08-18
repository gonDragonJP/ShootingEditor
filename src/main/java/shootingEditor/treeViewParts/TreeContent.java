package shootingEditor.treeViewParts;

public class TreeContent {
	
	public enum ContentCategory{
		
		FIXED_GROUP,
		MUTABLE_GROUP,
		CHILD_GROUP,
		ITEM
	}
	
	public String name;
	public ContentCategory category;

	public TreeContent(String name){
		
		this(name, ContentCategory.ITEM);
	}
	
	public TreeContent(String name, ContentCategory category){
		
		this.name = name;
		this.category = category;
	}
	
	@Override
	public String toString(){
		
		return this.name;
	}
}
