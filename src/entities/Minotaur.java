package entities;

import camera.Camera;
import config.GameConfig;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import utils.Assets;

public class Minotaur extends Monster{
   
    private int currentHealth = GameConfig.MONSTER_MAX_HEALTH;

    private boolean attacking = false;
    private boolean facingRight = false;

    private int currentWalkFrame = 0;
    private int currentAttackFrame = 0;

    private long lastAttackTime = 0;
    private long lastFrameTime = 0;

    private final Image[] walkFrames;
    private final Image[] attackFrames;

    public Minotaur(double x, double y, Character player) {
    	super(x, y, player);

        walkFrames = new Image[] {
            Assets.loadImage("monster_walk01.png"),
            Assets.loadImage("monster_walk02.png"),
            Assets.loadImage("monster_walk03.png"),
            Assets.loadImage("monster_walk04.png"),
            Assets.loadImage("monster_walk05.png")
        };

        attackFrames = new Image[] {
            Assets.loadImage("monster_attack01.png"),
            Assets.loadImage("monster_attack01.png"),
            Assets.loadImage("monster_attack02.png"),
            Assets.loadImage("monster_attack02.png"),
            Assets.loadImage("monster_attack03.png"),
            Assets.loadImage("monster_attack03.png")
        };
    }
    
    @Override
    public void update() {
    	Character player = getPlayer();
    	if (player == null) return;
    	
        double dx = player.getX() - this.x;
        facingRight = dx > 0;

        if (attacking) {
            updateAttackAnimation();
            return;
        }

        if (Math.abs(dx) > 85) {
            moveTowardPlayer(dx);
        } else {
            tryAttack(player);
        }
        
    }

    private void moveTowardPlayer(double dx) {
        this.x += dx > 0 ? GameConfig.MONSTER_SPEED : -GameConfig.MONSTER_SPEED;

        if (System.currentTimeMillis() - lastFrameTime > 200) {
            currentWalkFrame = (currentWalkFrame + 1) % walkFrames.length;
            lastFrameTime = System.currentTimeMillis();
        }
    }

    private void tryAttack(Character player) {
        long now = System.currentTimeMillis();
        if (now - lastAttackTime > GameConfig.MONSTER_ATTACK_COOLDOWN) {
            attacking = true;
            currentAttackFrame = 0;
            lastAttackTime = now;
            player.takeDamage(10); 
        }
    }

    private void updateAttackAnimation() {
        if (System.currentTimeMillis() - lastFrameTime > 150) {
            currentAttackFrame++;
            lastFrameTime = System.currentTimeMillis();
            if (currentAttackFrame >= attackFrames.length) {
                currentAttackFrame = 0;
                attacking = false;
            }
        }
    }
    
    @Override
    public void render(GraphicsContext gc, Camera camera) {
        Image frame = attacking ? attackFrames[currentAttackFrame] : walkFrames[currentWalkFrame];

        double drawX = x - camera.getX();
        double drawY = y - camera.getY();
        double drawWidth = frame.getWidth() * 2;
        double drawHeight = frame.getHeight() * 2;

        drawHealthBar(gc, drawX, drawY);

        if (facingRight) {
            gc.drawImage(frame, drawX, drawY, drawWidth, drawHeight);
        } else {
            gc.drawImage(frame, 0, 0, frame.getWidth(), frame.getHeight(),
                         drawX + drawWidth, drawY, -drawWidth, drawHeight);
        }
    }

    private void drawHealthBar(GraphicsContext gc, double drawX, double drawY) {
        double healthPercent = (double) currentHealth / GameConfig.MONSTER_MAX_HEALTH;
        gc.setFill(Color.DARKRED);
        gc.fillRect(drawX, drawY - 10, 40, 5);
        gc.setFill(Color.LIMEGREEN);
        gc.fillRect(drawX, drawY - 10, 40 * healthPercent, 5);
    }
    
    @Override
    public void takeDamage(int damage) {
        currentHealth -= damage;
        if (currentHealth < 0) currentHealth = 0;
    }
    
    @Override
    public Rectangle2D getHitbox() {
        Image frame = walkFrames[0]; 
        return new Rectangle2D(x, y, frame.getWidth() * 2, frame.getHeight() * 2);
    }
    
    @Override
    public boolean isDead() {
        return currentHealth <= 0;
    }


    public double getX() { return x; }
    public double getY() { return y; }

	public void setTarget(Character currentPlayer) {
	}
}
