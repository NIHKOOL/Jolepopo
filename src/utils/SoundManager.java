package utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {

    private static MediaPlayer bgmPlayer;
    private static MediaPlayer sefPlayer;
    private static final Map<String, Long> soundCooldowns = new HashMap<>();
    private static List<MediaPlayer> activeEffects = new ArrayList<>();

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
        activeEffects.add(bgmPlayer);
    }

    public static void stopBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
            bgmPlayer = null;
        } 
        
    }
    
    public static void playSEFOnce(String filename, double volume) {
        URL resource = SoundManager.class.getResource("/" + filename);
        if (resource == null) {
            throw new IllegalArgumentException("Sound effect file not found: " + filename);
        }
        Media media = new Media(resource.toString());
        MediaPlayer player = new MediaPlayer(media);
        player.setVolume(volume);
        player.setCycleCount(1); // เล่นแค่รอบเดียว
        player.play();

        // ลบออกจาก active list หลังเล่นเสร็จ
        activeEffects.add(player);
        player.setOnEndOfMedia(() -> activeEffects.remove(player));
    }
    
    public static void playSEFOnce(String filename, double volume, Runnable onEnd) {
        URL resource = SoundManager.class.getResource("/" + filename);
        if (resource == null) {
            throw new IllegalArgumentException("Sound effect file not found: " + filename);
        }
        Media media = new Media(resource.toString());
        MediaPlayer player = new MediaPlayer(media);
        player.setVolume(volume);
        player.setCycleCount(1); // เล่นแค่รอบเดียว
        player.play();

        // เพิ่ม player ลงใน activeEffects
        activeEffects.add(player);

        // ตั้งค่าการดำเนินการเมื่อเสียงเล่นจบ
        player.setOnEndOfMedia(() -> {
            activeEffects.remove(player);
            if (onEnd != null) {
                Platform.runLater(onEnd);
            }
        });
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
        activeEffects.add(sefPlayer);
    }

    public static void playSEF(String filename, double volume, long cooldownMillis) {
        long now = System.currentTimeMillis();
        long lastPlayed = soundCooldowns.getOrDefault(filename, 0L);
        if (now - lastPlayed >= cooldownMillis) {
            playSEF(filename, volume);
            soundCooldowns.put(filename, now);
        }
    }
    
    public static void stopAllSEF() {
        for (MediaPlayer player : activeEffects) {
            player.stop();
        }
        activeEffects.clear();
    }

    public static void stopAllSounds() {
        stopBGM();
        stopAllSEF();
    }

	public static List<MediaPlayer> getActiveEffects() {
		return activeEffects;
	}

	public static void setActiveEffects(List<MediaPlayer> activeEffects) {
		SoundManager.activeEffects = activeEffects;
	}

	public static MediaPlayer getBgmPlayer() {
		return bgmPlayer;
	}

	public static void setBgmPlayer(MediaPlayer bgmPlayer) {
		SoundManager.bgmPlayer = bgmPlayer;
	}

}
    
