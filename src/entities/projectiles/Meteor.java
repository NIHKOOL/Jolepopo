package entities.projectiles;

import camera.Camera;
import config.GameConfig;
import interfaces.Renderable;
import interfaces.Updatable;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.Assets;

public class Meteor implements Updatable, Renderable{
	private double x,y;
	private boolean active = true;
	private final Image sprite = Assets.loadImage("pixil-frame-0.png");
	private static final double SPEED = 8;
	
	public Meteor(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void update() {
		y += SPEED;
		if (y > GameConfig.SCREEN_HEIGHT) active = false;
	}
	
	public void render(GraphicsContext gc, Camera camera) {
		gc.drawImage(sprite, x - camera.getX(), y - camera.getY());
	}
	
	public Rectangle2D getHitBox() {
		return new Rectangle2D(x, y, sprite.getWidth(), sprite.getHeight());
	}
	
	public boolean isActive() { return active;}
	public void deactivate() { active = false;}
	
}
