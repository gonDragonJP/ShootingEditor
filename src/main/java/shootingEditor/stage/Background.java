package shootingEditor.stage;

import com.sun.istack.internal.logging.Logger;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import shootingEditor.animation.AnimationData;
import shootingEditor.enemy.Enemy;
import shootingEditor.texture.TextureSheet;
import shootingEditor.vector.Double2Vector;
import shootingEditor.vector.IntRect;

public class Background {

	private Background(){

    }

    private static Double2Vector drawSize = new Double2Vector();
    private static TextureSheet drawSheet;

    private static int pictureNumber, pictureLength;
    private static int scrollPosition, pictureIndex;
    
    private static GraphicsContext graphicsContext;
    
    public static void setGraphicsContext(Canvas canvas) {
    	
    	graphicsContext = canvas.getGraphicsContext2D();
    	drawSize.set(canvas.getWidth(), canvas.getHeight());
    	pictureLength = (int)canvas.getHeight();
    }

    public static void initStage(){

        pictureNumber = StageData.backgroundTexSheets.length;

        scrollPosition =0;
    }

    public static void onDraw(int scrollPoint){

        calcScroll(scrollPoint);

        //�܂��ꖇ�`�o
        drawSheet = StageData.backgroundTexSheets[pictureIndex];
        drawOnePicture(scrollPosition);

        // �����X�N���[������ɕ`��
        int preIndex = (pictureIndex+1)==pictureNumber ? 0 : pictureIndex +1;
        drawSheet = StageData.backgroundTexSheets[preIndex];
        drawOnePicture(scrollPosition - pictureLength);
    }
    
    private static void drawOnePicture(int position){
		
		IntRect texRect = drawSheet.getTexRect(0); // �e�N�X�`�����W�擾
		Image img = drawSheet.texImage;
		
		graphicsContext.drawImage(
					img, texRect.left, texRect.top, drawSheet.gridSizeX, drawSheet.gridSizeY, 
					0, position, drawSize.x, drawSize.y
				);
	}

    private static void calcScroll(int scrollPoint){

        int wholePictureLength = pictureLength * pictureNumber;

        scrollPosition = scrollPoint % pictureLength;
        pictureIndex = (scrollPoint / pictureLength) % pictureNumber;
        System.out.println(pictureIndex);
    }
}
