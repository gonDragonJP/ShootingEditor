package shootingEditor.treeView.enemy;

import java.util.Set;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import shootingEditor.MainApp;
import shootingEditor.animation.AnimationData;
import shootingEditor.animation.AnimationSet;
import shootingEditor.animation.AnimationSet.AnimeKind;
import shootingEditor.enemy.EnemyData;
import shootingEditor.treeView.TreeContent;
import shootingEditor.treeView.TreeContent.ContentCategory;
import shootingEditor.treeView.enemy.TreeEnemyGroup.GroupType;
import shootingEditor.treeView.enemy.content.AnimationNodeContent;
import shootingEditor.treeView.enemy.content.BasicDataContent;
import shootingEditor.treeView.enemy.content.CollisionNodeContent;
import shootingEditor.treeView.enemy.content.GeneratorNodeContent;
import shootingEditor.treeView.enemy.content.MovingNodeContent;
import shootingEditor.treeView.enemy.content.NodeAnimeKeyContent;
import shootingEditor.treeView.enemy.TreeEnemyEntry.EntryCategory;
import shootingEditor.treeView.enemy.TreeEnemyEntry.Entry;

public class EnemyTreeUtil {
	
	private static TreeView<TreeContent> treeView;
	private static EnemyData enemyData;
	
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
	
	private static void addBasicData(TreeItem<TreeContent> argRoot){
		
		TreeContent treeData;
		TreeItem<TreeContent> root, treeItem;
		
		root = argRoot;
		
		treeData = new TreeContent("BasicData", TreeContent.ContentCategory.FIXED_GROUP);
		treeItem = new TreeItem<TreeContent>(treeData);
		treeItem.setExpanded(true);
		root.getChildren().add(treeItem);
		
		root = treeItem;
		
		for(BasicDataContent.Fields e : BasicDataContent.Fields.values()) {
			
			treeData = BasicDataContent.create(e, enemyData);
			treeItem = new TreeItem<TreeContent>(treeData);
			root.getChildren().add(treeItem);
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
				treeItem = new EnemyTreeItem(treeData);
				root.getChildren().add(treeItem);
				
				addGroupChild(treeItem, e);
				break;
				
			case ANIMATION:
				treeData = new TreeEnemyGroup(enemyData,e);
				treeItem = new TreeItem<TreeContent>(treeData);
				root.getChildren().add(treeItem);
				
				addAnimationSubGroups(treeItem);
				break;
				
			default:
			}	
		}
	}
	
	private static void addAnimationSubGroups(TreeItem<TreeContent> root){
		
		TreeContent treeData;
		TreeItem<TreeContent> treeItem;
		
		AnimationSet animeSet = enemyData.animationSet;
		
		if (animeSet==null) return;
			
		treeData = new TreeEnemyGroup(enemyData, GroupType.NORMAL_ANIME);
		treeItem = new TreeItem<TreeContent>(treeData);
		root.getChildren().add(treeItem);
		addAnimationItem(treeItem, animeSet.normalAnime);
		
		treeData = new TreeEnemyGroup(enemyData, GroupType.EXPLOSION_ANIME);
		treeItem = new TreeItem<TreeContent>(treeData);
		root.getChildren().add(treeItem);
		addAnimationItem(treeItem, animeSet.explosionAnime);
		
		treeData = new TreeEnemyGroup(enemyData, GroupType.NODE_ANIME);
		treeItem = new EnemyTreeItem(treeData);
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
		
		TreeEnemyGroup  childGroup = new TreeEnemyGroup(enemyData, groupType, index);
		EnemyTreeItem treeItem = new EnemyTreeItem(childGroup);
		root.getChildren().add(treeItem);
		
		switch(groupType){
		
		case MOVING_CHILD:
			for(MovingNodeContent.Fields e : MovingNodeContent.Fields.values()){
				treeItem.getChildren().add(new TreeItem<>(
						MovingNodeContent.create(e, enemyData.node.get(index))));
			}
			break;
			
		case GENERATOR_CHILD:
			for(GeneratorNodeContent.Fields e : GeneratorNodeContent.Fields.values()){
				treeItem.getChildren().add(new TreeItem<>(
					GeneratorNodeContent.create(e, enemyData.generator.get(index))));
			}
			break;
			
		case COLLISION_CHILD:
			for(CollisionNodeContent.Fields e : CollisionNodeContent.Fields.values()){
				treeItem.getChildren().add(new TreeItem<>(
					CollisionNodeContent.create(e, enemyData.collision.get(index))));
			}
			break;
			
		default:
		}
	}
	
	private static void addAnimationItem(TreeItem<TreeContent> root, AnimationData data){
		// normalAnime, explosionAnime といった単一ノードのデータをグループに追加します
		
		for(AnimationNodeContent.Fields e : AnimationNodeContent.Fields.values()) {
			
			root.getChildren().add(new TreeItem<>(
					AnimationNodeContent.create(e, data)));
		}
	}
	
	private static void addGroupChildsOfNodeAnime(TreeItem<TreeContent> root){
		
		Set<Integer> keySet = enemyData.animationSet.nodeActionAnime.keySet();
		int childIndex=0;
		
		for(int e: keySet){
			
			addChildOfNodeAnime(root, childIndex, e);
			
			childIndex++;
		}
	}
	
	public static void addChildOfNodeAnime(TreeItem<TreeContent> root, int childIndex, int keyNode){
		
		TreeEnemyGroup childGroup;
		TreeItem<TreeContent> treeItem;
		
		childGroup = new TreeEnemyGroup
				(enemyData, TreeEnemyGroup.GroupType.NODEANIME_CHILD, childIndex, keyNode);
		treeItem = new EnemyTreeItem(childGroup);
		root.getChildren().add(treeItem);
		
		addItemOfNodeAnime(treeItem, keyNode);
	}
	
	
	private static void addItemOfNodeAnime
	(TreeItem<TreeContent> root, int keyNode){
		// nodeIndex番目の　nodeActionAnime のデータを　keyNode編集用のitemと共に追加します

		root.getChildren().add(new TreeItem<>(
				NodeAnimeKeyContent.create(keyNode)));
		
		AnimationData nodeAnime = enemyData.animationSet.nodeActionAnime.get(keyNode);
		
		for(AnimationNodeContent.Fields e : AnimationNodeContent.Fields.values()) {
			
			root.getChildren().add(new TreeItem<>(
					AnimationNodeContent.create(e, nodeAnime)));
		}
	}
}
