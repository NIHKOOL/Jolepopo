package utils;

import java.net.URL;

import javafx.scene.image.Image;

public class Assets {

	public static Image loadImage(String path) {
	    URL url = Assets.class.getResource("/" + path);
	    if (url == null) {
	        throw new IllegalArgumentException("Image not found: " + path);
	    }
	    return new Image(url.toExternalForm());
	}
}