package shootingEditor.treeViewParts;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class TreeCellImpl extends TreeCell<TreeContent>{
	
	public TextField textField;
	
	@Override
	public void updateItem(TreeContent data, boolean isEmpty){
		
		super.updateItem(data, isEmpty);
		
		if(isEmpty){
			
			setText(null);
			setGraphic(null);
			
		}else if(isEditing()){
			
			setText(null);
			setGraphic(textField);
		}else{
			
			setText(data.toString());
			setGraphic(null);
		}
	}
	
	@Override
	public void startEdit(){
		
		super.startEdit();
		
		if(getItem().category == TreeContent.ContentCategory.ITEM) {
		
			if (textField == null) createTextField();
		
			setText(null);
			setGraphic(textField);
		}
	}
	
	@Override
	public void cancelEdit(){
		
		super.cancelEdit();
		
		setText(getItem().toString());
		setGraphic(null);
	}
	
	public void createTextField(){
		
		textField = new TextField(getItem().toString());
		textField.setOnKeyReleased(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent event) {
				
				KeyCode code = event.getCode();
				
				if(code == KeyCode.ENTER){
					
					commitEdit(new TreeContent(textField.getText()));
					
				}else if (code == KeyCode.ESCAPE){
					
					cancelEdit();
				}
			}	
		});	
	}
}
