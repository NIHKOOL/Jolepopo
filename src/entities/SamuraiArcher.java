package entities;

import camera.Camera;
import config.GameConfig;
import entities.projectiles.Arrow;
import entities.projectiles.BigArrow;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import utils.Assets;
import utils.SoundManager;

import java.util.ArrayList;
import java.util.List;

public class SamuraiArcher extends Character {
	private double velocityY;
	private boolean onGround;
	private boolean dashing;
	private long dashStartTime;
	private long lastDashTime;
	private int dashFrame;
	private long lastDashFrameTime;
	private long lastAttackTime;
	private int currentAttackFrame;
	private long lastAttackFrameTime;
	private boolean shooting;
	private int currentShootingFrame;
	private long lastShootingFrameTime;
	private int currentFrame;
	private long lastFrameTime;
	private int jumpFrame;
	private long lastJumpFrameTime;
	private final Image[] walkFrames, dashFrames, jumpFrames, attackFrames, defendFrames;

	private final List<Arrow> arrows = new ArrayList<>();
	private final List<BigArrow> bigArrows = new ArrayList<>();
	private long lastBigArrowTime;
	private static long bigArrowCooldown;

	public SamuraiArcher(double x, double y) {
		this.x = x;
		this.y = y;
		this.currentHealth = GameConfig.PLAYER_MAX_HEALTH;
		this.currentMana = GameConfig.PLAYER_MAX_MANA;
		this.velocityY = 0;
		this.onGround = true;
		this.dashing = false;
		this.dashStartTime = 0;
		this.lastDashTime = 0;
		this.dashFrame = 0;
		this.lastDashFrameTime = 0;
		this.lastAttackTime = 0;
		this.currentAttackFrame = 0;
		this.lastAttackFrameTime = 0;
		this.shooting = false;
		this.currentShootingFrame = 0;
		this.lastShootingFrameTime = 0;
		this.currentFrame = 0;
		this.lastFrameTime = 0;
		this.jumpFrame = 0;
		this.lastJumpFrameTime = 0;
		this.lastBigArrowTime = 0;
		SamuraiArcher.bigArrowCooldown = GameConfig.BIG_ARROW_COOLDOWN;

		walkFrames = new Image[] { Assets.loadImage("samuraiArcher/arWalk_1.png"),
				Assets.loadImage("samuraiArcher/arWalk_2.png"), Assets.loadImage("samuraiArcher/arWalk_3.png"),
				Assets.loadImage("samuraiArcher/arWalk_4.png"), Assets.loadImage("samuraiArcher/arWalk_5.png"),
				Assets.loadImage("samuraiArcher/arWalk_6.png"), Assets.loadImage("samuraiArcher/arWalk_7.png"),
				Assets.loadImage("samuraiArcher/arWalk_8.png") };

		dashFrames = new Image[] { Assets.loadImage("samuraiArcher/arDash_1.png"),
				Assets.loadImage("samuraiArcher/arDash_2.png"), Assets.loadImage("samuraiArcher/arDash_3.png"),
				Assets.loadImage("samuraiArcher/arDash_4.png") };

		jumpFrames = new Image[] { Assets.loadImage("samuraiArcher/arJump_1.png"),
				Assets.loadImage("samuraiArcher/arJump_2.png"), Assets.loadImage("samuraiArcher/arJump_3.png"),
				Assets.loadImage("samuraiArcher/arJump_4.png"), Assets.loadImage("samuraiArcher/arJump_5.png"),
				Assets.loadImage("samuraiArcher/arJump_6.png"), Assets.loadImage("samuraiArcher/arJump_7.png") };

		attackFrames = new Image[] { Assets.loadImage("samuraiArcher/arShoot_0.png"),
				Assets.loadImage("samuraiArcher/arShoot_1.png"), Assets.loadImage("samuraiArcher/arShoot_2.png"),
				Assets.loadImage("samuraiArcher/arShoot_3.png"), Assets.loadImage("samuraiArcher/arShoot_4.png"),
				Assets.loadImage("samuraiArcher/arShoot_5.png"), Assets.loadImage("samuraiArcher/arShoot_6.png"),
				Assets.loadImage("samuraiArcher/arShoot_7.png"), Assets.loadImage("samuraiArcher/arShoot_8.png"),
				Assets.loadImage("samuraiArcher/arShoot_9.png"), Assets.loadImage("samuraiArcher/arShoot_10.png"),
				Assets.loadImage("samuraiArcher/arShoot_11.png"), Assets.loadImage("samuraiArcher/arShoot_12.png"),
				Assets.loadImage("samuraiArcher/arShoot_13.png") };

		defendFrames = new Image[] { Assets.loadImage("samuraiArcher/arShoot_5.png"),
				Assets.loadImage("samuraiArcher/arShoot_6.png"), Assets.loadImage("samuraiArcher/arShoot_7.png"),
				Assets.loadImage("samuraiArcher/arShoot_8.png"), Assets.loadImage("samuraiArcher/arShoot_9.png"),
				Assets.loadImage("samuraiArcher/arShoot_10.png"), Assets.loadImage("samuraiArcher/arShoot_11.png"),
				Assets.loadImage("samuraiArcher/arShoot_12.png"), Assets.loadImage("samuraiArcher/arShoot_13.png") };
	}

	@Override
	public void update(boolean left, boolean right, boolean isScrollable) {
		long now = System.currentTimeMillis();
		updateAbilityOne(now);
		updateAbilityTwo(now);
		updateDash(now);
		updateMovement(now, left, right, isScrollable);
		updateJump(now);
		regenMana();
		arrows.forEach(Arrow::update);
		arrows.removeIf(a -> !a.isActive());
		bigArrows.forEach(BigArrow::update);
		bigArrows.removeIf(b -> !b.isActive());
	}

	@Override
	public void render(GraphicsContext gc, Camera camera) {
		Image frame = getCurrentFrame();
		double baseHeight = walkFrames[0].getHeight();
		double drawX = x - camera.getX() - 50;
		double drawY = y - camera.getY() - (frame.getHeight() - baseHeight) * 2;
		double drawWidth = frame.getWidth() * 2;
		double drawHeight = frame.getHeight() * 2;

		double barWidth = 80, barHeight = 6;
		double barX = drawX + (drawWidth / 2) - (barWidth / 2);
		double barY = drawY - 15;

		gc.setFill(Color.LIGHTGRAY);
		gc.fillRect(barX, barY, barWidth, barHeight);
		gc.setFill(Color.YELLOW);
		double hpRatio = Math.min(currentHealth / (double) GameConfig.PLAYER_MAX_HEALTH, 1.0);
		gc.fillRect(barX, barY, barWidth * hpRatio, barHeight);
		gc.setFill(Color.RED);
		gc.fillRect(barX, barY, barWidth * currentHealth / GameConfig.PLAYER_MAX_HEALTH, barHeight);

		if (facingRight)
			gc.drawImage(frame, drawX, drawY, drawWidth, drawHeight);
		else
			gc.drawImage(frame, 0, 0, frame.getWidth(), frame.getHeight(), drawX + drawWidth, drawY, -drawWidth,
					drawHeight);

		arrows.forEach(a -> a.render(gc, camera));
		bigArrows.forEach(b -> b.render(gc, camera));
	}

	@Override
	public void abilityOne() {
		long now = System.currentTimeMillis();
		if (!attacking && now - lastAttackTime >= GameConfig.ATTACK_COOLDOWN) {
			attacking = true;
			currentAttackFrame = 0;
			lastAttackFrameTime = now;
			lastAttackTime = now;
			double arrowX = facingRight ? x + 40 : x - 40;
			arrows.add(new Arrow(arrowX, y - 20, facingRight));
			currentMana = Math.max(currentMana - 20, 0);
			SoundManager.playSEF("effects/bow-release-bow-and-arrow-4-101936.mp3", 10);
		}
	}

	private void updateAbilityOne(long now) {
		if (attacking && now - lastAttackFrameTime > GameConfig.ATTACK_FRAME_INTERVAL - 85) {
			currentAttackFrame++;
			lastAttackFrameTime = now;
			if (currentAttackFrame >= attackFrames.length) {
				attacking = false;
				currentAttackFrame = 0;
			}
		}
	}

	@Override
	public void dash() {
		long now = System.currentTimeMillis();
		if (!dashing && now - lastDashTime >= GameConfig.DASH_COOLDOWN && currentMana >= GameConfig.DASH_MANA_COST) {
			dashing = true;
			dashStartTime = now;
			lastDashTime = now;
			currentMana -= GameConfig.DASH_MANA_COST;
			SoundManager.playSEF("effects/clean-fast-swooshaiff-14784.mp3", 1);
		}
	}

	private void updateDash(long now) {
		if (dashing) {
			x += (facingRight ? 1 : -1) * GameConfig.DASH_SPEED * -1;
			if (now - lastDashFrameTime > 60) {
				dashFrame = (dashFrame + 1) % dashFrames.length;
				lastDashFrameTime = now;
			}
			if (now - dashStartTime > GameConfig.DASH_DURATION) {
				dashing = false;
				dashFrame = 0;
			}
		}
	}

	@Override
	public void jump() {
		if (onGround) {
			velocityY = GameConfig.JUMP_STRENGTH;
			onGround = false;
			SoundManager.playSEF("effects/metal-crunch-263638.mp3", 1);
		}
	}

	private void updateJump(long now) {
		velocityY += GameConfig.GRAVITY;
		y += velocityY;

		if (!onGround && now - lastJumpFrameTime > 100) {
			jumpFrame = (jumpFrame + 1) % jumpFrames.length;
			lastJumpFrameTime = now;
		}

		if (y >= GameConfig.GROUND_LEVEL - 20) {
			y = GameConfig.GROUND_LEVEL - 20;
			velocityY = 0;
			onGround = true;
		}
	}

	@Override
	public void abilityTwo() {
		long now = System.currentTimeMillis();
		if (now - lastBigArrowTime >= bigArrowCooldown && currentMana >= GameConfig.BIG_ARROW_MANA_COST) {
			double arrowX = facingRight ? x + 40 : x - 40;
			bigArrows.add(new BigArrow(arrowX, y - 20, facingRight));
			currentMana = Math.max(currentMana - GameConfig.BIG_ARROW_MANA_COST, 0);
			lastBigArrowTime = now;
			SoundManager.playSEF("effects/metallic-latch-release-43678.mp3", 3);

			shooting = true;
			currentShootingFrame = 0;
			lastShootingFrameTime = now;
		}
	}

	private void updateAbilityTwo(long now) {
		if (shooting && now - lastShootingFrameTime > GameConfig.SHOOTING_FRAME_INTERVAL) {
			currentShootingFrame++;
			lastShootingFrameTime = now;
			if (currentShootingFrame >= defendFrames.length) {
				shooting = false;
				currentShootingFrame = 0;
			}
		}
	}

	@Override
	public Rectangle2D getAttackBox() {
		double attackWidth = 50, attackHeight = 60;
		double offsetX = facingRight ? 40 : -40;
		return new Rectangle2D(x + offsetX, y, attackWidth, attackHeight);
	}

	private void updateMovement(long now, boolean left, boolean right, boolean isScrollable) {
		if (!dashing) {
			if (left) {
				x -= GameConfig.PLAYER_SPEED - 1;
				facingRight = false;
			}
			if (right) {
				x += GameConfig.PLAYER_SPEED - 1;
				facingRight = true;
			}

			applyMapBounds(walkFrames[0].getWidth() * 2, isScrollable);

			if (now - lastFrameTime > 150 && (left || right) && !(isDead()) && !(isWin())) {
				currentFrame = (currentFrame + 1) % walkFrames.length;
				lastFrameTime = now;
				if (onGround)
					SoundManager.playSEF("effects/st3-footstep-sfx-323056.mp3", 1, 625);
			}
		}
	}

	private void regenMana() {
		if (!dashing && currentMana < GameConfig.PLAYER_MAX_MANA) {
			currentMana += GameConfig.MANA_REGEN * 2;
			currentMana = Math.min(currentMana, GameConfig.PLAYER_MAX_MANA);
		}
	}

	private Image getCurrentFrame() {
		if (attacking)
			return attackFrames[currentAttackFrame];
		if (shooting)
			return defendFrames[currentShootingFrame];
		if (dashing)
			return dashFrames[dashFrame];
		if (!onGround)
			return jumpFrames[jumpFrame];
		return walkFrames[currentFrame];
	}

	@Override
	public void takeDamage(int damage) {
		currentHealth -= damage;
		if (currentHealth < 0)
			currentHealth = 0;
		if (currentHealth == 0)
			dead = true;
	}

	public void clearProjectiles() {
		arrows.clear();
		bigArrows.clear();
	}

	@Override
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean isAttacking() {
		return attacking;
	}

	@Override
	public boolean isDead() {
		return dead;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public int getCurrentHealth() {
		return currentHealth;
	}

	@Override
	public int getMaxHealth() {
		return GameConfig.PLAYER_MAX_HEALTH;
	}

	@Override
	public double getCurrentMana() {
		return currentMana;
	}

	@Override
	public int getMaxMana() {
		return GameConfig.PLAYER_MAX_MANA;
	}

	public List<Arrow> getArrows() {
		return arrows;
	}

	public List<BigArrow> getBigArrows() {
		return bigArrows;
	}
}
