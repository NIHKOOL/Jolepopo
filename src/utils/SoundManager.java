package utils;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {
	private static MediaPlayer bgmPlayer;
	private static MediaPlayer sefPlayer;
	private static final Map<String, Long> soundCooldowns = new HashMap<>();
	
	
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
	
	
	//อันนี้ sound effect
	public static void playSEF(String filename) {
		URL resource = SoundManager.class.getResource("/" +  filename);
		Media media = new Media(resource.toString());
		sefPlayer = new MediaPlayer(media);
		sefPlayer.setVolume(1);
		sefPlayer.play();
	}
	//อันนี้ sound effect ที่มันจะมี cooldown เผื่อเสียงมันรั่วเกิน
	public static void playSEF(String filename, long cooldownMillis) {
		long now = System.currentTimeMillis();
		long lastPlayed = soundCooldowns.getOrDefault(filename, 0L);
		if (now - lastPlayed >= cooldownMillis) {
			playSEF(filename);
			soundCooldowns.put(filename, now);
		}
	}
	
	
	
	
	
	
	
}