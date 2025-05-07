package entities;

import camera.Camera;
import config.GameConfig;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class Monster {
    protected double x, y;
    protected int currentHealth = GameConfig.MONSTER_MAX_HEALTH;
    protected boolean attacking = false;
    protected boolean facingRight = false;

    protected Character player;
    
    private boolean slowed = false;
    private long slowEndTime = 0;
    private double speedMultiplier = 1.0;
    

    public Monster(double x, double y, Character player) {
        this.x = x;
        this.y = y;
        this.player = player;
    }
    
    public abstract void update();
    public abstract void render(GraphicsContext gc, Camera camera);

    public void takeDamage(int damage) {
        currentHealth -= damage;
        if (currentHealth < 0) currentHealth = 0;
    }
    
    public void applySlow(long durationMillis) {
    	slowed = true;
    	slowEndTime = System.currentTimeMillis() + durationMillis;
    	speedMultiplier = 0.3;
    }

    public void updateSlowStatus() {
    	if (slowed && System.currentTimeMillis() > slowEndTime) {
    		slowed = false;
    		speedMultiplier = 1.0;
    	}
    }
    
    public void setTarget(Character player) {
        this.player = player;
    }

    public double getX() { return x;}
    public double getY() { return y;}
    public abstract Rectangle2D getHitbox();
    public boolean isDead() { return currentHealth <= 0; }
    protected Character getPlayer() { return player;}
    public double getSpeedMultipiler() { return speedMultiplier;}
    
}
