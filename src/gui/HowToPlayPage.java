package gui;

import javafx.scene.paint.Color;
import config.GameConfig;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utils.ButtonUtils;
import utils.SoundManager;

public class HowToPlayPage {
	private Scene scene;

	public HowToPlayPage(Stage stage, Scene mainMenuScene) {
		//create background
		Image backgroundImage = new Image("gamemenu/night.png");
		ImageView backgroundImageView = new ImageView(backgroundImage);
		backgroundImageView.setPreserveRatio(false);
		backgroundImageView.setFitWidth(800);
		backgroundImageView.setFitHeight(600);

		// Label หัวข้อ
		Text titleLabel = new Text("How to play");
        titleLabel.setFont(Font.font("Impact", FontWeight.BOLD, 50));
        titleLabel.setFill(Color.WHITE);
        
		// first Hbox
		HBox hbox01 = new HBox(10);
		ImageView A = createimageview("letter/A.png");
		ImageView D = createimageview("letter/D.png");
		Text movement = createtext("Press A or D to move left and move right");
		hbox01.setAlignment(Pos.CENTER);
		hbox01.getChildren().addAll(A, D, movement);
		
		// second Hbox
		HBox hbox02 = new HBox(10);
		ImageView W = createimageview("letter/W.png");
		Text jump = createtext("Press W to jump");
		hbox02.setAlignment(Pos.CENTER);
		hbox02.getChildren().addAll(W, jump);
				
		// third Hbox
		HBox hbox03 = new HBox(10);
		ImageView K = createimageview("letter/K.png");
		ImageView L = createimageview("letter/L.png");
		Text skills = createtext("Press K or L or Shift to Use skills");
		hbox03.setAlignment(Pos.CENTER);
		hbox03.getChildren().addAll(K, L, skills);
		
		// fourth Hbox
		HBox hbox04 = new HBox(10);
		ImageView H = createimageview("letter/H.png");
		ImageView P = createimageview("letter/P.png");
		Text Heal = createtext("Press H or P to Heal and Enchated");
		hbox04.setAlignment(Pos.CENTER);
		hbox04.getChildren().addAll(H, P, Heal);
		
		// fifth Hbox
		HBox hbox05 = new HBox(10);
		ImageView E = createimageview("letter/E.png");
		Text pause = createtext("Press E to pause");
		hbox05.setAlignment(Pos.CENTER);
		hbox05.getChildren().addAll(E, pause);
		
		// sixth Hbox
		HBox hbox06 = new HBox(10);
		ImageView TAB = createimageview("letter/TAB.jpg");
		TAB.setFitWidth(90);
		Text character = createtext("Press TAB to Switch character");
		hbox06.setAlignment(Pos.CENTER);
		hbox06.getChildren().addAll(TAB, character);
		
		// seventh Hbox
		HBox hbox07 = new HBox(10);
		ImageView ENTER = createimageview("letter/ENTER.png");
		ENTER.setFitWidth(75);
		Text changemap = createtext("Press ENTER to Change Map when kill all monsters \nand walk to the end of the map");
		hbox07.setAlignment(Pos.CENTER);
		hbox07.getChildren().addAll(ENTER, changemap);
		
		// Create content layout
		VBox page2Layout = new VBox(15);
		page2Layout.setAlignment(Pos.CENTER);
		
		// Button action
		Button backButton = ButtonUtils.createButton("gamemenu/back.png", GameConfig.MENU_WIDTH,
				GameConfig.MENU_HEIGHT);
		
		ButtonUtils.addHoverEffect(backButton);
		backButton.setOnAction(_ -> {
			stage.setScene(mainMenuScene);
			SoundManager.playSEF("effects/sharp-pop-328170.mp3", 0.3);
		});
		
		page2Layout.getChildren().addAll(titleLabel, hbox01, hbox02, hbox03, hbox04, hbox05, hbox06, hbox07, backButton);

		// Use StackPane to layer background and content
		StackPane root = new StackPane();
		root.getChildren().addAll(backgroundImageView, page2Layout);

		// Create scene
		scene = new Scene(root, 1244, 700);

		// Optional: resize background when window resizes
		backgroundImageView.fitWidthProperty().bind(scene.widthProperty());
		backgroundImageView.fitHeightProperty().bind(scene.heightProperty());

	}
	
	public ImageView createimageview(String string) {
		Image image = new Image(string);
		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(40);
		imageView.setFitWidth(40);
		return imageView;
	}
	
	public Text createtext(String string) {
		Text text = new Text(string);
		text.setFont(Font.font("Segoe UI", 20));
        text.setFill(Color.WHITE);
		return text;
	}
	
	public Scene getScene() {
		return scene;
	}
}
