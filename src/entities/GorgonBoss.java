package entities;

import camera.Camera;
import config.GameConfig;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.Assets;
import utils.SoundManager;
import interfaces.*;

public class GorgonBoss extends Monster implements Renderable, Updatable {
	private int currentWalkFrame, currentAttackFrame, currentDeathFrame;
	private long lastAttackTime, lastFrameTime, lastDeathFrameTime;
	private boolean attacking, facingRight;
	private final Image[] walkFrames, attackFrames, deathFrames;
	private boolean dying;

	public GorgonBoss(double x, double y, Character player) {
		super(x, y, player);
		this.currentWalkFrame = 0;
		this.currentAttackFrame = 0;
		this.lastAttackTime = 0;
		this.lastFrameTime = 0;
		this.attacking = false;
		this.facingRight = false;
		this.dying = false;
		this.currentDeathFrame = 0;
		this.lastDeathFrameTime = 0;
		setCurrentHealth(GameConfig.BOSS_MAX_HEALTH);
		walkFrames = new Image[] { Assets.loadImage("gorgon/ggWalk_0.png"), Assets.loadImage("gorgon/ggWalk_1.png"),
				Assets.loadImage("gorgon/ggWalk_2.png"), Assets.loadImage("gorgon/ggWalk_3.png"),
				Assets.loadImage("gorgon/ggWalk_4.png"), Assets.loadImage("gorgon/ggWalk_5.png"),
				Assets.loadImage("gorgon/ggWalk_6.png"), Assets.loadImage("gorgon/ggWalk_7.png"),
				Assets.loadImage("gorgon/ggWalk_8.png"), Assets.loadImage("gorgon/ggWalk_9.png"),
				Assets.loadImage("gorgon/ggWalk_10.png"), Assets.loadImage("gorgon/ggWalk_11.png"),
				Assets.loadImage("gorgon/ggWalk_12.png"), };
		attackFrames = new Image[] { Assets.loadImage("gorgon/ggAttack_0.png"),
				Assets.loadImage("gorgon/ggAttack_1.png"), Assets.loadImage("gorgon/ggAttack_2.png"),
				Assets.loadImage("gorgon/ggAttack_3.png"), Assets.loadImage("gorgon/ggAttack_4.png"),
				Assets.loadImage("gorgon/ggAttack_5.png"), Assets.loadImage("gorgon/ggAttack_6.png"),
				Assets.loadImage("gorgon/ggAttack_7.png"), Assets.loadImage("gorgon/ggAttack_8.png"),
				Assets.loadImage("gorgon/ggAttack_9.png"), };
		deathFrames = new Image[] { Assets.loadImage("gorgon/ggDead_0.png"), Assets.loadImage("gorgon/ggDead_0.png"),
				Assets.loadImage("gorgon/ggDead_1.png"), Assets.loadImage("gorgon/ggDead_1.png"),
				Assets.loadImage("gorgon/ggDead_2.png"), Assets.loadImage("gorgon/ggDead_2.png") };

	}

	@Override
	public void update() {

		if (dying) {
			if (System.currentTimeMillis() - lastDeathFrameTime > 200) {
				currentDeathFrame++;
				lastDeathFrameTime = System.currentTimeMillis();
			}
			return;
		}

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
		Image frame;

		if (dying) {
			int frameIndex = Math.min(currentDeathFrame, deathFrames.length - 1);
			frame = deathFrames[frameIndex];
		} else {
			if (attacking) {
				frame = attackFrames[currentAttackFrame];
			} else {
				frame = walkFrames[currentWalkFrame];
			}
		}

		double baseHeight = walkFrames[0].getHeight();
		double drawX = x - camera.getX();
		double drawY = y - camera.getY() - (frame.getHeight() - baseHeight);
		double drawWidth = frame.getWidth() * 2, drawHeight = frame.getHeight() * 2;

		if (facingRight)
			gc.drawImage(frame, drawX, drawY, drawWidth, drawHeight);
		else
			gc.drawImage(frame, 0, 0, frame.getWidth(), frame.getHeight(), drawX + drawWidth, drawY, -drawWidth,
					drawHeight);
	}

	@Override
	public void moveTowardPlayer(double dx) {
		if (dx > 0) {
			x += GameConfig.BOSS_SPEED * getSpeedMultipiler();
		} else {
			x -= GameConfig.BOSS_SPEED * getSpeedMultipiler();
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
				SoundManager.playSEF("effects/weretiger-yarik-roar-5-321201.mp3", 0.8);
			}
			if (player instanceof SamuraiMelee) {
				player.takeDamage(GameConfig.BOSS_DMG_MELEE);
			} else if (player instanceof SamuraiArcher) {
				player.takeDamage(GameConfig.BOSS_DMG_ARCHER);
			} else {
				player.takeDamage(GameConfig.BOSS_DMG_COMMANDER);
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
	public void takeDamage(int damage) {
		int finalDamage = (int) (damage * damageMultiplier);
		currentHealth -= finalDamage;
		if (currentHealth <= 0 && !dying) {
			currentHealth = 0;
			dying = true;
			currentDeathFrame = 0;
			lastDeathFrameTime = System.currentTimeMillis();
		}
	}

	@Override
	public Rectangle2D getHitbox() {
		Image frame = walkFrames[0];

		double hitboxWidth = frame.getWidth() * 0.6;
		double hitboxheight = frame.getHeight() * 2;
		double hitboxX = x + 200;
		double hitboxY = y;

		return new Rectangle2D(hitboxX, hitboxY, hitboxWidth, hitboxheight);
	}

	@Override
	public boolean isDead() {
		return dying && currentDeathFrame >= deathFrames.length;
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