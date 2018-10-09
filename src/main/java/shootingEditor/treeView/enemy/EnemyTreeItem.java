package shootingEditor.treeView.enemy;

import javafx.scene.control.TreeItem;
import shootingEditor.enemy.EnemyData;
import shootingEditor.enemy.EnemyData.MutableCategory;
import shootingEditor.treeView.TreeContent;
import shootingEditor.treeView.enemy.TreeEnemyGroup.GroupType;

public class EnemyTreeItem extends TreeItem<TreeContent>{
	
	public EnemyTreeItem(TreeContent treeContent){
		super(treeContent);
		
	}
	
	public void addNode(){
		
		TreeEnemyGroup group = ((TreeEnemyGroup)getValue());
		EnemyData enemyData = group.enemyData;
		
		MutableCategory mutableCategory = null;
		GroupType childGroupType = null;
		
		System.out.print(group.groupType.toString());
		
		switch(group.groupType){
		
		case MOVING:
			mutableCategory = MutableCategory.MOVING;
			childGroupType = GroupType.MOVING_CHILD;
			break;
		case GENERATOR:
			mutableCategory = MutableCategory.GENERATOR;
			childGroupType = GroupType.GENERATOR_CHILD;
			break;
		case COLLISION:	
			mutableCategory = MutableCategory.COLLISION;
			childGroupType = GroupType.COLLISION_CHILD;
			break;
		case NODE_ANIME:
			addNodeAnimeNode();
			
		default:
			return;
		}
		
		enemyData.generateNewNode(mutableCategory);
		
		int childCount = this.getChildren().size();
		EnemyTreeUtil.addAChild(this, childGroupType, childCount);
		
		setExpanded(true);
	}
	
	private void addNodeAnimeNode(){
		
		TreeEnemyGroup group = ((TreeEnemyGroup)getValue());
		EnemyData enemyData = group.enemyData;
		
		int keyNode = enemyData.getMinFreeKeyNode();
		enemyData.generateNewNodeActionAnime(keyNode);
		
		int childCount = this.getChildren().size();
		EnemyTreeUtil.addChildOfNodeAnime(this, childCount, keyNode);
		
		setExpanded(true);
	}
	
	public void deleteNode(){
		
		TreeEnemyGroup group = ((TreeEnemyGroup)getValue());
		EnemyData enemyData = group.enemyData;
		int index = group.childIndex;
		
		MutableCategory mutableCategory = null;

		switch(group.groupType){
		
		case MOVING_CHILD:
			mutableCategory = MutableCategory.MOVING;
			break;
		case GENERATOR_CHILD:
			mutableCategory = MutableCategory.GENERATOR;
			break;
		case COLLISION_CHILD:	
			mutableCategory = MutableCategory.COLLISION;
			break;
		case NODEANIME_CHILD:
			mutableCategory = MutableCategory.NODE_ACTION_ANIME;
			index = group.keyNode;
		default:
		}
		
		enemyData.deleteNode(mutableCategory, index);
		numberingOfChildIndex();
		getParent().getChildren().remove(this);
	}
	
	private void numberingOfChildIndex(){
		
		int index=0;
		for(TreeItem<TreeContent> e : getParent().getChildren()){
			
			if(!e.equals(this)){
			
				TreeEnemyGroup childGroup = (TreeEnemyGroup)e.getValue();
				childGroup.childIndex =index++;
			}	
		}
	}
	
}
