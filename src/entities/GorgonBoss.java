package entities;

import camera.Camera;
import config.GameConfig;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import utils.Assets;

public class GorgonBoss extends Monster{
	private int currentWalkFrame = 0, currentAttackFrame = 0;
    private long lastAttackTime = 0, lastFrameTime = 0;
    private boolean attacking = false, facingRight = false;
    private final Image[] walkFrames, attackFrames;

    public GorgonBoss(double x, double y, Character player) {
        super(x, y, player);
        walkFrames = new Image[] {
        	Assets.loadImage("gorgon/ggWalk_0.png"),
        	Assets.loadImage("gorgon/ggWalk_1.png"),
        	Assets.loadImage("gorgon/ggWalk_2.png"),
        	Assets.loadImage("gorgon/ggWalk_3.png"),
        	Assets.loadImage("gorgon/ggWalk_4.png"),
        	Assets.loadImage("gorgon/ggWalk_5.png"),
        	Assets.loadImage("gorgon/ggWalk_6.png"),
        	Assets.loadImage("gorgon/ggWalk_7.png"),
        	Assets.loadImage("gorgon/ggWalk_8.png"),
        	Assets.loadImage("gorgon/ggWalk_9.png"),
        	Assets.loadImage("gorgon/ggWalk_10.png"),
        	Assets.loadImage("gorgon/ggWalk_11.png"),
        	Assets.loadImage("gorgon/ggWalk_12.png"), 
        };
        attackFrames = new Image[] {
        	Assets.loadImage("gorgon/ggAttack_0.png"),
        	Assets.loadImage("gorgon/ggAttack_1.png"), 
        	Assets.loadImage("gorgon/ggAttack_2.png"), 
        	Assets.loadImage("gorgon/ggAttack_3.png"), 
        	Assets.loadImage("gorgon/ggAttack_4.png"), 
        	Assets.loadImage("gorgon/ggAttack_5.png"), 
        	Assets.loadImage("gorgon/ggAttack_6.png"), 
        	Assets.loadImage("gorgon/ggAttack_7.png"), 
        	Assets.loadImage("gorgon/ggAttack_8.png"), 
        	Assets.loadImage("gorgon/ggAttack_9.png"), 
        };
    }

    @Override
    public void update() {
    	updateSlowStatus();
    	updateDebuffStatus();
        Character player = getPlayer();
        if (player == null) return;
        double dx = player.getX() - this.x;
        facingRight = dx > 0;

        if (attacking) {
            updateAttackAnimation();
            return;
        }

        if (Math.abs(dx) > 85) moveTowardPlayer(dx);
        else tryAttack(player);
    }
    
    @Override
    public void render(GraphicsContext gc, Camera camera) {
        Image frame = attacking ? attackFrames[currentAttackFrame] : walkFrames[currentWalkFrame];
        double baseHeight = walkFrames[0].getHeight();
        double drawX = x - camera.getX();
        double drawY = y - camera.getY() - (frame.getHeight() - baseHeight);
        double drawWidth = frame.getWidth() * 2, drawHeight = frame.getHeight() * 2;
        drawHealthBar(gc, drawX, drawY);

        if (facingRight) gc.drawImage(frame, drawX, drawY, drawWidth, drawHeight);
        else gc.drawImage(frame, 0, 0, frame.getWidth(), frame.getHeight(), drawX + drawWidth, drawY, -drawWidth, drawHeight);
    }

    private void drawHealthBar(GraphicsContext gc, double drawX, double drawY) {
        double healthPercent = (double) currentHealth / GameConfig.MONSTER_MAX_HEALTH;
        gc.setFill(Color.DARKRED);
        gc.fillRect(drawX, drawY - 10, 40, 5);
        gc.setFill(Color.LIMEGREEN);
        gc.fillRect(drawX, drawY - 10, 40 * healthPercent, 5);
    }

    private void moveTowardPlayer(double dx) {
        x += dx > 0 
        		? GameConfig.MONSTER_SPEED * getSpeedMultipiler()
        		: -GameConfig.MONSTER_SPEED * getSpeedMultipiler();
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
            if (player instanceof SamuraiMelee) {
            	player.takeDamage(10);
            } else if (player instanceof SamuraiArcher) {
            	player.takeDamage(20);
            } else {
            	player.takeDamage(10);
            }
        }
    }
    

    private void updateAttackAnimation() {
        if (System.currentTimeMillis() - lastFrameTime > 150) {
            currentAttackFrame++;
            lastFrameTime = System.currentTimeMillis();
            if (currentAttackFrame >= attackFrames.length) {
                attacking = false;
                currentAttackFrame = 0;
            }
        }
    }

    @Override
    public Rectangle2D getHitbox() {
        Image frame = walkFrames[0];
        return new Rectangle2D(x, y, frame.getWidth() * 2, frame.getHeight() * 2);
    }

    @Override
    public boolean isDead() { return currentHealth <= 0; }
    @Override
    public double getX() { return x; }
    @Override
    public double getY() { return y; }
}
