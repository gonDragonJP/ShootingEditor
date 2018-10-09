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
import shootingEditor.treeView.enemy.content.EntryContent;
import shootingEditor.treeView.enemy.content.MultipleChoiceFields;
import shootingEditor.treeView.trash.TreeEnemyEntry.ValueType;

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
			
			if(MultipleChoiceFields.isMultipleChoiceField(data.fieldName)){
			 
				createChoiceBox();
				editControl = choiceBox;
			}else{
		
				if (textField == null) createTextField();
				textField.setText(data.valueText);
				editControl = textField;
			}
			
			setText(null);
			setGraphic(editControl);
			editControl.requestFocus();
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
		
		choiceBox.getItems().addAll(
				MultipleChoiceFields.getMultipleChoiceList(item.fieldName));
		
		choiceBox.getSelectionModel().selectedItemProperty().addListener
		(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				
				int index = choiceBox.getSelectionModel().getSelectedIndex();
				item.setChoicedValue(index, newValue);
				commitEdit(item);
			}	
		});
	}
	
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
