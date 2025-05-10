package main;

import config.GameConfig;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class GameApp extends Application {
	
    public static final int WIDTH = GameConfig.SCREEN_WIDTH; //1244
    public static final int HEIGHT = GameConfig.SCREEN_HEIGHT; //700

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GameScene gameScene = new GameScene(canvas);

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root);

        stage.setTitle("Jolepopo");
        stage.setScene(scene);
        stage.show();

        gameScene.start(scene);
    }

    public static void main(String[] args) {
        launch();
    }
}
