package shootingEditor.treeView.event;

import java.util.Set;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import shootingEditor.MainApp;
import shootingEditor.animation.AnimationSet;
import shootingEditor.animation.AnimationSet.AnimeKind;
import shootingEditor.stage.EventData;
import shootingEditor.treeView.TreeContent;
import shootingEditor.treeView.TreeContent.ContentCategory;
import shootingEditor.treeView.event.TreeEventEntry.Entry;


public class EventTreeUtil {
	
	private static TreeView<TreeContent> treeView;
	private static EventData eventData;
	
	public static void addeventTree(TreeView<TreeContent> argTreeView, EventData argeventData){
		
		eventData = argeventData;
		treeView = argTreeView;
		
		TreeItem<TreeContent> root;
		
		root = new TreeItem<>();
		root.setValue(new TreeContent("EVENT", TreeContent.ContentCategory.FIXED_GROUP));
		root.setExpanded(true);
		
		addData(root);
		
		treeView.setRoot(root);
	}
	
	private static void addData(TreeItem<TreeContent> argRoot){
		
		TreeContent treeData;
		TreeItem<TreeContent> root, treeItem;
		
		root = argRoot;
		
		treeData = new TreeContent("EventData", TreeContent.ContentCategory.FIXED_GROUP);
		treeItem = new TreeItem<TreeContent>(treeData);
		treeItem.setExpanded(true);
		root.getChildren().add(treeItem);
		
		root = treeItem;
		
		for(Entry e : Entry.values()){
		
			if(e.category == TreeEventEntry.EntryCategory.EVENT_DATA){
				
				treeData = new TreeEventEntry(eventData, e);
				treeItem = new TreeItem<TreeContent>(treeData);
				root.getChildren().add(treeItem);
			}
		}
	}
}
