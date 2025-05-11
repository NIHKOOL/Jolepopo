package logic;

import camera.Camera;
import config.GameConfig;
import entities.*;
import entities.Character;
import entities.environment.Bonfire;
import entities.projectiles.Meteor;
import gui.GameStopPage;
import interfaces.Updatable;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import ui.HUDRenderer;
import utils.Assets;
import utils.SoundManager;
import entities.projectiles.Arrow;
import entities.projectiles.BigArrow;

import java.util.ArrayList;
import java.util.List;


public class GameScene extends AnimationTimer implements Updatable{
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final HUDRenderer hudRenderer;
    private Character currentPlayer;
    private final PlayerTeamManager teamManager;
    private final Camera camera;
    private Image background;
    private final ArrayList<Monster> monsters = new ArrayList<>();
    private final Text tempMessage = new Text();
    private Scene scene;
    private final Stage stage;
    private boolean moveLeft = false;
    private boolean moveRight = false;
    private boolean canChangeMap = false;
    private boolean isScrollable = true;
    private int currentMapIndex = 0;
    private final String[] backgrounds = {"map/BG_1.png", "map/terrace.png", "map/BG_2.png", "map/terrace.png", "map/BG_4.png"};
    private Bonfire bonfire;
    private Bonfire currentBonfire;
    private boolean nearBonfire;
    private boolean bonfireMenuOpen = false;  
    private final List<Meteor> meteors = new ArrayList<>();
    private long lastMeteorSpawnTime = 0;
    private boolean enableMeteorShower = false;
    private final GameLogicManager logicManger;
    private boolean debugMode = false;
    private GorgonBoss boss;
    
    public GameScene(Canvas canvas,Stage stage) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.camera = new Camera(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
        this.stage = stage;

        List<Character> team = new ArrayList<>();
        team.add(new SamuraiMelee(100, 0));
        team.add(new SamuraiArcher(100, 0));
        team.add(new SamuraiCommander(100, 0, monsters));

        this.teamManager = new PlayerTeamManager(team);
        this.currentPlayer = teamManager.getCurrentCharacter();
        this.logicManger = new GameLogicManager(currentPlayer, monsters, stage);
        this.logicManger.setMapIndexSupplier(() -> currentMapIndex);
        this.background = Assets.loadImage(backgrounds[currentMapIndex]);
        this.hudRenderer = new HUDRenderer(currentPlayer);

        SoundManager.playBGM("musics/10. Fighting.mp3", 0.3);

        int[] minotaurX = {-2500, -2000, -1000, 2000, 3000, 3500, 5000};
		for (int x : minotaurX) { monsters.add(new Minotaur(x, GameConfig.GROUND_LEVEL - 37, currentPlayer));}
        
        tempMessage.setFont(Font.font("Impact", FontWeight.BOLD, 50));
        tempMessage.setFill(Color.WHITE);
        tempMessage.setStroke(Color.BLACK);
        tempMessage.setStrokeWidth(1);
        tempMessage.setVisible(false);
        
        
    }

    public void start(Scene scene) {
    	this.scene = scene;
        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            if (bonfireMenuOpen) {
                if (code == KeyCode.DIGIT1) {
                    GameConfig.MANA_REGEN += 0.3;
                    bonfireMenuOpen = false;
                } else if (code == KeyCode.DIGIT2) {
                    GameConfig.PLAYER_DAMAGE_MULTIPLIER += 0.5; 
                    bonfireMenuOpen = false;
                }
                return;
            }
            if (code == KeyCode.A) moveLeft = true;
            if (code == KeyCode.D) moveRight = true;
            if (code == KeyCode.SHIFT) currentPlayer.dash();
            if (code == KeyCode.SPACE || code == KeyCode.W) currentPlayer.jump();
            if (code == KeyCode.K) currentPlayer.abilityOne();
            if (code == KeyCode.L) currentPlayer.abilityTwo();
            if (code == KeyCode.TAB) switchCharacter();
            if (code == KeyCode.ENTER && canChangeMap) changeMap();
            if (code == KeyCode.E) goToStopScene();
            if (code == KeyCode.H && nearBonfire) restBonfire();
            if (code == KeyCode.P && nearBonfire) { bonfireMenuOpen = true;}
            if (code == KeyCode.M) {
            	debugMode = !debugMode ;
            	System.out.println("Debug Mode : " + (debugMode ? "ON" : "OFF"));
            }
        });

        scene.setOnKeyReleased(e -> {
            KeyCode code = e.getCode();
            if (code == KeyCode.A) moveLeft = false;
            if (code == KeyCode.D) moveRight = false;
        });

        this.start();
    }
    
    private void goToStopScene() {
        this.stop();

        Scene stopScene = GameStopPage.create(stage, () -> {
            stage.setScene(scene); 
            this.start(); 
        },currentMapIndex);

        stage.setScene(stopScene);
    }
    
    private void restBonfire() {
    	if (currentPlayer instanceof SamuraiMelee) {
    		((SamuraiMelee) currentPlayer).setHealthToMax();
    	} else if (currentPlayer instanceof SamuraiArcher) {
    		((SamuraiArcher) currentPlayer).setHealthToMax();
    	} else if (currentPlayer instanceof SamuraiCommander) {
    		((SamuraiCommander) currentPlayer).setHealthToMax();
    	}
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
        
        clearAllProjectiles();
    }

    @Override
    public void handle(long now) {
        update();
        render();
    }

    public void update() {
    	currentPlayer.update(moveLeft, moveRight, isScrollable);
        camera.update(currentPlayer, isScrollable);
        logicManger.updateLogic();
        enableMeteorShower();
        if (currentMapIndex == 1 || currentMapIndex == 3) {
        	canChangeMap = currentPlayer.getX() >= GameConfig.MAP_LOCK_WIDTH ;
        } else {
        	canChangeMap = currentPlayer.getX() >= GameConfig.MAP_WIDTH - 100 && (logicManger.getMonsters().size() == 0);
        }  
        if (bonfire != null) bonfire.update();
        if (currentBonfire != null && (currentMapIndex == 1 || currentMapIndex == 3)) {
        	Rectangle2D playerBox = new Rectangle2D(currentPlayer.getX(), currentPlayer.getY(), 60, 120);
        	if (playerBox.intersects(currentBonfire.getHitbox())) {
        		nearBonfire = true;
        	} else {
        		nearBonfire = false;
        	}
        }
        
        if (boss != null && boss.isDead()) {
        	monsters.clear();          
            meteors.clear();   
            enableMeteorShower = false;
            boss = null;         
            System.out.println("Boss defeated. All monsters and meteors cleared.");
        }
    }

    private void render() {
    	gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawBackground();
        
        for (Monster m : monsters) {
            m.render(gc, camera);
        }

        currentPlayer.render(gc, camera);
        if (bonfire != null) bonfire.render(gc, camera);
        hudRenderer.renderHUD(gc);
        meteors.forEach(m -> m.render(gc, camera));
        
        if (nearBonfire) {
        	gc.setFill(Color.WHITE);
        	gc.fillText("Press H to heal", GameConfig.SCREEN_WIDTH / 2 - 220, canvas.getHeight() - 100);
        	gc.fillText("Press P to enhance", GameConfig.SCREEN_WIDTH / 2 +160 , canvas.getHeight() - 100);
        }
        
        if (canChangeMap && currentMapIndex != 4) {
        	gc.setFill(Color.WHITE);
        	gc.fillText("Press ENTER to travel", canvas.getWidth() / 2 - 50, canvas.getHeight() - 70);
        }
        
        if (bonfireMenuOpen) {
        	gc.setFill(Color.BLACK);
            gc.fillRect(canvas.getWidth() / 2 - 100, canvas.getHeight() / 2 - 60, 180, 100);

            gc.setFill(Color.WHITE);
            gc.fillText("1: ++Boost Mana Regen", canvas.getWidth() / 2 - 80, canvas.getHeight() / 2 - 30);
            gc.fillText("[ " + GameConfig.MANA_REGEN + " >>> " + (GameConfig.MANA_REGEN + 0.3) + " ]", canvas.getWidth() / 2 - 60, canvas.getHeight() / 2 - 15);
            gc.fillText("2: ++Boost DMG Multiplier", canvas.getWidth() / 2 - 80, canvas.getHeight() / 2);
            gc.fillText("[ x" + GameConfig.PLAYER_DAMAGE_MULTIPLIER + " >>> " + (GameConfig.PLAYER_DAMAGE_MULTIPLIER + 0.5) + " ]", canvas.getWidth() / 2 - 60, canvas.getHeight() / 2 + 15);
        }
        drawDebugMode();
    }

    public void drawDebugMode() {
    	if (debugMode) {
    		System.out.println("DebugMode / "+ "SysTime " + System.currentTimeMillis() + "ms / posX " + currentPlayer.getX() + " posY " + currentPlayer.getY() );
    		if (currentPlayer instanceof SamuraiMelee) {
    			Rectangle2D hitbox = currentPlayer.getAttackBox();
    	        gc.setStroke(Color.WHITE);
    	        gc.setLineWidth(2);
    	        gc.strokeRect(hitbox.getMinX() - camera.getX(), 
    	        			  hitbox.getMinY() - camera.getY(),
    	        			  hitbox.getWidth(), 
    	        			  hitbox.getHeight());
    			
    		} else if (currentPlayer instanceof SamuraiArcher) {
    			SamuraiArcher archer = (SamuraiArcher) currentPlayer;
    			List<Arrow> arrows = archer.getArrows();
                List<BigArrow> bigArrows = archer.getBigArrows();
                for (Arrow a : arrows) {
                	Rectangle2D hitbox = a.getHitbox();
        	        gc.setStroke(Color.WHITE);
        	        gc.setLineWidth(2);
        	        gc.strokeRect(hitbox.getMinX() - camera.getX(), 
        	        			  hitbox.getMinY() - camera.getY(),
        	        			  hitbox.getWidth(), 
        	        			  hitbox.getHeight());
                }
                for (Arrow ba : bigArrows) {
                	Rectangle2D hitbox = ba.getHitbox();
        	        gc.setStroke(Color.WHITE);
        	        gc.setLineWidth(2);
        	        gc.strokeRect(hitbox.getMinX() - camera.getX(), 
        	        			  hitbox.getMinY() - camera.getY(),
        	        			  hitbox.getWidth(), 
        	        			  hitbox.getHeight());
                }
    		}
    		
    		
    		for (Monster m : monsters) {
    			Rectangle2D hitbox = m.getHitbox();
    	        gc.setStroke(Color.WHITE);
    	        gc.setLineWidth(2);
    	        gc.strokeRect(hitbox.getMinX() - camera.getX(), 
    	        			  hitbox.getMinY() - camera.getY(),
    	        			  hitbox.getWidth(), 
    	        			  hitbox.getHeight());
    		}
    		for (Meteor m : meteors) {
    			Rectangle2D hitbox = m.getHitbox();
    	        gc.setStroke(Color.WHITE);
    	        gc.setLineWidth(2);
    	        gc.strokeRect(hitbox.getMinX() - camera.getX(), 
    	        			  hitbox.getMinY() - camera.getY(),
    	        			  hitbox.getWidth(), 
    	        			  hitbox.getHeight());
    		}
    		if (bonfire != null) {
    			Rectangle2D hitbox = bonfire.getHitbox();
    	        gc.setStroke(Color.WHITE);
    	        gc.setLineWidth(2);
    	        gc.strokeRect(hitbox.getMinX() - camera.getX(), 
    	        			  hitbox.getMinY() - camera.getY(),
    	        			  hitbox.getWidth(), 
    	        			  hitbox.getHeight());
    		}
        	
    	}
    }
    
    private void drawBackground() {
    	if (currentMapIndex == 1 || currentMapIndex == 3) {
        	gc.drawImage(background, 0, 0);
        } else {
        	double bgX = -camera.getX() % background.getWidth();
            gc.drawImage(background, bgX, 0);
            gc.drawImage(background, bgX + background.getWidth(), 0);
        }
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
	    		if (m.getHitbox().intersects(playerHitBox)) {
	    			currentPlayer.takeDamage(5);
	    			m.deactivate();
	    		}
	    	}
	    		
	    	meteors.removeIf(m -> !m.isActive());
    	}
    }
    
    private void clearAllProjectiles() {
        for (Character c : teamManager.getTeam()) {
            if (c instanceof SamuraiArcher) {
                ((SamuraiArcher) c).clearProjectiles();
            }
        }
    }
    
    private void changeMap() {
    	if (currentMapIndex >= backgrounds.length - 1) {
    		System.out.println("CONGRATE YOU JUST GO IN LAST");
    		return;
    	}
    	currentMapIndex = (currentMapIndex + 1) % backgrounds.length;
    	isScrollable = !(currentMapIndex == 1 || currentMapIndex == 3);
    	background = Assets.loadImage(backgrounds[currentMapIndex]);
    	
    	currentPlayer.setPosition(100, GameConfig.GROUND_LEVEL);
    	
    	monsters.clear();
    	SoundManager.playSEF("effects/magic-spell-333896.mp3", 0.5); 
    	
    	if (currentMapIndex == 1) {
    		SoundManager.stopAllSounds();
    		showTemporaryMessage("Recovery Zone");
    		SoundManager.playBGM("musics/vampire-189047.mp3", 0.2);
    		SoundManager.playBGM("musics/campfire-crackling-fireplace-sound-119594.mp3", 0.6);
    		bonfire = new Bonfire(GameConfig.SCREEN_WIDTH / 2 - 180, GameConfig.GROUND_LEVEL - 100);
    		currentBonfire = bonfire;
    		
    	} else if (currentMapIndex == 2) {
    		bonfire = null;
    		SoundManager.stopAllSounds();
    		showTemporaryMessage("Map 2 : The Forest");
    		SoundManager.playBGM("musics/1-08 - Ominous.mp3", 0.8);
    		int[] skeletonX = {-5000, -4500, -3000, 2000, 3000, 4000, 5000};
    		int[] skeletonWarriorX = {-7000, 7000, -6000, 2500, 600};
    		
    		for (int x : skeletonX) { monsters.add(new Skeleton(x, GameConfig.GROUND_LEVEL + 20, currentPlayer));}
    		for (int x : skeletonWarriorX) { monsters.add(new SkeletonWarrior(x, GameConfig.GROUND_LEVEL + 20, currentPlayer));}
    		
    	} else if (currentMapIndex == 3) {
    		SoundManager.stopAllSounds();
    		showTemporaryMessage("Recovery Zone");
    		SoundManager.playBGM("musics/vampire-189047.mp3", 0.2);
    		SoundManager.playBGM("musics/campfire-crackling-fireplace-sound-119594.mp3", 0.6);
    		bonfire = new Bonfire(GameConfig.SCREEN_WIDTH / 2 - 180, GameConfig.GROUND_LEVEL - 100); 
    		currentBonfire = bonfire;
    		
    	} else if (currentMapIndex == 4) {
    		bonfire = null;
    		SoundManager.stopAllSounds();
    		showTemporaryMessage("Map 3 : The Boss");
    		SoundManager.playBGM("musics/1.01 The Unknown Journey Continues.mp3", 0.2);
    		
    		boss = new GorgonBoss(2000, GameConfig.GROUND_LEVEL - 280, currentPlayer);
    		monsters.add(boss);
    		hudRenderer.setBoss(boss);
    		
    		int[] skeletonX = {-10000, -5000, 8000};
    		int[] skeletonWarriorX = {10000, 12000};
    		
    		for (int x : skeletonX) { monsters.add(new Skeleton(x, GameConfig.GROUND_LEVEL + 20, currentPlayer));}
    		for (int x : skeletonWarriorX) { monsters.add(new SkeletonWarrior(x, GameConfig.GROUND_LEVEL + 20, currentPlayer));}
    		
    		int[] minotaurX = { -12000,-1000, 2000 , 4000, 5000};
    		for (int x : minotaurX) { monsters.add(new Minotaur(x, GameConfig.GROUND_LEVEL - 37, currentPlayer));}
    		
    		enableMeteorShower = true;
    		
    	} 
    	
    	clearAllProjectiles();
    }
    
    public void showTemporaryMessage(String message) {
    	tempMessage.setText(message);
        tempMessage.setX(GameConfig.SCREEN_WIDTH / 2.0 - message.length() * 8);
        tempMessage.setY(100);
        tempMessage.setOpacity(1.0);
        tempMessage.setVisible(true);

        PauseTransition pause = new PauseTransition(Duration.millis(100));
        pause.setOnFinished(e -> {
            FadeTransition fade = new FadeTransition(Duration.seconds(5), tempMessage);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(ev -> tempMessage.setVisible(false));
            fade.play();
        });
        pause.play();
    }
    
	public Scene getScene() {
		return scene;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public int getCurrentMapIndex() {
		return currentMapIndex;
	}

	public Text getTempMessage() {
		return tempMessage;
	}
	
	
	
}
