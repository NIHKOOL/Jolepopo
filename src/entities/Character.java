package entities;

import camera.Camera;
import config.GameConfig;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class Character {
    protected double x, y;
    protected boolean facingRight = true;
    protected int currentHealth;
    protected double currentMana;
    protected boolean attacking = false;
    protected boolean dead = false;
    
    public abstract void update(boolean left, boolean right);
    public abstract void render(GraphicsContext gc, Camera camera);
    
    public abstract void attack();
    public abstract void dash();
    public abstract void jump();
    public abstract void defend();
    

    public abstract Rectangle2D getAttackBox();
    public abstract void takeDamage(int damage);
    public abstract void setPosition(double x, double y);
    
    public double getX() { return x; }
    public double getY() { return y; }
    public int getCurrentHealth() { return currentHealth; }
    public int getMaxHealth() { return GameConfig.PLAYER_MAX_HEALTH; }
    public double getCurrentMana() { return currentMana; }
    public int getMaxMana() { return GameConfig.PLAYER_MAX_MANA; }
	public boolean isAttacking() { return attacking; }
    public boolean isDead() { return dead; }
	
}