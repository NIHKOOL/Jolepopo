package entities;

import camera.Camera;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.Assets;

public class Arrow {
	protected double x, y;
	protected double speed;
	protected boolean facingRight;
	protected boolean active = true;
	protected Image arrowImage;
	
	public Arrow(double x, double y, boolean facingRight) {
		this.x = x;
		this.y = y;
		this.facingRight = facingRight;
		this.speed = 8;
		this.arrowImage = Assets.loadImage("Arrow.png");
	}
	
		
	public void update() {
		x += facingRight ? speed : -speed;
	}
	
	public void render(GraphicsContext gc, Camera camera) {
		double drawX = x - camera.getX();
		double drawY = y - camera.getY() + 22;
		double width = arrowImage.getWidth();
		double height = arrowImage.getHeight();
		
		if (facingRight) {
			gc.drawImage(arrowImage, drawX, drawY);
		} else {
			gc.drawImage(arrowImage, 0, 0, width, height,
						drawX + width, drawY, -width, height);
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
	
	
	
}
