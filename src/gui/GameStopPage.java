package gui;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import logic.GameScene;
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
		Image resume = new Image("gameover/resume.png");
		ImageView resumebutton = new ImageView(resume);
		resumebutton.setFitWidth(200);
		resumebutton.setFitHeight(80);
		Button resumeButton = new Button();
		resumeButton.setGraphic(resumebutton);
		resumeButton.setStyle("-fx-background-color: transparent;");
		resumeButton.setOnAction(e -> onResume.run());
		addHoverEffect(resumeButton);
		
		// create restart button
		Image restartImage = new Image("gameover/restart.png");
		ImageView restartIcon = new ImageView(restartImage);
		restartIcon.setFitWidth(200);
		restartIcon.setFitHeight(80);
		Button restartButton = new Button();
		restartButton.setGraphic(restartIcon);
		restartButton.setStyle("-fx-background-color: transparent;");
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
		addHoverEffect(restartButton);
		
		// create quit button
		Image quitImage = new Image("gameover/menu.png");
		ImageView quitIcon = new ImageView(quitImage);
		quitIcon.setFitWidth(200);
		quitIcon.setFitHeight(80);
		Button quitButton = new Button();
		quitButton.setGraphic(quitIcon);
		quitButton.setStyle("-fx-background-color: transparent;");
		quitButton.setOnAction(e -> {
			SoundManager.stopAllSounds();
			GameMenu newpage = new GameMenu(stage);
			stage.setScene(newpage.getScene());
		});
		addHoverEffect(quitButton);

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
		
		overlay.getChildren().addAll(Pausetext, resumeButton, restartButton, quitButton);
		
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

	private static void addHoverEffect(Button button) {
		button.setOnMouseEntered(e -> {
			button.setStyle("-fx-background-color: #beb9b9;");
			button.setCursor(Cursor.HAND);
		});
		button.setOnMouseExited(e -> {
			button.setStyle("-fx-background-color: transparent;");
			button.setCursor(Cursor.DEFAULT);
		});
	}
	
	public Boolean getRestarted() {
		return restarted;
	}

	public void setRestarted(Boolean restarted) {
		this.restarted = restarted;
	}

}

