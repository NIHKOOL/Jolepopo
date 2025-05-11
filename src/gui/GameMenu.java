package gui;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.SoundManager;
import javafx.geometry.Pos;

public class GameMenu {
	private String start_button = "gamemenu/start.png";
	private String howtoplay_button = "gamemenu/howtoplay.png";
	private String quit_button = "gamemenu/quit.png";
	private Scene Scene;

	public GameMenu(Stage primaryStage) {
		Image start = new Image(start_button); // place in resources or same folder
		ImageView imgView = new ImageView(start);
		imgView.setFitWidth(250); // optional resizing
		imgView.setFitHeight(60);

		Button startButton = new Button();
		startButton.setGraphic(imgView);
		startButton.setStyle("-fx-background-color: transparent;");

		addHoverEffect(startButton);

// option button
		Image option = new Image(howtoplay_button); // place in resources or same folder
		ImageView imgView01 = new ImageView(option);
		imgView01.setFitWidth(250); // optional resizing
		imgView01.setFitHeight(60);

		Button optionsButton = new Button();
		optionsButton.setGraphic(imgView01);
		optionsButton.setStyle("-fx-background-color: transparent;");

		addHoverEffect(optionsButton);

// quit button
		Image exit = new Image(quit_button); // place in resources or same folder
		ImageView imgView02 = new ImageView(exit);
		imgView02.setFitWidth(250); // optional resizing
		imgView02.setFitHeight(60);

		Button exitButton = new Button();
		exitButton.setGraphic(imgView02);
		exitButton.setStyle("-fx-background-color: transparent;");

		// On hover: change style and cursor
		addHoverEffect(exitButton);

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

// Set button actions
		startButton.setOnAction(e -> {
			SoundManager.stopBGM();
			StoryPage secondPage = new StoryPage(primaryStage);
			primaryStage.setScene(secondPage.getScene());
			// You can switch to your game scene here
		});

		exitButton.setOnAction(e -> {
			primaryStage.close();
		});

		// Layout container
		VBox menuLayout = new VBox(10);
		menuLayout.setAlignment(Pos.CENTER);
		menuLayout.getChildren().addAll(logoImageView, startButton, optionsButton, exitButton);

		// StackPane for layering background and UI
		StackPane root = new StackPane();
		root.getChildren().addAll(backgroundImageView, menuLayout);

		// Scene
		// Scene menuScene = new Scene(root, 800, 600);
		this.Scene = new Scene(root, 1244, 700);

		optionsButton.setOnAction(e -> {
			HowToPlayPage optionPage = new HowToPlayPage(primaryStage, Scene); // pass the main menu scene
			primaryStage.setScene(optionPage.getScene());
		});

		// Bind background size to scene size (dynamic resizing)
		backgroundImageView.fitWidthProperty().bind(Scene.widthProperty());
		backgroundImageView.fitHeightProperty().bind(Scene.heightProperty());

	}

	private void addHoverEffect(Button button) {
		button.setOnMouseEntered(e -> {
			button.setStyle("-fx-background-color: #beb9b9;");
			button.setCursor(Cursor.HAND);
		});
		button.setOnMouseExited(e -> {
			button.setStyle("-fx-background-color: transparent;");
			button.setCursor(Cursor.DEFAULT);
		});
	}

	public Scene getScene() {
		return this.Scene;
	}

	public void setScene(Scene menuScene) {
		this.Scene = menuScene;
	}

}