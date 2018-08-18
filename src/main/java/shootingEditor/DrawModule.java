package shootingEditor;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class DrawModule {
	
	private TextField scrollTextField;
	public Slider slider;
	private Canvas canvas;
	
	public DrawModule(MainApp mainApp){
		
		this.scrollTextField = mainApp.scrollTextField;
		this.slider = mainApp.slider;
		this.canvas = mainApp.canvas;
	}
	
	public void drawScreen(){
		
		scrollTextField.setText
				("Position : " + String.valueOf((int)slider.getValue()));
		
		clearScreen();
		drawCanvasGrid();
	}
	
	public void clearScreen(){
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.BLACK);
		gc.fillRect(0,0,MainApp.CanvasX, MainApp.CanvasY);
	}
	
	public void drawCanvasGrid(){
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		final int gridSize = 100;
		gc.setStroke(Color.ALICEBLUE);
		
		double sliderValue = slider.getValue();
		int startY = ((int)(sliderValue / gridSize)+1) * gridSize;
		
		for(int i=startY; i < startY + MainApp.CanvasY; i+=gridSize){
			
			double drawY = MainApp.CanvasY - (i - sliderValue);	
		
			gc.strokeLine(0, drawY, MainApp.CanvasX, drawY);
		}
	}
}
