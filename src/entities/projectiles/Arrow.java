package entities.projectiles;

import camera.Camera;
import config.GameConfig;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.Assets;
import interfaces.Updatable;
import interfaces.Renderable;

public class Arrow implements Updatable, Renderable {
	protected double x, y;
	protected double speed;
	protected boolean facingRight;
	protected boolean active;
	protected Image arrowImage;

	public Arrow(double x, double y, boolean facingRight) {
		this.x = x;
		this.y = y;
		this.facingRight = facingRight;
		this.speed = GameConfig.PlAYER_ARROW_SPEED;
		this.arrowImage = Assets.loadImage("samuraiArcher/Arrow.png");
		this.active = true;
	}

	@Override
	public void update() {
		x += facingRight ? speed : -speed;
	}

	@Override
	public void render(GraphicsContext gc, Camera camera) {
		double drawX = x - camera.getX();
		double drawY = y - camera.getY() + 22;
		double width = arrowImage.getWidth();
		double height = arrowImage.getHeight();

		if (facingRight) {
			gc.drawImage(arrowImage, drawX, drawY);
		} else {
			gc.drawImage(arrowImage, 0, 0, width, height, drawX + width, drawY, -width, height);
		}
	}

	public Rectangle2D getHitbox() {
		return new Rectangle2D(x, y, arrowImage.getWidth(), arrowImage.getHeight());
	}

	public boolean isActive() {
		return active;
	}

	public void deactive() {
		active = false;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getDamage() {
		return GameConfig.PLAYER_ARROW_DAMAGE;
	}
}