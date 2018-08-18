package shootingEditor;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TreeView;
import shootingEditor.database.AccessOfEnemyData;
import shootingEditor.database.AccessOfEventData;
import shootingEditor.enemy.EnemyData;
import shootingEditor.stage.EventData;
import shootingEditor.stage.StageData;
import shootingEditor.treeView.TreeContent;
import shootingEditor.treeView.enemy.EnemyTreeUtil;
import shootingEditor.treeView.enemy.TreeEnemyCell;
import shootingEditor.treeView.event.EventTreeUtil;
import shootingEditor.treeView.event.TreeEventCell;

public class TreeModule {
	
	private CallbackOfMainApp cbOfMainApp;
	
	private TreeView<TreeContent> treeView;
	
	private EnemyData tempEnemyDataForEdit = new EnemyData();
	private EventData tempEventDataForEdit = new EventData();
	
	public TreeModule(MainApp mainApp){
		
		cbOfMainApp = mainApp;
		this.treeView = mainApp.treeView;
	}
	
	public void setEnemyTree(EnemyData enemyData){
		
		tempEnemyDataForEdit.copy(enemyData);
		
		treeView.setCellFactory(treeView -> new TreeEnemyCell());
			// cellはすべてのitemに対して作られるので単一のインスタンス変数を渡すだけではダメです
			// このコールバックはツリーを作ると複数回呼び出されます
		EnemyTreeUtil.addEnemyTree(treeView, tempEnemyDataForEdit);
	}
	
	public void setEventTree(EventData eventData){
		
		tempEventDataForEdit.copy(eventData);
		
		treeView.setCellFactory(treeView -> new TreeEventCell());
		EventTreeUtil.addeventTree(treeView, tempEventDataForEdit);
	}

	public void testTreeEnemy(){
		
		if (tempEnemyDataForEdit == null) return;
		
		GameTestModule gameTestModule = cbOfMainApp.getgameTestModule();
		gameTestModule.testEnemy(tempEnemyDataForEdit);
	}
	
	public void storeEnemyDataToDB(){
		
		int id = tempEnemyDataForEdit.objectID;
		boolean isExist = AccessOfEnemyData.checkExistSameObjectID(id);
		
		if(isExist){
			if (!checkOverwritePermission()) return;
			
			AccessOfEnemyData.deleteEnemyData(tempEnemyDataForEdit);;
		}
		
		AccessOfEnemyData.addEnemyData(tempEnemyDataForEdit);
		
		GameTestModule gameTestModule = cbOfMainApp.getgameTestModule();
		gameTestModule.refreshEnemyList();
		
		TableModule tableModule = cbOfMainApp.getTableModule();
		tableModule.setEnemyTableData(StageData.enemyList);
		tableModule.scrollEnemyTableTo(id);
	}
		
	private boolean checkOverwritePermission(){	
		
		Alert alertBox = new Alert(Alert.AlertType.CONFIRMATION);
		int id = tempEnemyDataForEdit.objectID;
		
		alertBox.setHeaderText("Object (ID: "+id+") already exists");
		alertBox.setContentText("Wish to overwrite the object ?");
		Optional<ButtonType> result = alertBox.showAndWait();
		
		if(result.get() == ButtonType.OK) return true;
		
		return false;
	}
	
	public void storeEventDataToDB(){
		
		AccessOfEventData.addEventData(tempEventDataForEdit);
		
		GameTestModule gameTestModule = cbOfMainApp.getgameTestModule();
		gameTestModule.refreshEventList();
		
		TableModule tableModule = cbOfMainApp.getTableModule();
		tableModule.setEventTableData(StageData.eventList);
	}
}
