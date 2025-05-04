package main;

import camera.Camera;
import config.GameConfig;
import entities.Monster;
import entities.Player;
import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import utils.Assets;
import utils.SoundManager;

import java.net.URL;
import java.util.ArrayList;

public class GameScene extends AnimationTimer {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final Player player;
    private final Camera camera;
    private final Image background;
    private final ArrayList<Monster> monsters = new ArrayList<>();

    private boolean moveLeft = false;
    private boolean moveRight = false;

    public GameScene(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.player = new Player(100, 0);
        this.camera = new Camera(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
        this.background = Assets.loadImage("BG.png");
        
        SoundManager.playBGM("BG_music.mp3");
        SoundManager.playSEF("Into.m4a");
        
        monsters.add(new Monster(600, GameConfig.GROUND_LEVEL - 30, player));
    }

    public void start(Scene scene) {
        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            if (code == KeyCode.A) moveLeft = true;
            if (code == KeyCode.D) moveRight = true;
            if (code == KeyCode.SHIFT) player.dash();
            if (code == KeyCode.SPACE || code == KeyCode.W) player.jump();
            if (code == KeyCode.K) player.attack();
            if (code == KeyCode.L) player.defend();
        });

        scene.setOnKeyReleased(e -> {
            KeyCode code = e.getCode();
            if (code == KeyCode.A) moveLeft = false;
            if (code == KeyCode.D) moveRight = false;
        });

        this.start();
    }

    @Override
    public void handle(long now) {
        update();
        render();
    }

    private void update() {
        player.update(moveLeft, moveRight);
        camera.update(player);
        
        for (Monster m : monsters) {
            m.update();
        }
        
        monsters.removeIf(m -> m.isDead());
        
        if (player.isAttacking()) {
            Rectangle2D playerAtk = player.getAttackBox();
            
            for (Monster m : monsters) {
                if (playerAtk.intersects(m.getHitbox())) {
                    m.takeDamage(1);  // หรือใส่ค่าดาเมจตามที่ต้องการ
                }
            }
        }

        
    }

    private void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawBackground();

        for (Monster m : monsters) {
            m.render(gc, camera);
        }

        player.render(gc, camera);
        renderHUD();
    }

    private void drawBackground() {
        double bgX = -camera.getX() % background.getWidth();
        gc.drawImage(background, bgX, 0);
        gc.drawImage(background, bgX + background.getWidth(), 0);
    }

    private void renderHUD() {
        double x = 20;
        double y = 20;

        double healthWidth = 200;
        double healthHeight = 20;
        gc.setFill(Color.GRAY);
        gc.fillRect(x, y, healthWidth, healthHeight);
        gc.setFill(Color.RED);
        double healthPercent = (double) player.getCurrentHealth() / player.getMaxHealth();
        gc.fillRect(x, y, healthWidth * healthPercent, healthHeight);

        gc.setFill(Color.WHITE);
        gc.fillText(player.getCurrentHealth() + " / " + player.getMaxHealth(), x + 5, y + 14);

        double manaWidth = 300;
        double manaHeight = 20;
        double manaY = y + healthHeight + 5;
        gc.setFill(Color.DARKBLUE);
        gc.fillRect(x, manaY, manaWidth, manaHeight);
        gc.setFill(Color.AQUAMARINE);
        double manaPercent = player.getCurrentMana() / player.getMaxMana();
        gc.fillRect(x, manaY, manaWidth * manaPercent, manaHeight);

        gc.setFill(Color.BLACK);
        gc.fillText((int) player.getCurrentMana() + " / " + player.getMaxMana(), x + 5, manaY + 14);
    }
    
}