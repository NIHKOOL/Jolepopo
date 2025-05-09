package entities;

import camera.Camera;
import config.GameConfig;
import interfaces.AbilityCaster;
import interfaces.Controllable;
import interfaces.Damagable;
import interfaces.Renderable;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.Assets;

public abstract class Character implements Renderable, Damagable, Controllable, AbilityCaster{
    protected double x, y;
    protected boolean facingRight = true;
    protected int currentHealth;
    protected double currentMana;
    protected boolean attacking = false;
    protected boolean dead = false;
    protected static final Image SHADOW_IMAGE = Assets.loadImage("shadow.png");
 
    public abstract void update(boolean left, boolean right, boolean isScrollable);
    public abstract void render(GraphicsContext gc, Camera camera);
    
    public abstract void dash();
    public abstract void jump();
    public abstract void abilityOne();
    public abstract void abilityTwo();
    public abstract void takeDamage(int damage);
    
    public abstract Rectangle2D getAttackBox();
    public abstract void setPosition(double x, double y);
    protected void applyMapBounds(double characterWidth, boolean isScrollable) {
    	if (x < 0) x = 0;
    	double maxX = isScrollable ? GameConfig.MAP_WIDTH : GameConfig.SCREEN_WIDTH - characterWidth;
    	if (x > maxX) x = maxX;
    }
    public void setHealthToMax() {
    	this.currentHealth = GameConfig.PLAYER_MAX_HEALTH;
    }
    
    public double getX() { return x;}
    public double getY() { return y;}
    public int getCurrentHealth() { return currentHealth;}
    public int getMaxHealth() { return GameConfig.PLAYER_MAX_HEALTH;}
    public double getCurrentMana() { return currentMana;}
    public int getMaxMana() { return GameConfig.PLAYER_MAX_MANA;}
    public boolean isAttacking() { return attacking;}
    public boolean isDead() { return dead;}
}
