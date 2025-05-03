package utils;

import javafx.scene.image.Image;

public class Assets {
    public static Image loadImage(String name) {
        return new Image(Assets.class.getResource("/" + name).toExternalForm());
    }
}
