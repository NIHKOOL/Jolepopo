package utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {

    private static MediaPlayer bgmPlayer;
    private static MediaPlayer sefPlayer;
    private static final Map<String, Long> soundCooldowns = new HashMap<>();
    private static final List<MediaPlayer> bgmPlayersList = new ArrayList<>(); 
    
    public static void playBGM(String filename, double volume) {
        URL resource = SoundManager.class.getResource("/" + filename);
        if (resource == null) {
            throw new IllegalArgumentException("BGM file not found: " + filename);
        }
        Media media = new Media(resource.toString());
        bgmPlayer = new MediaPlayer(media);
        bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        bgmPlayer.setVolume(volume);
        bgmPlayer.play();
        bgmPlayersList.add(bgmPlayer);
        
    }

    public static void stopBGM() {
    	for (MediaPlayer player : bgmPlayersList) {
            player.stop();
        }
        bgmPlayersList.clear();
    }

    public static void playSEF(String filename, double volume) {
        URL resource = SoundManager.class.getResource("/" + filename);
        if (resource == null) {
            throw new IllegalArgumentException("Sound effect file not found: " + filename);
        }
        Media media = new Media(resource.toString());
        sefPlayer = new MediaPlayer(media);
        sefPlayer.setVolume(volume);
        sefPlayer.play();
    }

    public static void playSEF(String filename, double volume, long cooldownMillis) {
        long now = System.currentTimeMillis();
        long lastPlayed = soundCooldowns.getOrDefault(filename, 0L);
        if (now - lastPlayed >= cooldownMillis) {
            playSEF(filename, volume);
            soundCooldowns.put(filename, now);
        }
    }
}
