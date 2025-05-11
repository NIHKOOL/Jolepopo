package gui;

import config.GameConfig;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import logic.GameScene;
import utils.ButtonUtils;
import utils.SoundManager;

public class GameOverPage {

	private Scene scene;
	private StackPane root;

	public GameOverPage(Stage stage, int index) {
		root = new StackPane(); 
		SoundManager.playSEFOnce("musics/failure_sound.mp3", 0.3);

		VBox overlay = new VBox(5);
		overlay.setAlignment(Pos.CENTER);
		overlay.setMaxWidth(250);
		overlay.setMaxHeight(200);

		// create death symbol
		ImageView Deathsymbol = new ImageView("gameover/Dealth symbol.png");
		Deathsymbol.setFitWidth(200);
		Deathsymbol.setFitHeight(210);
		
		// create death text
		ImageView Deathtext = new ImageView("gameover/gameover_text.png");
		Deathtext.setFitWidth(400);
		Deathtext.setFitHeight(75);
		
		VBox.setMargin(Deathsymbol, new Insets(0, 0, 5, 0));
		VBox.setMargin(Deathtext, new Insets(0));
		
		// create restart button
		Button restartButton = ButtonUtils.createButton("gameover/restart.png", GameConfig.OTHER_WIDTH,
				GameConfig.OTHER_HEIGHT);

		restartButton.setOnAction(e -> {
			SoundManager.stopAllSounds();
			Canvas canvas = new Canvas(1244, 700);
			GameScene gameScene = new GameScene(canvas, stage);

			StackPane root = new StackPane(canvas, gameScene.getTempMessage());
			gameScene.getTempMessage().setTranslateY(150);
			StackPane.setAlignment(gameScene.getTempMessage(), Pos.TOP_CENTER);
			Scene scene = new Scene(root);

			stage.setScene(scene);
			gameScene.start(scene);

			gameScene.showTemporaryMessage("Map 1 : The Castle");
		});
		ButtonUtils.addHoverEffect(restartButton);

		// create quit button
		Button menuButton = ButtonUtils.createButton("gameover/menu.png", GameConfig.OTHER_WIDTH,
				GameConfig.OTHER_HEIGHT);
		menuButton.setOnAction(e -> {
			SoundManager.stopAllSounds();
			GameMenu newpage = new GameMenu(stage);
			stage.setScene(newpage.getScene());
		});
		ButtonUtils.addHoverEffect(menuButton);

		// create background
		Image backgroundImage;
		if(index == 0) {
			backgroundImage = new Image("/map/BG_1.png");
		} else if(index == 2) {
			backgroundImage = new Image("/map/BG_2.png");
		} else if(index == 4) {
			backgroundImage = new Image("/map/BG_4.png");
		} else {
			backgroundImage = null;
		}
		ImageView backgroundImageView = new ImageView(backgroundImage);
		backgroundImageView.setPreserveRatio(false);
		backgroundImageView.fitWidthProperty().bind(root.widthProperty());
		backgroundImageView.fitHeightProperty().bind(root.heightProperty());

		VBox buttonBox = new VBox(10);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.getChildren().addAll(restartButton, menuButton);

		overlay.getChildren().addAll(Deathsymbol, Deathtext, buttonBox);

		root.getChildren().add(backgroundImageView);
		root.getChildren().add(overlay); 

		scene = new Scene(root, 1244, 700); 
	}

	public Scene getScene() {
		return scene;
	}
}

