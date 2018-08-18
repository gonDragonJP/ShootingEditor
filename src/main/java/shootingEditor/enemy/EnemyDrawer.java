package shootingEditor.enemy;
import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import shootingEditor.Global;
import shootingEditor.animation.AnimationData;
import shootingEditor.enemy.CollisionRegion;
import shootingEditor.enemy.Enemy;
import shootingEditor.enemy.EnemyData.EnemyCategory;
import shootingEditor.stage.StageData;
import shootingEditor.texture.TextureSheet;
import shootingEditor.vector.Double2Vector;
import shootingEditor.vector.Int2Vector;
import shootingEditor.vector.IntRect;

public class EnemyDrawer {
	
	static private final int drawRadius = 5;
	static private final int drawAngleLineLength = 20;
	
	static private GraphicsContext context;
	static private boolean isEnableTex = false;
	
	public static void setContext(Canvas canvas){
		
		context = canvas.getGraphicsContext2D();
	}
	
	public static void setEnableTex(boolean isEnable){
		
		isEnableTex = isEnable;
	}
	
	public static void onDrawIfGrounder(Enemy enemy){
		
		Color color = getEnemyColor(Color.SANDYBROWN, enemy.getCategory());
		
		if(enemy.isGrounder)
			if(!isEnableTex || !onDrawWithTex(enemy)) onDraw(enemy,color);
	}
	
	public static void onDrawIfAir(Enemy enemy){
		
		Color color = getEnemyColor(Color.CORNFLOWERBLUE, enemy.getCategory());
		
		if(!enemy.isGrounder) 
			if(!isEnableTex || !onDrawWithTex(enemy)) onDraw(enemy,color);
	}
	
	private static Color getEnemyColor(Color defaultColor, EnemyCategory category){
		
		Color resultColor = defaultColor;
		
		if (category == EnemyCategory.PARTS) resultColor = Color.BLUEVIOLET;
		
		return resultColor;
	}
	
	static void onDraw(Enemy enemy, Color color){
		
		context.setFill(color);
		context.fillOval
			(enemy.x-drawRadius, enemy.y-drawRadius, drawRadius*2, drawRadius*2);
		
		context.setStroke(color);
		drawCollisionRegions(enemy);
		
		context.setStroke(Color.WHITE);
		drawFaceOnLine(enemy);
	}
	
	static void drawCollisionRegions(Enemy enemy){
		
		ArrayList<CollisionRegion> list = enemy.collisionRotated;
		
		for(CollisionRegion e: list){
			
			int colX = enemy.x + e.centerX;
			int colY = enemy.y + e.centerY;
			int colRadius = e.size;
	
			context.strokeOval
				(colX-colRadius, colY-colRadius, colRadius*2, colRadius*2);
		}
	}
	
	static void drawFaceOnLine(Enemy enemy){
		
		Double2Vector vec = enemy.getUnitVectorOfFaceOn();
		
		int px = enemy.x + (int)(drawAngleLineLength *vec.x);
		int py = enemy.y + (int)(drawAngleLineLength *vec.y);
		
		context.strokeLine(enemy.x, enemy.y, px, py);
	}
	
	static boolean onDrawWithTex(Enemy enemy){
		
		AnimationData anime  = enemy.getCurrentAnimeData();
		int textureID = anime.textureID;
		int frameIndex = anime.frameOffset + enemy.animeFrame;
		double drawSizeX = anime.drawSize.x;
		double drawSizeY = anime.drawSize.y;
		
		TextureSheet sheet = StageData.textureSheets[textureID];
		if(sheet == null) return false;
		Image img = sheet.texImage;
		
		IntRect texRect = sheet.getTexRect(frameIndex); // テクスチャ座標取得
		
		setAffine(enemy.x, enemy.y, drawSizeX, drawSizeY, enemy.drawAngle);
			
		context.drawImage(
					img, texRect.left, texRect.top, sheet.gridSizeX, sheet.gridSizeY, 
					0, 0, drawSizeX, drawSizeY
				);
		
		toIdentityAffine();
		return true;
	}
	
	private static Affine affine = new Affine();
	
	static void setAffine(int x, int y, double sizeX, double sizeY, double angle){
		
		//affineのappendは数学の式のように左から行列を書き並べる感じで記述します	
		affine.appendTranslation(x, y);
		affine.appendRotation(angle);
		affine.appendTranslation(-sizeX/2, -sizeY/2);
		
		context.setTransform(affine);
	}
	
	static void toIdentityAffine(){
		
		affine.setToIdentity();
		context.setTransform(affine);
	}
}
