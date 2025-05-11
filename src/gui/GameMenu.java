package gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.ButtonUtils;
import utils.SoundManager;
import config.GameConfig;
import javafx.geometry.Pos;

public class GameMenu {
	private Scene Scene;

	public GameMenu(Stage primaryStage) {
		// create start button
		Button startButton = ButtonUtils.createButton("gamemenu/start.png",GameConfig.MENU_WIDTH,GameConfig.MENU_HEIGHT);
		ButtonUtils.addHoverEffect(startButton);

		// create how_to_play button
		Button howtoplayButton = ButtonUtils.createButton("gamemenu/howtoplay.png",GameConfig.MENU_WIDTH,GameConfig.MENU_HEIGHT);
		ButtonUtils.addHoverEffect(howtoplayButton);

		//create quit button
		Button quitbutton = ButtonUtils.createButton("gamemenu/quit.png",GameConfig.MENU_WIDTH,GameConfig.MENU_HEIGHT);
		ButtonUtils.addHoverEffect(quitbutton);

		// import background
		Image Logo = new Image("gamemenu/logo.png");
		ImageView logoImageView = new ImageView(Logo);
		logoImageView.setFitHeight(250);
		logoImageView.setFitWidth(240);

		Image backgroundImage = new Image("gamemenu/homepage.png");
		ImageView backgroundImageView = new ImageView(backgroundImage);
		backgroundImageView.setPreserveRatio(false);
		backgroundImageView.setFitWidth(1244);
		backgroundImageView.setFitHeight(700);

		// set sound
		SoundManager.playBGM("musics/Menupage_sound.mp3", 0.3);

		// set button actions
		startButton.setOnAction(e -> {
			SoundManager.stopBGM();
			StoryPage secondPage = new StoryPage(primaryStage);
			primaryStage.setScene(secondPage.getScene());
		});
		
		howtoplayButton.setOnAction(e -> {
			HowToPlayPage optionPage = new HowToPlayPage(primaryStage, Scene);
			primaryStage.setScene(optionPage.getScene());
		});

		quitbutton.setOnAction(e -> {
			primaryStage.close();
		});

		// Layout container
		VBox menuLayout = new VBox(10);
		menuLayout.setAlignment(Pos.CENTER);
		menuLayout.getChildren().addAll(logoImageView, startButton, howtoplayButton, quitbutton);

		// StackPane for layering background and UI
		StackPane root = new StackPane();
		root.getChildren().addAll(backgroundImageView, menuLayout);

		// create Scene
		this.Scene = new Scene(root, 1244, 700);

		// Bind background size to scene size (dynamic resizing)
		backgroundImageView.fitWidthProperty().bind(Scene.widthProperty());
		backgroundImageView.fitHeightProperty().bind(Scene.heightProperty());

	}

	public Scene getScene() {
		return this.Scene;
	}

	public void setScene(Scene menuScene) {
		this.Scene = menuScene;
	}

}