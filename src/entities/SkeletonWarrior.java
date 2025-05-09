package entities;

import camera.Camera;
import config.GameConfig;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import utils.Assets;

public class SkeletonWarrior extends Skeleton{

	public SkeletonWarrior(double x, double y, Character player) {
		super(x, y, player);
		setCurrentHealth(GameConfig.SKELETON_MAX_HEALTH * 2);
		
		walkFrames = new Image[] {
				Assets.loadImage("skeletonWarrior/skewRun_0.png"),
	            Assets.loadImage("skeletonWarrior/skewRun_1.png"),
	            Assets.loadImage("skeletonWarrior/skewRun_2.png"),
	            Assets.loadImage("skeletonWarrior/skewRun_3.png"),
	            Assets.loadImage("skeletonWarrior/skewRun_4.png"),
	            Assets.loadImage("skeletonWarrior/skewRun_5.png"),
	            Assets.loadImage("skeletonWarrior/skewRun_6.png"),
	            Assets.loadImage("skeletonWarrior/skewRun_7.png"),
	        };
		attackFrames = new Image[] {
				Assets.loadImage("skeletonWarrior/skewAttack_0.png"),
	            Assets.loadImage("skeletonWarrior/skewAttack_1.png"),
	            Assets.loadImage("skeletonWarrior/skewAttack_2.png"),
	            Assets.loadImage("skeletonWarrior/skewAttack_3.png"),
	        };
	}
	
	public void render(GraphicsContext gc, Camera camera) {
        Image frame = attacking ? attackFrames[currentAttackFrame] : walkFrames[currentWalkFrame];
        double baseHeight = walkFrames[0].getHeight() - 10;
        double drawX = x - camera.getX(), drawY = y - camera.getY() - (frame.getHeight() - baseHeight);
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

}
