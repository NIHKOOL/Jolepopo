package entities;

import camera.Camera;
import config.GameConfig;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import utils.Assets;
import utils.SoundManager;

public class Minotaur extends Monster {
	private int currentWalkFrame, currentAttackFrame;
	private long lastAttackTime, lastFrameTime;
	private boolean attacking, facingRight;
	private final Image[] walkFrames, attackFrames;

	public Minotaur(double x, double y, Character player) {
		super(x, y, player);
		this.currentWalkFrame = 0;
		this.currentAttackFrame = 0;
		this.lastAttackTime = 0;
		this.lastFrameTime = 0;
		this.attacking = false;
		this.facingRight = false;
		this.currentHealth = GameConfig.MINOTAUR_MAX_HEALTH;
		walkFrames = new Image[] { Assets.loadImage("minotaur/Monster_walk01.png"),
				Assets.loadImage("minotaur/Monster_walk02.png"), Assets.loadImage("minotaur/Monster_walk03.png"),
				Assets.loadImage("minotaur/Monster_walk04.png"), Assets.loadImage("minotaur/Monster_walk05.png") };
		attackFrames = new Image[] { Assets.loadImage("minotaur/Monster_attack01.png"),
				Assets.loadImage("minotaur/Monster_attack01.png"), Assets.loadImage("minotaur/Monster_attack02.png"),
				Assets.loadImage("minotaur/Monster_attack02.png"), Assets.loadImage("minotaur/Monster_attack03.png"),
				Assets.loadImage("minotaur/Monster_attack03.png") };
	}

	@Override
	public void update() {
		updateSlowStatus();
		updateDebuffStatus();
		Character player = getPlayer();
		if (player == null)
			return;
		double dx = player.getX() - this.x;
		facingRight = dx > 0;

		if (attacking) {
			updateAttackAnimation();
			return;
		}

		if (Math.abs(dx) > GameConfig.MONSTER_ATTACK_RANGE)
			moveTowardPlayer(dx);
		else
			tryAttack(player);
		
	}

	@Override
	public void render(GraphicsContext gc, Camera camera) {
		Image frame = attacking ? attackFrames[currentAttackFrame] : walkFrames[currentWalkFrame];
		double drawX = x - camera.getX(), drawY = y - camera.getY();
		double drawWidth = frame.getWidth() * 2, drawHeight = frame.getHeight() * 2;
		drawHealthBar(gc, drawX, drawY);

		if (facingRight)
			gc.drawImage(frame, drawX, drawY, drawWidth, drawHeight);
		else
			gc.drawImage(frame, 0, 0, frame.getWidth(), frame.getHeight(), drawX + drawWidth, drawY, -drawWidth,
					drawHeight);
	}

	private void drawHealthBar(GraphicsContext gc, double drawX, double drawY) {
		double healthPercent = (double) currentHealth / GameConfig.MONSTER_MAX_HEALTH;
		gc.setFill(Color.LIMEGREEN);
		gc.fillRect(drawX, drawY - 10, 40 * healthPercent, 5);
	}

	private void moveTowardPlayer(double dx) {
		if (dx > 0) {
			x += GameConfig.MINOTAUR_SPEED * getSpeedMultipiler();
		} else {
			x -= GameConfig.MINOTAUR_SPEED * getSpeedMultipiler();
		}
		if (System.currentTimeMillis() - lastFrameTime > 200) {
			currentWalkFrame = (currentWalkFrame + 1) % walkFrames.length;
			lastFrameTime = System.currentTimeMillis();
		}
	}

	private void tryAttack(Character player) {
		long now = System.currentTimeMillis();
		if (now - lastAttackTime > GameConfig.MONSTER_ATTACK_COOLDOWN) {
			attacking = true;
			currentAttackFrame = 0;
			lastAttackTime = now;
			if (!player.isDead()) {
				SoundManager.playSEF("effects/draw-a-sword-327726.mp3", 0.7);
			}
			if (player instanceof SamuraiMelee) {
				player.takeDamage(GameConfig.MINOTAUR_DMG_MELEE);
			} else if (player instanceof SamuraiArcher) {
				player.takeDamage(GameConfig.MINOTAUR_DMG_ARCHER);
			} else {
				player.takeDamage(GameConfig.MINOTAUR_DMG_COMMANDER);
			}
		}
	}

	private void updateAttackAnimation() {
		if (System.currentTimeMillis() - lastFrameTime > 150) {
			currentAttackFrame++;
			lastFrameTime = System.currentTimeMillis();
			if (currentAttackFrame >= attackFrames.length) {
				attacking = false;
				currentAttackFrame = 0;
			}
		}
	}

	@Override
	public Rectangle2D getHitbox() {
		Image frame = walkFrames[0];
		return new Rectangle2D(x, y, frame.getWidth() * 2, frame.getHeight() * 2);
	}

	@Override
	public boolean isDead() {
		return currentHealth <= 0;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}
}
