package entities;

import java.util.List;

import camera.Camera;
import config.GameConfig;
import interfaces.AbilityCaster;
import interfaces.Controllable;
import interfaces.Damagable;
import interfaces.Renderable;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import utils.Assets;
import utils.SoundManager;

public class SamuraiCommander extends Character implements AbilityCaster, Renderable, Controllable, Damagable {
    private double velocityY = 0;
    private boolean onGround = true;

    private boolean dashing = false;
    private long dashStartTime = 0;
    private long lastDashTime = 0;
    private int dashFrame = 0;
    private long lastDashFrameTime = 0;

    private int currentAttackFrame = 0;
    private long lastAttackFrameTime = 0;

    private boolean defending = false;
    private int currentDefendFrame = 0;
    private long lastDefendFrameTime = 0;

    private int currentFrame = 0;
    private long lastFrameTime = 0;
    private int jumpFrame = 0;
    private long lastJumpFrameTime = 0;

    private final Image[] walkFrames, dashFrames, jumpFrames, attackFrames, defendFrames;
    
    private List<Monster> monsters;
    

    public SamuraiCommander(double x, double y, List<Monster> monsters) {
        this.x = x;
        this.y = y;
        this.monsters = monsters;
        this.currentHealth = GameConfig.PLAYER_MAX_HEALTH;
        this.currentMana = GameConfig.PLAYER_MAX_MANA;

        walkFrames = new Image[] {
            Assets.loadImage("samuraiCommander/cmWalk_0.png"),
            Assets.loadImage("samuraiCommander/cmWalk_1.png"),
            Assets.loadImage("samuraiCommander/cmWalk_2.png"),
            Assets.loadImage("samuraiCommander/cmWalk_3.png"),
            Assets.loadImage("samuraiCommander/cmWalk_4.png"),
            Assets.loadImage("samuraiCommander/cmWalk_5.png"),
            Assets.loadImage("samuraiCommander/cmWalk_6.png"),
            Assets.loadImage("samuraiCommander/cmWalk_7.png"),

        };

        dashFrames = new Image[] {
            Assets.loadImage("samuraiCommander/cmDash_3.png"),
            Assets.loadImage("samuraiCommander/cmDash_4.png"),
            Assets.loadImage("samuraiCommander/cmDash_5.png"),
            Assets.loadImage("samuraiCommander/cmDash_4.png"),
            Assets.loadImage("samuraiCommander/cmDash_3.png"),
        };

        jumpFrames = new Image[] {
            Assets.loadImage("samuraiCommander/cmJump_0.png"),
            Assets.loadImage("samuraiCommander/cmJump_1.png"),
            Assets.loadImage("samuraiCommander/cmJump_2.png"),
            Assets.loadImage("samuraiCommander/cmJump_3.png"),
            Assets.loadImage("samuraiCommander/cmJump_4.png"),
            Assets.loadImage("samuraiCommander/cmJump_5.png"),
            Assets.loadImage("samuraiCommander/cmJump_6.png"),
            
        };

        attackFrames = new Image[] {
            Assets.loadImage("samuraiCommander/cmBless_0.png"),
            Assets.loadImage("samuraiCommander/cmBless_1.png"),
            Assets.loadImage("samuraiCommander/cmBless_2.png"),
            Assets.loadImage("samuraiCommander/cmBless_3.png"),
            
        };

        defendFrames = new Image[] {
            Assets.loadImage("samuraiCommander/cmCurse_0.png"),
            Assets.loadImage("samuraiCommander/cmCurse_1.png"),
            Assets.loadImage("samuraiCommander/cmCurse_2.png"),
            Assets.loadImage("samuraiCommander/cmCurse_3.png")
        };
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
        
    }

    @Override
    public void render(GraphicsContext gc, Camera camera) {
        Image frame = getCurrentFrame();
        double baseHeight = walkFrames[0].getHeight();
        double drawX = x - camera.getX();
        double drawY = y - camera.getY() - (frame.getHeight() - baseHeight) * 2;
        double drawWidth = frame.getWidth() * 2;
        double drawHeight = frame.getHeight() * 2;

        double barWidth = 80;
        double barHeight = 6;
        double barX = drawX + (drawWidth / 2) - (barWidth / 2);
        double barY = drawY - 15;

        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(barX, barY, barWidth, barHeight);
        gc.setFill(Color.YELLOW);
        double hpRatio = Math.min((currentHealth) / (double) GameConfig.PLAYER_MAX_HEALTH, 1.0);
        gc.fillRect(barX, barY, barWidth * hpRatio, barHeight);
        gc.setFill(Color.RED);
        gc.fillRect(barX, barY, barWidth * currentHealth / GameConfig.PLAYER_MAX_HEALTH, barHeight);

        if (facingRight) {
            gc.drawImage(frame, drawX, drawY, drawWidth, drawHeight);
        } else {
            gc.drawImage(frame, 0, 0, frame.getWidth(), frame.getHeight(),
                         drawX + drawWidth, drawY, -drawWidth, drawHeight);
        }
    }

    @Override
    public void abilityOne() {
    	attacking = true;
    	System.currentTimeMillis();
        for (Monster m : monsters) {
        	double distance = Math.abs(this.x - m.getX());
        	if (distance < 600) {
        		m.applySlow(5000);
        		SoundManager.playSEF("effects/dizzy-ellectric-bolt-spell-1-186768.mp3", 0.7);
        	}
        
        }
    
    }

    private void updateAbilityOne(long now) {
        if (attacking && now - lastAttackFrameTime > GameConfig.ATTACK_FRAME_INTERVAL) {
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
            double dashDirection = facingRight ? 1 : -1;
            x += dashDirection * GameConfig.DASH_SPEED;
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

        if (y >= GameConfig.GROUND_LEVEL) {
            y = GameConfig.GROUND_LEVEL;
            velocityY = 0;
            onGround = true;
        }
    }

    @Override
    public void abilityTwo() {
        defending = true;
        System.currentTimeMillis();
        for (Monster m : monsters) {
        	double distance = Math.abs(this.x - m.getX());
        	if (distance < 600) {
        		m.applyDamageDebuff(15000);
        		SoundManager.playSEF("effects/enchanted-spell-casting-229208.mp3", 0.5);
        	}
        
        }
    }

    private void updateAbilityTwo(long now) {
        if (defending && now - lastDefendFrameTime > GameConfig.DEFEND_FRAME_INTERVAL) {
            currentDefendFrame++;
            lastDefendFrameTime = now;
            if (currentDefendFrame >= defendFrames.length) {
                defending = false;
                currentDefendFrame = 0;
            }
        }
    }

    @Override
    public Rectangle2D getAttackBox() {
        double attackWidth = 50;
        double attackHeight = 60;
        double offsetX = facingRight ? 40 : -40;
        return new Rectangle2D(x + offsetX, y, attackWidth, attackHeight);
    }

    private void updateMovement(long now, boolean left, boolean right, boolean isScrollable) {
        if (!dashing) {
            if (left) {
                x -= GameConfig.PLAYER_SPEED + 4;
                facingRight = false;
            }
            if (right) {
                x += GameConfig.PLAYER_SPEED + 4;
                facingRight = true;
            }
            
            applyMapBounds(walkFrames[0].getWidth() * 2, isScrollable);
            
            if (now - lastFrameTime > 150 && (left || right) && !(isDead()) && !(isWin())) {
                currentFrame = (currentFrame + 1) % walkFrames.length;
                lastFrameTime = now;
                if (onGround) {
                    SoundManager.playSEF("effects/st3-footstep-sfx-323056.mp3", 1, 200);
                }
            }
        }
    }

    private void regenMana() {
        if (!dashing && currentMana < GameConfig.PLAYER_MAX_MANA) {
            currentMana += GameConfig.MANA_REGEN;
            if (currentMana > GameConfig.PLAYER_MAX_MANA) currentMana = GameConfig.PLAYER_MAX_MANA;
        }
    }

    private Image getCurrentFrame() {
        if (attacking) return attackFrames[currentAttackFrame];
        if (defending) return defendFrames[currentDefendFrame];
        if (dashing) return dashFrames[dashFrame];
        if (!onGround) return jumpFrames[jumpFrame];
        return walkFrames[currentFrame];
    }

    @Override
    public void takeDamage(int damage) {
        currentHealth -= damage;
        if (currentHealth < 0) currentHealth = 0;
        if (currentHealth == 0) dead = true;
    }

    @Override
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean isAttacking() { return attacking;}
    @Override
    public boolean isDead() { return dead;}
    @Override
    public double getX() { return x;}
    @Override
    public double getY() { return y;}
    @Override
    public int getCurrentHealth() { return currentHealth;}
    @Override
    public int getMaxHealth() { return GameConfig.PLAYER_MAX_HEALTH;}
    @Override
    public double getCurrentMana() { return currentMana;}
    @Override
    public int getMaxMana() { return GameConfig.PLAYER_MAX_MANA;}
}
