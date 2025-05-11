package entities.environment;

import camera.Camera;
import interfaces.Renderable;
import interfaces.Updatable;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.Assets;

public class Bonfire implements Updatable, Renderable {
	private final Image[] frames;
	private int currentFrame;
	private long lastFrameTime;

	private final double x, y;

	public Bonfire(double x, double y) {
		this.x = x;
		this.y = y;
		this.currentFrame = 0;
		this.lastFrameTime = 0;
		this.frames = new Image[] { Assets.loadImage("bonfire/00.png"), Assets.loadImage("bonfire/01.png"),
				Assets.loadImage("bonfire/02.png"), Assets.loadImage("bonfire/03.png"),
				Assets.loadImage("bonfire/04.png"), Assets.loadImage("bonfire/05.png"),
				Assets.loadImage("bonfire/06.png"), Assets.loadImage("bonfire/07.png"),
				Assets.loadImage("bonfire/08.png"), Assets.loadImage("bonfire/09.png"),
				Assets.loadImage("bonfire/10.png"), Assets.loadImage("bonfire/11.png"),
				Assets.loadImage("bonfire/12.png"), Assets.loadImage("bonfire/13.png"),
				Assets.loadImage("bonfire/14.png"), Assets.loadImage("bonfire/15.png"),
				Assets.loadImage("bonfire/16.png"), Assets.loadImage("bonfire/17.png"),
				Assets.loadImage("bonfire/18.png"), Assets.loadImage("bonfire/19.png"),
				Assets.loadImage("bonfire/20.png"), Assets.loadImage("bonfire/21.png"),
				Assets.loadImage("bonfire/22.png"), Assets.loadImage("bonfire/23.png"),
				Assets.loadImage("bonfire/24.png"), Assets.loadImage("bonfire/25.png"),
				Assets.loadImage("bonfire/26.png"), Assets.loadImage("bonfire/27.png"),
				Assets.loadImage("bonfire/28.png"), Assets.loadImage("bonfire/29.png"),
				Assets.loadImage("bonfire/30.png"), Assets.loadImage("bonfire/31.png"),
				Assets.loadImage("bonfire/32.png"), Assets.loadImage("bonfire/33.png"),
				Assets.loadImage("bonfire/34.png"), Assets.loadImage("bonfire/35.png"),
				Assets.loadImage("bonfire/36.png"), Assets.loadImage("bonfire/37.png"),
				Assets.loadImage("bonfire/38.png"), Assets.loadImage("bonfire/39.png"),
				Assets.loadImage("bonfire/40.png"), Assets.loadImage("bonfire/41.png"),
				Assets.loadImage("bonfire/42.png"), Assets.loadImage("bonfire/43.png"),
				Assets.loadImage("bonfire/44.png"), Assets.loadImage("bonfire/45.png"),
				Assets.loadImage("bonfire/46.png"), Assets.loadImage("bonfire/47.png"),
				Assets.loadImage("bonfire/48.png"), Assets.loadImage("bonfire/49.png"),
				Assets.loadImage("bonfire/50.png"), Assets.loadImage("bonfire/51.png"),
				Assets.loadImage("bonfire/52.png"), };
	}

	public void update() {
		if (System.currentTimeMillis() - lastFrameTime > 150) {
			currentFrame = (currentFrame + 1) % frames.length;
			lastFrameTime = System.currentTimeMillis();
		}
	}

	public void render(GraphicsContext gc, Camera camera) {
		Image frame = frames[currentFrame];
		double drawX = x - camera.getX();
		double drawY = y - camera.getY();
		gc.drawImage(frame, drawX, drawY, frame.getHeight(), frame.getWidth());
	}

	public Rectangle2D getHitbox() {
		Image frame = frames[0];
		return new Rectangle2D(x, y, frame.getWidth(), frame.getHeight());
	}

}
