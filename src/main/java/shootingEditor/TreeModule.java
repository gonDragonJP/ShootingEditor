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
	
	private MainApp mainApp;
	
	private EnemyData tempEnemyDataForEdit = new EnemyData();
	private EventData tempEventDataForEdit = new EventData();
	
	public TreeModule(MainApp mainApp){
		
		this.mainApp = mainApp;
	}
	
	public void setEnemyTree(EnemyData enemyData){
		
		tempEnemyDataForEdit.copy(enemyData);
		
		MainSceneUtil.treeView.setCellFactory(treeView -> new TreeEnemyCell());
			// cellはすべてのitemに対して作られるので単一のインスタンス変数を渡すだけではダメです
			// このコールバックはツリーを作ると複数回呼び出されます
		EnemyTreeUtil.addEnemyTree(MainSceneUtil.treeView, tempEnemyDataForEdit);
	}
	
	public void setEventTree(EventData eventData){
		
		tempEventDataForEdit.copy(eventData);
		
		TreeView<TreeContent> view = MainSceneUtil.treeView;
		view.setCellFactory(treeView -> new TreeEventCell());
		EventTreeUtil.addeventTree(view, tempEventDataForEdit);
	}

	public void testTreeEnemy(){
		
		if (tempEnemyDataForEdit == null) return;
		
		mainApp.gameTestModule.testEnemy(tempEnemyDataForEdit);
	}
	
	public void storeEnemyDataToDB(){
		
		int id = tempEnemyDataForEdit.objectID;
		boolean isExist = AccessOfEnemyData.checkExistSameObjectID(id);
		
		if(isExist){
			if (!checkOverwritePermission()) return;
			
			AccessOfEnemyData.deleteEnemyData(tempEnemyDataForEdit);;
		}
		
		AccessOfEnemyData.addEnemyData(tempEnemyDataForEdit);
		
		mainApp.gameTestModule.refreshEnemyList();
		
		mainApp.tableModule.setEnemyTableData(StageData.enemyList);
		mainApp.tableModule.scrollEnemyTableTo(id);
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
	
		mainApp.gameTestModule.refreshEventList();
		
		mainApp.tableModule.setEventTableData(StageData.eventList);
	}
}
