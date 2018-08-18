package shootingEditor.treeViewParts;

import java.util.Set;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import shootingEditor.MainApp;
import shootingEditor.animation.AnimationSet;
import shootingEditor.animation.AnimationSet.AnimeKind;
import shootingEditor.enemy.EnemyData;
import shootingEditor.treeViewParts.TreeEnemyGroup.GroupType;
import shootingEditor.treeViewParts.TreeEnemyItem.ItemCategory;
import shootingEditor.treeViewParts.TreeEnemyItem.ItemType;

public class MyTreeUtil {
	
	private static EnemyData enemyData;
	private static TreeView<TreeContent> treeView;
	
	public static void addEnemyTree(TreeView<TreeContent> argTreeView, EnemyData argEnemyData){
		
		enemyData = argEnemyData;
		treeView = argTreeView;
		
		TreeItem<TreeContent> root;
		
		root = new TreeItem<>();
		root.setValue(new TreeContent("ENEMY", TreeContent.ContentCategory.FIXED_GROUP));
		root.setExpanded(true);
		
		addBasicData(root);
		addGroups(root);
		
		treeView.setRoot(root);
	}
	
	public static void refresh(){
		
		treeView.refresh();
	}
	
	private static void addBasicData(TreeItem<TreeContent> argRoot){
		
		TreeContent treeData;
		TreeItem<TreeContent> root, treeItem;
		
		root = argRoot;
		
		treeData = new TreeContent("BasicData", TreeContent.ContentCategory.FIXED_GROUP);
		treeItem = new TreeItem<TreeContent>(treeData);
		treeItem.setExpanded(true);
		root.getChildren().add(treeItem);
		
		root = treeItem;
		
		for(ItemType e : ItemType.values()){
		
			if(e.category == TreeEnemyItem.ItemCategory.BASIC_DATA){
				
				treeData = new TreeEnemyItem(null, enemyData, e);
				treeItem = new TreeItem<TreeContent>(treeData);
				root.getChildren().add(treeItem);
			}
		}
	}
	
	private static void addGroups(TreeItem<TreeContent> root){
		
		TreeContent treeData;
		TreeItem<TreeContent> treeItem;
		
		for(GroupType e : GroupType.values()){
			
			switch(e){
			
			case MOVING:
			case GENERATOR:
			case COLLISION:
				treeData = new TreeEnemyGroup(enemyData,e);
				treeItem = new MyTreeItem(treeData);
				
				addGroupChild(treeItem, e);
				
				root.getChildren().add(treeItem);
				break;
				
			case ANIMATION:
				treeData = new TreeEnemyGroup(enemyData,e);
				treeItem = new TreeItem<TreeContent>(treeData);
				
				addAnimationGroup(treeItem);
				
				root.getChildren().add(treeItem);
				break;
				
			default:
			}	
		}
	}
	
	private static void addAnimationGroup(TreeItem<TreeContent> root){
		
		TreeContent treeData;
		TreeItem<TreeContent> treeItem;
		
		AnimationSet animeSet = enemyData.animationSet;
		
		if (animeSet==null) return;
			
		treeData = new TreeEnemyGroup(enemyData, GroupType.NORMAL_ANIME);
		treeItem = new TreeItem<TreeContent>(treeData);
		root.getChildren().add(treeItem);
		addAnimationItem(treeItem, AnimeKind.NORMAL);
		
		treeData = new TreeEnemyGroup(enemyData, GroupType.EXPLOSION_ANIME);
		treeItem = new TreeItem<TreeContent>(treeData);
		root.getChildren().add(treeItem);
		addAnimationItem(treeItem, AnimeKind.EXPLOSION);
		
		treeData = new TreeEnemyGroup(enemyData, GroupType.NODE_ANIME);
		treeItem = new MyTreeItem(treeData);
		root.getChildren().add(treeItem);
		addGroupChild(treeItem, GroupType.ANIMATION);
	}
	
	private static void addGroupChild(TreeItem<TreeContent> root, GroupType groupType){
	
		GroupType childGroupType;
		
		switch(groupType){
		
		case MOVING:
			childGroupType = GroupType.MOVING_CHILD;	
			addMutableGroupChilds(root, childGroupType, enemyData.node.size());
			break;
			
		case GENERATOR:
			childGroupType = GroupType.GENERATOR_CHILD;	
			addMutableGroupChilds(root, childGroupType, enemyData.generator.size());
			break;
			
		case COLLISION:
			childGroupType = GroupType.COLLISION_CHILD;
			addMutableGroupChilds(root, childGroupType, enemyData.collision.size());
			break;
			
		case ANIMATION:
			addGroupChildsOfNodeAnime(root);
			break;
			
		default:
		}
	}
	
	private static void addMutableGroupChilds
		(TreeItem<TreeContent> root, GroupType groupType, int childCount){
		
		for(int i=0; i<childCount ;i++){
			
			addAChild(root, groupType, i);
		}
	}
	
	public static void addAChild
	(TreeItem<TreeContent> root, GroupType groupType, int index){
		
		TreeEnemyGroup  childGroup = new TreeEnemyGroup(enemyData, groupType, index, -1);
		MyTreeItem treeItem = new MyTreeItem(childGroup);
		root.getChildren().add(treeItem);
		
		ItemCategory itemCategory = null;
		
		switch(groupType){
		
		case MOVING_CHILD:	itemCategory = ItemCategory.MOVING_DATA;
			break;
			
		case GENERATOR_CHILD:	itemCategory = ItemCategory.GENERATOR_DATA;
			break;
			
		case COLLISION_CHILD:	itemCategory = ItemCategory.COLLISION_DATA;
			break;
		default:
		}
			
		addMutableItem(treeItem, itemCategory);
	}
	
	private static void addMutableItem
		(TreeItem<TreeContent> root, ItemCategory itemCategory){

		TreeEnemyItem item;
		TreeItem<TreeContent> treeItem;
		
		for(ItemType e : ItemType.values()){
		
			if(e.category == itemCategory){
		
				item = new TreeEnemyItem((TreeEnemyGroup)root.getValue(), enemyData, e);
				treeItem  = new TreeItem<TreeContent>(item);
				root.getChildren().add(treeItem);
			}
		}
	}
	
	private static void addAnimationItem(TreeItem<TreeContent> root, AnimeKind animeKind){
		// normalAnime, explosionAnime といった単一ノードのデータをグループに追加します
		
		TreeEnemyItem item;
		TreeItem<TreeContent> treeItem;
	
		for(ItemType e : ItemType.values()){
	
			if(e.category == ItemCategory.ANIME_DATA){
			
				item = new TreeEnemyItem(enemyData, e, animeKind);
				treeItem = new TreeItem<TreeContent>(item);
				root.getChildren().add(treeItem);
			}
		}
	}
	
	private static void addGroupChildsOfNodeAnime(TreeItem<TreeContent> root){
		
		Set<Integer> keySet = enemyData.animationSet.nodeActionAnime.keySet();
		int childIndex=0;
		
		for(int e: keySet){
			
			addAChildOfNodeAnime(root, childIndex, e);
			
			childIndex++;
		}
	}
	
	public static void addAChildOfNodeAnime(TreeItem<TreeContent> root, int childIndex, int keyNode){
		
		TreeEnemyGroup childGroup;
		TreeItem<TreeContent> treeItem;
		
		childGroup = new TreeEnemyGroup(enemyData, TreeEnemyGroup.GroupType.NODEANIME_CHILD, childIndex, keyNode);
		treeItem = new MyTreeItem(childGroup);
		root.getChildren().add(treeItem);
		
		addItemOfNodeAnime(treeItem);
	}
	
	private static void addItemOfNodeAnime(TreeItem<TreeContent> root){
		// nodeIndex番目の　nodeActionAnime のデータを　keyNode編集用のitemと共に追加します

		TreeEnemyItem item;
		TreeItem<TreeContent> treeItem;
	
		item = new TreeEnemyItem((TreeEnemyGroup)root.getValue(), enemyData, ItemType.ANI_NODEANIME_KEY);
		treeItem  = new TreeItem<TreeContent>(item);
		root.getChildren().add(treeItem);
		
		for(ItemType e : ItemType.values()){
			
			if(e.category == ItemCategory.ANIME_DATA){
				
				item = new TreeEnemyItem((TreeEnemyGroup)root.getValue(), enemyData, e);
				treeItem  = new TreeItem<TreeContent>(item);
				root.getChildren().add(treeItem);
			}
		}
	}
}
