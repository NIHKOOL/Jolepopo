package main;

import camera.Camera;
import config.GameConfig;
import entities.*;
import entities.Character;
import entities.projectiles.Meteor;
import interfaces.Updatable;
import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
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

public class GameScene extends AnimationTimer implements Updatable {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final HUDRenderer hudRenderer;
    private Character currentPlayer;
    private final PlayerTeamManager teamManager;
    private final Camera camera;
    private Image background;
    private final ArrayList<Monster> monsters = new ArrayList<>();

    private boolean moveLeft = false;
    private boolean moveRight = false;
    private boolean canChangeMap = false;
    private int currentMapIndex = 0;
    private final String[] backgrounds = {"map/BG_1.png", "map/BG_2.png", "map/BG_4.png"};
    
    private final List<Meteor> meteors = new ArrayList<>();
    private long lastMeteorSpawnTime = 0;
    private boolean enableMeteorShower = false;

    private final GameLogicManager logicManger;

    
    public GameScene(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.camera = new Camera(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);

        List<Character> team = new ArrayList<>();
	        team.add(new SamuraiMelee(100, 0));
	        team.add(new SamuraiArcher(100, 0));
	        team.add(new SamuraiCommander(100, 0, monsters));

        this.teamManager = new PlayerTeamManager(team);
        this.currentPlayer = teamManager.getCurrentCharacter();
        this.logicManger = new GameLogicManager(currentPlayer, monsters);
        this.background = Assets.loadImage(backgrounds[currentMapIndex]);
        this.hudRenderer = new HUDRenderer(currentPlayer);

        SoundManager.playBGM("musics/10. Fighting.mp3", 0.1);
      
        // First map spawn
        monsters.add(new Minotaur(300, GameConfig.GROUND_LEVEL - 20, currentPlayer));   

    }

    public void start(Scene scene) {
        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            if (code == KeyCode.A) moveLeft = true;
            if (code == KeyCode.D) moveRight = true;
            if (code == KeyCode.SHIFT) currentPlayer.dash();
            if (code == KeyCode.SPACE || code == KeyCode.W) currentPlayer.jump();
            if (code == KeyCode.K) currentPlayer.abilityOne();
            if (code == KeyCode.L) currentPlayer.abilityTwo();
            if (code == KeyCode.TAB) switchCharacter();
            if (code == KeyCode.ENTER && canChangeMap) changeMap();
        });

        scene.setOnKeyReleased(e -> {
            KeyCode code = e.getCode();
            if (code == KeyCode.A) moveLeft = false;
            if (code == KeyCode.D) moveRight = false;
        });

        this.start();
    }

    private void switchCharacter() {
        double oldX = currentPlayer.getX();
        double oldY = currentPlayer.getY();

        teamManager.switchToNext();
        currentPlayer = teamManager.getCurrentCharacter();
        currentPlayer.setPosition(oldX, oldY + 100);

        logicManger.setPlayer(currentPlayer);
        logicManger.updateLogic();
        hudRenderer.setCharacter(currentPlayer);

        SoundManager.playSEF("effects/hotel-bell-334109.mp3", 0.3);

        for (Monster m : monsters) {
            m.setTarget(currentPlayer);
            m.update();
        }
    }

    @Override
    public void handle(long now) {
        update();
        render();
    }

    public void update() {
        currentPlayer.update(moveLeft, moveRight);
        camera.update(currentPlayer);
        logicManger.updateLogic();
        enableMeteorShower();
        canChangeMap = currentPlayer.getX() >= GameConfig.MAP_WIDTH - 100;
        System.out.println("[SyTime : " + System.currentTimeMillis() + "][X : " + currentPlayer.getX() + 
        					"][Y : " + currentPlayer.getY() + "] " + canChangeMap);
    }

    private void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawBackground();
        
        for (Monster m : monsters) {
            m.render(gc, camera);
        }

        currentPlayer.render(gc, camera);
        hudRenderer.renderHUD(gc);
        meteors.forEach(m -> m.render(gc, camera));
    } 
    
    private void drawBackground() {
        double bgX = -camera.getX() % background.getWidth();
        gc.drawImage(background, bgX, 0);
        gc.drawImage(background, bgX + background.getWidth(), 0);
    }
    
    private void enableMeteorShower() {
    	if (enableMeteorShower) {
	    	long now = System.currentTimeMillis();
	    	if (now - lastMeteorSpawnTime > GameConfig.METEOR_SPAWN_INTERVAL) {
	    		double spawnX = Math.random() * GameConfig.MAP_WIDTH;
	    		meteors.add(new Meteor(spawnX, -100));
	    		lastMeteorSpawnTime = now;
	    	}
	    	
	    	meteors.forEach(Meteor::update);
	    	
	    	Rectangle2D playerHitBox = new Rectangle2D(currentPlayer.getX(), currentPlayer.getY(), 60, 120);
	    	for (Meteor m : meteors) {
	    		if (m.getHitBox().intersects(playerHitBox)) {
	    			currentPlayer.takeDamage(5);
	    			m.deactivate();
	    		}
	    	}
	    		
	    	meteors.removeIf(m -> !m.isActive());
    	}
    }
    
    private void changeMap() {
    	if (currentMapIndex >= backgrounds.length - 1) {
    		System.out.println("CONGRATE YOU JUST GO IN LAST");
    		return;
    	}
    	currentMapIndex = (currentMapIndex + 1) % backgrounds.length;
    	background = Assets.loadImage(backgrounds[currentMapIndex]);
    	currentPlayer.setPosition(100, GameConfig.GROUND_LEVEL);
    	
    	monsters.clear();
    	
    	SoundManager.playSEF("effects/magic-spell-333896.mp3", 0.5); 
    	
    	if (currentMapIndex == 1) {
    		SoundManager.stopBGM();
    		SoundManager.playBGM("musics/1-08 - Ominous.mp3", 0.8);
    	} else if (currentMapIndex == 2) {
    		SoundManager.stopBGM();
    		Monster boss = new GorgonBoss(2000, GameConfig.GROUND_LEVEL - 280, currentPlayer);
    		monsters.add(boss);
    		hudRenderer.setBoss(boss);
    		enableMeteorShower = true;
    		SoundManager.playBGM("musics/1.01 The Unknown Journey Continues.mp3", 0.2); 
    	} 
    }
}
