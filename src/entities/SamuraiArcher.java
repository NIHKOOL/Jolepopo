package entities;

import camera.Camera;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.Assets;

public class SamuraiArcher extends Character{
	
	private Image[] walkFrames, dashFrames, jumpFrames, attackFrames;
	
	public SamuraiArcher(double x, double y) {
        this.x = x;
        this.y = y;

        walkFrames = new Image[] {
            Assets.loadImage("RealKnight.png"), 
            Assets.loadImage("walk02.png"), 
            Assets.loadImage("walk03.png"),
            Assets.loadImage("walk04.png"), 
            Assets.loadImage("walk05.png"), 
            Assets.loadImage("walk06.png"),
            Assets.loadImage("walk07.png"), 
            Assets.loadImage("walk08.png"), 
            Assets.loadImage("walk09.png")
        };

        dashFrames = new Image[] {
            Assets.loadImage("dash01.png"), 
            Assets.loadImage("dash02.png"),
            Assets.loadImage("dash03.png"), 
            Assets.loadImage("dash04.png")
        };

        jumpFrames = new Image[] {
            Assets.loadImage("jump01.png"),
            Assets.loadImage("jump02.png"),
            Assets.loadImage("jump03.png"), 
            Assets.loadImage("jump04.png"), 
            Assets.loadImage("jump05.png")
        };

        attackFrames = new Image[] {
            Assets.loadImage("attack01.png"), 
            Assets.loadImage("dash04.png"), 
            Assets.loadImage("attack05.png")
        };
	}
	
	@Override
	public void update(boolean left, boolean right) {
		// TODO Auto-generated method stub
		long now = System.currentTimeMillis();
        updateAttack(now);
        updateDash(now);
        updateMovement(now, left, right);
        updateJump(now);
        regenMana();
	}
	
	@Override
	public void render(GraphicsContext gc, Camera camera) {
		// TODO Auto-generated method stub
		
	}

	private void updateMovement(long now, boolean left, boolean right) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dash() {
		// TODO Auto-generated method stub
		
	}

	private void updateDash(long now) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void jump() {
		// TODO Auto-generated method stub
		
	}
	
	private void updateJump(long now) {
		// TODO Auto-generated method stub
		
	}

	
	
	private void regenMana() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void attack() {
		// TODO Auto-generated method stub
		
	}
	
	private void updateAttack(long now) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public Rectangle2D getAttackBox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void takeDamage(int damage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPosition(double x, double y) {
		// TODO Auto-generated method stub
		
	}

}
