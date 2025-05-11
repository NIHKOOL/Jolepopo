package gui;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.GameScene;
import utils.SoundManager;

public class StoryPage {
    private Scene scene;
    
    public StoryPage(Stage stage) {
        Text animatedLabel = new Text();
        
        // botton create
        Image exit = new Image("gamemenu/next.png"); // place in resources or same folder
		ImageView imgView02 = new ImageView(exit);
		imgView02.setFitWidth(200); // optional resizing
		imgView02.setFitHeight(60);
		
		Button nextButton = new Button();
		nextButton.setVisible(false);
		nextButton.setGraphic(imgView02);	
		nextButton.setStyle("-fx-background-color: transparent;");
		
		addHoverEffect(nextButton);
		
        nextButton.setOnAction(e -> {
        	Canvas canvas = new Canvas(1244, 700);
        	GameScene gameScene = new GameScene(canvas,stage);
        	
        	StackPane root = new StackPane(canvas,gameScene.getTempMessage());
        	gameScene.getTempMessage().setTranslateY(150);
        	StackPane.setAlignment(gameScene.getTempMessage(), Pos.TOP_CENTER);
            Scene scene = new Scene(root);
            
            stage.setScene(scene);
            gameScene.start(scene);
            
            gameScene.showTemporaryMessage("Map 1 : The Castle");
			// You can switch to your game scene here
		});
		
        // create root
        AnchorPane buttonPane = new AnchorPane(nextButton);
        AnchorPane.setBottomAnchor(nextButton, 20.0);
        AnchorPane.setRightAnchor(nextButton, 20.0);

        // วางข้อความตรงกลางจริงๆ โดยไม่ใช้ VBox
        StackPane centerTextPane = new StackPane(animatedLabel);
        centerTextPane.setPrefSize(1244, 700);  // ขนาดเท่ากับหน้าจอ
        centerTextPane.setStyle("-fx-alignment: center;");

        StackPane root = new StackPane();
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        root.getChildren().addAll(centerTextPane, buttonPane);

        scene = new Scene(root, 1244, 700);
		
        SoundManager.playBGM("musics/Keyboard_typing.mp3",1);
        
		// setlabel
        animatedLabel.setStyle("-fx-font-size: 40px;");
        animatedLabel.setTextAlignment(TextAlignment.CENTER);
        animatedLabel.setFill(Color.WHITE);
        animatedLabel.setFont(Font.font("TH Chakra Petch", FontWeight.BOLD, 28));
        animateText(animatedLabel, "กาลครั้งหนึ่งนานมาแล้ว\nณ เมืองสงบสุขแห่งหนึ่ง ผู้คนต่างอยู่ด้วยกันอย่างมีความสุข\nแต่ทันใดนั้นเองก็นางปีศาจร้ายบุกเข้ามา\nนางปีศาจร้ายได้สร้างความแตกแยกให้บ้านเมืองของเรา"
        		+ "\nมาร่วมออกเดินทางจัดการนางปีศาจร้ายนี้กันเถอะ!!", nextButton);
        
        
    }

    private void animateText(Text textNode, String content, Button showButtonAfter) {
        final StringBuilder displayedText = new StringBuilder();
        Timeline timeline = new Timeline();
        Duration delay = Duration.millis(50); // เดิม set เป็น 50
        
        for (int i = 0; i < content.length(); i++) {
            final int index = i;
            KeyFrame frame = new KeyFrame(delay.multiply(i), e -> {
                displayedText.append(content.charAt(index));
                textNode.setText(displayedText.toString());
            });
            timeline.getKeyFrames().add(frame);
        }
        
        // Show button after animation ends
        timeline.setOnFinished(e -> {SoundManager.stopBGM();showButtonAfter.setVisible(true);});
        timeline.play();
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
        return scene;
    }
    
}