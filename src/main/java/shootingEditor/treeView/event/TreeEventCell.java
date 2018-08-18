package shootingEditor.treeView.event;
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
import shootingEditor.stage.EventData;
import shootingEditor.treeView.TreeContent;
import shootingEditor.treeView.TreeContent.ContentCategory;
import shootingEditor.treeView.event.TreeEventEntry.ValueType;

public class TreeEventCell extends TreeCell<TreeContent>{
	
	public TextField textField;
	public ChoiceBox<String> choiceBox;
	private Control editControl;
	
	public TreeEventCell(){
		super();
		
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
			
			TreeEventEntry data = (TreeEventEntry)getItem();
			
			switch(data.entry.valueType){
			
			case BOOLEAN:
			case EVENT_CATEGORY:
				
				if (choiceBox == null) createChoiceBox();
				choiceBox.getSelectionModel().select(data.attribID);
				editControl = choiceBox;
				break;
		
			default:
				if (textField == null) createTextField();
				textField.setText(data.textValue);
				editControl = textField;
			}
			
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
	
	private void createChoiceBox(){
		
		TreeEventEntry entry = (TreeEventEntry) getItem();
		
		choiceBox = new ChoiceBox<>();
		setChoiceBoxItems(entry.entry.valueType);
		choiceBox.getSelectionModel().selectedItemProperty().addListener
		(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				
				int index = choiceBox.getSelectionModel().getSelectedIndex();
				entry.setChoicedValue(index, newValue);
			}	
		});
	}
	
	private void setChoiceBoxItems(ValueType type){
		
		ArrayList<String> itemList = new ArrayList<>();
		
		switch(type){
		
		case BOOLEAN:
			itemList.add("true"); itemList.add("false");
			break;
			
		case EVENT_CATEGORY:
			for(EventData.EventCategory e: EventData.EventCategory.values()){
				itemList.add(e.name());
			}
			break;
		default:
		}
		
		choiceBox.getItems().addAll(itemList);
	}
	
	private void createTextField(){
		
		TreeEventEntry entry = (TreeEventEntry) getItem();
		
		textField = new TextField(entry.textValue);
		textField.setOnKeyReleased(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent event) {
				
				KeyCode code = event.getCode();
				
				if(code == KeyCode.ENTER){
					
					entry.setValueByText(textField.getText());
					commitEdit(entry);
					
				}else if (code == KeyCode.ESCAPE){
					
					cancelEdit();
				}
			}	
		});
	}

}
