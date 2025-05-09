package entities;

import camera.Camera;
import config.GameConfig;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import utils.Assets;

public class Skeleton extends Monster{
	protected int currentWalkFrame = 0;
	protected int currentAttackFrame = 0;
    protected long lastAttackTime = 0;
	private long lastFrameTime = 0;
    protected boolean attacking = false;
	protected boolean facingRight = false;
    protected Image[] walkFrames;
	protected Image[] attackFrames;
	
	public Skeleton(double x, double y, Character player) {
		super(x, y, player);
		setCurrentHealth(GameConfig.SKELETON_MAX_HEALTH);
		walkFrames = new Image[] {
				Assets.loadImage("skeleton/skeRun_0.png"),
	            Assets.loadImage("skeleton/skeRun_1.png"),
	            Assets.loadImage("skeleton/skeRun_2.png"),
	            Assets.loadImage("skeleton/skeRun_3.png"),
	            Assets.loadImage("skeleton/skeRun_4.png"),
	            Assets.loadImage("skeleton/skeRun_5.png"),
	        };
		attackFrames = new Image[] {
	            Assets.loadImage("skeleton/skeAttack_0.png"),
	            Assets.loadImage("skeleton/skeAttack_1.png"),
	            Assets.loadImage("skeleton/skeAttack_2.png"),
	            Assets.loadImage("skeleton/skeAttack_3.png"),
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
        double drawX = x - camera.getX(), drawY = y - camera.getY();
        double drawWidth = frame.getWidth() * 2, drawHeight = frame.getHeight() * 2;
        drawHealthBar(gc, drawX + 100, drawY);

        if (facingRight) gc.drawImage(frame, drawX, drawY, drawWidth, drawHeight);
        else gc.drawImage(frame, 0, 0, frame.getWidth(), frame.getHeight(), drawX + drawWidth, drawY, -drawWidth, drawHeight);
        
      //Debug
        Rectangle2D hitbox = getHitbox();
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeRect(hitbox.getMinX() - camera.getX(), 
        			  hitbox.getMinY() - camera.getY(),
        			  hitbox.getWidth(), 
        			  hitbox.getHeight());
    }

    protected void drawHealthBar(GraphicsContext gc, double drawX, double drawY) {
        double healthPercent = (double) currentHealth / GameConfig.MONSTER_MAX_HEALTH;
        gc.setFill(Color.LIMEGREEN);
        gc.fillRect(drawX, drawY - 10, 40 * healthPercent, 5);
    }

    private void moveTowardPlayer(double dx) {
        x += dx > 0 
        		? GameConfig.MONSTER_SPEED * 4 * getSpeedMultipiler()
        		: -GameConfig.MONSTER_SPEED * 4 * getSpeedMultipiler();
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
            	player.takeDamage(15);
            } else if (player instanceof SamuraiArcher) {
            	player.takeDamage(25);
            } else {
            	player.takeDamage(20);
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
        
        double hitboxWidth = frame.getWidth() * 0.6;
        double hitboxheight = frame.getHeight() * 2;
        double hitboxX = x + 100;
        double hitboxY = y;
        
        return new Rectangle2D(hitboxX, hitboxY, hitboxWidth, hitboxheight);
    }

    @Override
    public boolean isDead() { return currentHealth <= 0; }
    @Override
    public double getX() { return x; }
    @Override
    public double getY() { return y; }

}
