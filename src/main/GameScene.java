package main;

import camera.Camera;
import config.GameConfig;
import entities.Monster;
import entities.SamuraiMelee;
import entities.Character;
import entities.Minotaur;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import logic.GameLogicManager;
import logic.PlayerTeamManager;
import ui.HUDRenderer;
import utils.Assets;
import utils.SoundManager;

import java.util.ArrayList;
import java.util.List;

public class GameScene extends AnimationTimer {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final HUDRenderer hudRenderer;
    private Character currentPlayer;
    private PlayerTeamManager teamManager;
    private final Camera camera;
    private final Image background;
    private final ArrayList<Monster> monsters = new ArrayList<>();

    private boolean moveLeft = false;
    private boolean moveRight = false;
    
    private GameLogicManager logicManger;

    public GameScene(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.camera = new Camera(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
        
        List<Character> team = new ArrayList<>();
        team.add(new SamuraiMelee(100, 0));
        team.add(new SamuraiMelee(100, 0));
        
        this.teamManager = new PlayerTeamManager(team);
        this.currentPlayer = teamManager.getCurrentCharacter();
        this.logicManger = new GameLogicManager(currentPlayer, monsters);
        this.background = Assets.loadImage("BG.png");
        this.hudRenderer = new HUDRenderer(currentPlayer);
        
        
        
        
        SoundManager.playBGM("10. Fighting.mp3");
        //SoundManager.playSEF("Into.m4a");
        
        monsters.add(new Minotaur(600, GameConfig.GROUND_LEVEL - 30, currentPlayer));
        monsters.add(new Minotaur(1600, GameConfig.GROUND_LEVEL - 30, currentPlayer));
        monsters.add(new Minotaur(2600, GameConfig.GROUND_LEVEL - 30, currentPlayer));
    }

    public void start(Scene scene) {
        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            if (code == KeyCode.A) moveLeft = true;
            if (code == KeyCode.D) moveRight = true;
            if (code == KeyCode.SHIFT) currentPlayer.dash();
            if (code == KeyCode.SPACE || code == KeyCode.W) currentPlayer.jump();
            if (code == KeyCode.K) currentPlayer.attack();
            if (code == KeyCode.L) currentPlayer.defend();
            if (code == KeyCode.TAB) {
            	
            	double oldX = currentPlayer.getX();
            	double oldY = currentPlayer.getY();
            	
            	teamManager.switchToNext();
            	currentPlayer = teamManager.getCurrentCharacter();
            	
            	currentPlayer.setPosition(oldX, oldY+100);
            	
            	hudRenderer.setCharacter(currentPlayer);
            	logicManger.setPlayer(currentPlayer);
            }
            
            for (Monster m : monsters) {
            	m.setTarget(currentPlayer);
            }
            
            
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
        currentPlayer.update(moveLeft, moveRight);
        camera.update(currentPlayer);
        logicManger.updateLogic();

        System.out.println("SystemTIME : " + System.currentTimeMillis() + " | X : " + currentPlayer.getX() + " | Y : " + currentPlayer.getY());
        
        
    }

    private void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawBackground();

        for (Monster m : monsters) {
            m.render(gc, camera);
        }

        currentPlayer.render(gc, camera);
        hudRenderer.renderHUD(gc);
    }

    private void drawBackground() {
        double bgX = -camera.getX() % background.getWidth();
        gc.drawImage(background, bgX, 0);
        gc.drawImage(background, bgX + background.getWidth(), 0);
    }

}