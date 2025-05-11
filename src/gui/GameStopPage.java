package gui;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import config.GameConfig;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import logic.GameScene;
import utils.ButtonUtils;
import utils.SoundManager;

public class GameStopPage {
	private Boolean restarted = false;

	public static Scene create(Stage stage, Runnable onResume, int currentMapIndex) {
		StackPane root = new StackPane();
		VBox overlay = new VBox(10);
		overlay.setAlignment(Pos.CENTER);
		overlay.setMaxWidth(250);
		overlay.setMaxHeight(200);

		ImageView Pausetext = new ImageView("gamepause/pausetext.png");
		Pausetext.setFitWidth(400);
		Pausetext.setFitHeight(200);

		VBox.setMargin(Pausetext, new Insets(0, 0, 5, 0));
		
		// create resume button
		Button resumeButton = ButtonUtils.createButton("gameover/resume.png", GameConfig.OTHER_WIDTH,
				GameConfig.OTHER_HEIGHT);
		resumeButton.setOnAction(e -> onResume.run());
		ButtonUtils.addHoverEffect(resumeButton);
		
		// create restart button
		Button restartButton = ButtonUtils.createButton("gameover/restart.png", GameConfig.OTHER_WIDTH,
				GameConfig.OTHER_HEIGHT);
		restartButton.setOnAction(e -> {
			SoundManager.stopAllSounds();
			Canvas canvas = new Canvas(1244, 700);
        	GameScene gameScene = new GameScene(canvas,stage);
        	
        	StackPane newroot = new StackPane(canvas,gameScene.getTempMessage());
        	gameScene.getTempMessage().setTranslateY(150);
        	StackPane.setAlignment(gameScene.getTempMessage(), Pos.TOP_CENTER);
            Scene scene = new Scene(newroot);
            
            stage.setScene(scene);
            gameScene.start(scene);
            
            gameScene.showTemporaryMessage("Map 1 : The Castle");
		});
		ButtonUtils.addHoverEffect(restartButton);
		
		// create menu button
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

		if (currentMapIndex == 0) {
			backgroundImage = new Image("/map/BG_1.png");
		} else if (currentMapIndex == 1) {
			backgroundImage = new Image("/map/terrace.png");
		} else if (currentMapIndex == 2) {
			backgroundImage = new Image("/map/BG_2.png");
		} else if (currentMapIndex == 3) {
			backgroundImage = new Image("/map/terrace.png");
		} else if (currentMapIndex == 4) {
			backgroundImage = new Image("/map/BG_4.png");
		} else {
			backgroundImage = null;
		}

		ImageView backgroundImageView = new ImageView(backgroundImage);
		backgroundImageView.setPreserveRatio(false);
		backgroundImageView.fitWidthProperty().bind(root.widthProperty());
		backgroundImageView.fitHeightProperty().bind(root.heightProperty());
		
		overlay.getChildren().addAll(Pausetext, resumeButton, restartButton, menuButton);
		
		root.getChildren().add(backgroundImageView);
		root.getChildren().add(overlay); 
		
		Scene scene = new Scene(root, 1244, 700); 
		
		scene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.E) {
				onResume.run();
			}
		});

		return scene;

	}
	
	public Boolean getRestarted() {
		return restarted;
	}

	public void setRestarted(Boolean restarted) {
		this.restarted = restarted;
	}

}

