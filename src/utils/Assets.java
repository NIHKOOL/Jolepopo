package utils;

import javafx.scene.image.Image;

public class Assets {

    public static Image loadImage(String name) {
        var resource = Assets.class.getResource("/" + name);
        if (resource == null) {
            throw new IllegalArgumentException("Image not found: " + name);
        }
        return new Image(resource.toExternalForm());
    }
}