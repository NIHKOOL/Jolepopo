package utils;

import java.net.URL;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {
	private static MediaPlayer bgmPlayer;
	private static MediaPlayer sefPlayer;
	
	public static void playBGM(String filename) {
		URL resource = SoundManager.class.getResource("/" +  filename);
		Media media = new Media(resource.toString());
		bgmPlayer = new MediaPlayer(media);
		bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		bgmPlayer.setVolume(0.3);
		bgmPlayer.play();
		
		
	}
	
	public static void stopBGM() {
		if (bgmPlayer != null) bgmPlayer.stop();;
	}
	
	public static void playSEF(String filename) {
		URL resource = SoundManager.class.getResource("/" +  filename);
		Media media = new Media(resource.toString());
		sefPlayer = new MediaPlayer(media);
		sefPlayer.setVolume(1);
		sefPlayer.play();
	}
}