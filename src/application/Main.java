package application;

import gui.GameMenu;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		GameMenu menuScene = new GameMenu(primaryStage);
		
		primaryStage.setTitle("Jolepopo");
	    primaryStage.setScene(menuScene.getScene());
	    primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
