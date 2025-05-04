package main;

import camera.Camera;
import config.GameConfig;
import entities.Monster;
import entities.Player;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import logic.GameLogicManager;
import ui.HUDRenderer;
import utils.Assets;
import utils.SoundManager;
import java.util.ArrayList;

public class GameScene extends AnimationTimer {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final HUDRenderer hudRenderer;
    private final Player player;
    private final Camera camera;
    private final Image background;
    private final ArrayList<Monster> monsters = new ArrayList<>();

    private boolean moveLeft = false;
    private boolean moveRight = false;
    
    private GameLogicManager logicManger;

    public GameScene(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.player = new Player(100, 0);
        this.camera = new Camera(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
        this.logicManger = new GameLogicManager(player, monsters);
        this.background = Assets.loadImage("BG.png");
        this.hudRenderer = new HUDRenderer(player);
        
        SoundManager.playBGM("10. Fighting.mp3");
        //SoundManager.playSEF("Into.m4a");
        
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
        logicManger.updateLogic();
        
        
    }

    private void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawBackground();

        for (Monster m : monsters) {
            m.render(gc, camera);
        }

        player.render(gc, camera);
        hudRenderer.renderHUD(gc);
    }

    private void drawBackground() {
        double bgX = -camera.getX() % background.getWidth();
        gc.drawImage(background, bgX, 0);
        gc.drawImage(background, bgX + background.getWidth(), 0);
    }

}