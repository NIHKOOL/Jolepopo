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

    public Monster(double x, double y, Character player) {
        this.x = x;
        this.y = y;
        this.player = player;
    }

    public abstract void update();
    public abstract void render(GraphicsContext gc, Camera camera);
    public abstract Rectangle2D getHitbox();
    
    public void takeDamage(int d) {
        currentHealth -= d;
        if (currentHealth < 0) currentHealth = 0;
    }

    public boolean isDead() {
        return currentHealth <= 0;
    }

    public void setTarget(Character player) {
        this.player = player;
        System.out.println("[Monster.setTarget] Target set to: " + player.getX() + ", " + player.getY());
    }

    public double getX() { return x; }
    public double getY() { return y; }
    protected Character getPlayer() { return player; }
}