package entities;

import camera.Camera;
import config.GameConfig;
import interfaces.Damageable;
import interfaces.Renderable;
import interfaces.Updatable;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class Monster implements Renderable, Updatable, Damageable {
	protected double x, y;
	protected int currentHealth;
	protected boolean attacking;
	protected boolean facingRight;
	protected Character player;
	private boolean slowed;
	private long slowEndTime;
	private double speedMultiplier;
	private boolean debuffed;
	private long debuffEndTime;
	protected double damageMultiplier;

	public Monster(double x, double y, Character player) {
		this.x = x;
		this.y = y;
		this.player = player;
		this.currentHealth = GameConfig.MONSTER_MAX_HEALTH;
		this.attacking = false;
		this.facingRight = false;
		this.slowed = false;
		this.slowEndTime = 0;
		this.speedMultiplier = 1.0;
		this.debuffed = false;
		this.debuffEndTime = 0;
		this.damageMultiplier = 1.0;
	}

	public abstract void update();

	public abstract void render(GraphicsContext gc, Camera camera);

	public void takeDamage(int damage) {
		int finalDamage = (int) (damage * damageMultiplier);
		currentHealth -= finalDamage;
		if (currentHealth < 0)
			currentHealth = 0;
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

	public void applyDamageDebuff(long durationMillis) {
		debuffed = true;
		debuffEndTime = System.currentTimeMillis() + durationMillis;
		damageMultiplier = 2.0;
	}

	public void updateDebuffStatus() {
		if (debuffed && System.currentTimeMillis() > debuffEndTime) {
			debuffed = false;
			damageMultiplier = 1.0;
		}
	}

	public void setTarget(Character player) {
		this.player = player;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public abstract Rectangle2D getHitbox();

	public boolean isDead() {
		return currentHealth <= 0;
	}

	protected Character getPlayer() {
		return player;
	}

	public double getSpeedMultipiler() {
		return speedMultiplier;
	}

	public double getDamageMultiplier() {
		return damageMultiplier;
	}

	public void setCurrentHealth(int health) {
		this.currentHealth = (int) health;
	}

	public double getCurrentHealth() {
		return currentHealth;
	}
}
