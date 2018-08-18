package shootingEditor.test;
import javafx.scene.canvas.Canvas;
import shootingEditor.enemy.CollisionRegion;
import shootingEditor.enemy.Enemy;
import shootingEditor.enemy.EnemyDrawer;

public class DrawTest {
	
	static Enemy enemy = new Enemy(null, null);
	static CollisionRegion col = new CollisionRegion();
	
	static void test(Canvas canvas){
		
		enemy.x = 50;
		enemy.y = 50;
		
		col.centerX = 0;
		col.centerY = 0;
		col.size = 50;
		
		enemy.collisionRotated.add(col);
		
		EnemyDrawer.setContext(canvas);
		EnemyDrawer.onDrawIfAir(enemy);
	}
	
}
