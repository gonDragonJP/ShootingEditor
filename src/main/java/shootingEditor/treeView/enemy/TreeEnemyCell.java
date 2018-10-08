package shootingEditor.treeView.enemy;
import java.util.ArrayList;
import java.util.Set;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import shootingEditor.animation.AnimationData.RepeatAttribute;
import shootingEditor.animation.AnimationData.RotateAttribute;
import shootingEditor.enemy.CollisionRegion;
import shootingEditor.enemy.EnemyData;
import shootingEditor.enemy.EnemyData.MutableCategory;
import shootingEditor.treeView.ReflectUtil;
import shootingEditor.treeView.TreeContent;
import shootingEditor.treeView.TreeContent.ContentCategory;
import shootingEditor.treeView.enemy.TreeEnemyEntry.ValueType;
import shootingEditor.treeView.enemy.content.EntryContent;

public class TreeEnemyCell extends TreeCell<TreeContent>{
	
	public TextField textField;
	public ChoiceBox<String> choiceBox;
	private Control editControl;
	
	public TreeEnemyCell(){
		super();
		
		this.setOnMouseClicked(e-> onMouseClicked(e));
		
	}
	
	@Override
	public void updateItem(TreeContent data, boolean isEmpty){
		
		super.updateItem(data, isEmpty);
		
		if(isEmpty){
			
			setText(null);
			setGraphic(null);
			
		}else if(isEditing()){
			
			setText(null);
			setGraphic(editControl);
		}else{
			
			setText(data.toString());
			setGraphic(null);
		}
	}
	
	@Override
	public void startEdit(){
		
		super.startEdit();
		
		if(getItem().category == TreeContent.ContentCategory.ENTRY) {
			
			EntryContent data = (EntryContent)getItem();
			
			/*
			switch(data.entry.valueType){
			
			case BOOLEAN:
			case STARTPOSITION_ATTRIB:
			case STARTVELOCITY_ATTRIB:
			case STARTACCELERATION_ATTRIB:
			case ANI_REPEAT_ATTRIB:
			case ANI_ROTATE_ATTRIB:
			case COLLISION_SHAPE:
				if (choiceBox == null) createChoiceBox();
				choiceBox.getSelectionModel().select(data.attribID);
				editControl = choiceBox;
				break;
		
			default:
				if (textField == null) createTextField();
				textField.setText(data.textValue);
				editControl = textField;
			}*/
			
			System.out.println(ReflectUtil.getSuperType
					(data.referObject, data.fieldName));
			
			if (textField == null) createTextField();
			textField.setText(data.valueText);
			editControl = textField;
			
			setText(null);
			setGraphic(editControl);
		}
	}
	
	@Override
	public void cancelEdit(){
		
		super.cancelEdit();
		
		setText(getItem().toString());
		setGraphic(null);
	}
	
	@Override
	public void commitEdit(TreeContent treeContent){
		
		super.commitEdit(treeContent);
		
		this.getTreeView().refresh();
	}
	
	public void onMouseClicked(MouseEvent e){
		
		//outputNodeAnimeMap();
		
		if(e.getButton() != MouseButton.SECONDARY) return;
		
		ContentCategory category = getItem().category;
		
		if(category == ContentCategory.MUTABLE_GROUP 
			|category == ContentCategory.CHILD_GROUP) createContextMenu(category);
	}
	
	private void outputNodeAnimeMap(){ //デバッグ用メソッド
		
		if(getItem().category != ContentCategory.CHILD_GROUP) return;
		
		TreeEnemyGroup group = (TreeEnemyGroup)getItem();
		EnemyData enemyData = group.enemyData;
		
		Set<Integer> keySet = enemyData.animationSet.nodeActionAnime.keySet();
		int childIndex=0;
		
		for(int e: keySet){
			
			System.out.printf("index %d   key %d \n", childIndex, e);
			
			childIndex++;
		}
	}
	
	private void createContextMenu(ContentCategory category){
		
		ContextMenu contextMenu = new ContextMenu();
		MenuItem menuItem = null;
		
		switch(category){
		
		case MUTABLE_GROUP:
			menuItem = new MenuItem("NewNode");
			menuItem.setOnAction(e -> addNode());
			break;
			
		case CHILD_GROUP:
			menuItem = new MenuItem("Delete");
			menuItem.setOnAction(e -> deleteNode());
			break;
			
		default:
		}
		contextMenu.getItems().add(menuItem);
		this.setContextMenu(contextMenu);
	}
	
	private void addNode(){
		
		((EnemyTreeItem)getTreeItem()).addNode();
	}
	
	private void deleteNode(){
		
		((EnemyTreeItem)getTreeItem()).deleteNode();
	}
	
	private void createChoiceBox(){
		
		EntryContent item = (EntryContent) getItem();
		
		choiceBox = new ChoiceBox<>();
		//setChoiceBoxItems(item.fieldName);
		choiceBox.getSelectionModel().selectedItemProperty().addListener
		(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				
				int index = choiceBox.getSelectionModel().getSelectedIndex();
				item.setChoicedValue(index, newValue);
			}	
		});
	}
	
	/*private void setChoiceBoxItems(String fieldName){
		
		ArrayList<String> itemList = new ArrayList<>();
		
		switch(type){
		
		case BOOLEAN:
			itemList.add("true"); itemList.add("false");
			break;
		case STARTPOSITION_ATTRIB:
			for(EnemyData.StartPositionAtrib e: EnemyData.StartPositionAtrib.values()){
				itemList.add(e.name());
			}
			break;
		case STARTVELOCITY_ATTRIB:
		case STARTACCELERATION_ATTRIB:
			for(EnemyData.StartVectorAtrib e: EnemyData.StartVectorAtrib.values()){
				itemList.add(e.name());
			}
			break;
		case ANI_REPEAT_ATTRIB:
			for(RepeatAttribute e: RepeatAttribute.values()){
				itemList.add(e.name());
			}
			break;
		case ANI_ROTATE_ATTRIB:
			for(RotateAttribute e: RotateAttribute.values()){
				itemList.add(e.name());
			}
			break;
		case COLLISION_SHAPE:
			for(CollisionRegion.CollisionShape e: CollisionRegion.CollisionShape.values()){
				itemList.add(e.name());
			}
			break;
		}
		
		choiceBox.getItems().addAll(itemList);
	}*/
	
	private void createTextField(){
		
		EntryContent item = (EntryContent) getItem();
		
		textField = new TextField(item.valueText);
		textField.setOnKeyReleased(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent event) {
				
				KeyCode code = event.getCode();
				
				if(code == KeyCode.ENTER){
					
					item.setValueByText(textField.getText());
					commitEdit(item);
					
				}else if (code == KeyCode.ESCAPE){
					
					cancelEdit();
				}
			}	
		});
	}

}
