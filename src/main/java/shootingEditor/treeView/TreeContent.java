package shootingEditor.treeView;

public class TreeContent {
	
	public enum ContentCategory{
		
		FIXED_GROUP,
		MUTABLE_GROUP,
		CHILD_GROUP,
		ENTRY
	}
	
	public String name;
	public ContentCategory category;

	public TreeContent(String name){
		
		this(name, ContentCategory.ENTRY);
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
