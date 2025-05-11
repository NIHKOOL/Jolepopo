package utils;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ButtonUtils {
	public static void addHoverEffect(Button button) {
		button.setOnMouseEntered(e -> {
			button.setStyle("-fx-background-color: #beb9b9;");
			button.setCursor(Cursor.HAND);
		});
		button.setOnMouseExited(e -> {
			button.setStyle("-fx-background-color: transparent;");
			button.setCursor(Cursor.DEFAULT);
		});
	}
	
	public static Button createButton(String string, int width, int height) {
		Image image = new Image(string); 
		ImageView imgView = new ImageView(image);
		imgView.setFitWidth(width); // optional resizing
		imgView.setFitHeight(height);
		
		Button newbutton = new Button();
		newbutton.setGraphic(imgView);	
		newbutton.setStyle("-fx-background-color: transparent;");
		
		return newbutton;
	}
	
}
