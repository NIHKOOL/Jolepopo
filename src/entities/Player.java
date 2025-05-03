package entities;

import camera.Camera;
import config.GameConfig;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import utils.Assets;

public class Player {
    private double x, y;
    private double velocityY = 0;
    private boolean onGround = true;
    private boolean facingRight = true;

    private int currentHealth = GameConfig.PLAYER_MAX_HEALTH;
    private double currentMana = GameConfig.PLAYER_MAX_MANA;

    private boolean dashing = false;
    private long dashStartTime = 0;
    private long lastDashTime = 0;
    private int dashFrame = 0;
    private long lastDashFrameTime = 0;

    private boolean attacking = false;
    private long lastAttackTime = 0;
    private int currentAttackFrame = 0;
    private long lastAttackFrameTime = 0;

    private boolean defending = false;
    private long lastDefendTime = 0;
    private int currentDefendFrame = 0;
    private long lastDefendFrameTime = 0;

    private int currentFrame = 0;
    private long lastFrameTime = 0;
    private int jumpFrame = 0;
    private long lastJumpFrameTime = 0;

    private Image[] walkFrames, dashFrames, jumpFrames, attackFrames, defendFrames;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;

        walkFrames = new Image[] {
            Assets.loadImage("RealKnight.png"), Assets.loadImage("walk02.png"), Assets.loadImage("walk03.png"),
            Assets.loadImage("walk04.png"), Assets.loadImage("walk05.png"), Assets.loadImage("walk06.png"),
            Assets.loadImage("walk07.png"), Assets.loadImage("walk08.png"), Assets.loadImage("walk09.png")
        };

        dashFrames = new Image[] {
            Assets.loadImage("dash01.png"), Assets.loadImage("dash02.png"),
            Assets.loadImage("dash03.png"), Assets.loadImage("dash04.png")
        };

        jumpFrames = new Image[] {
            Assets.loadImage("jump01.png"), Assets.loadImage("jump02.png"),
            Assets.loadImage("jump03.png"), Assets.loadImage("jump04.png"), Assets.loadImage("jump05.png")
        };

        attackFrames = new Image[] {
            Assets.loadImage("attack01.png"), Assets.loadImage("dash04.png"), Assets.loadImage("attack05.png")
        };

        defendFrames = new Image[] {
            Assets.loadImage("defend01.png"), Assets.loadImage("defend02.png")
        };
    }

    public void update(boolean left, boolean right) {
        long now = System.currentTimeMillis();
        updateAttack(now);
        updateDefend(now);
        updateDash(now);
        updateMovement(now, left, right);
        updateJump(now);
        regenMana();
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

    private void updateAttack(long now) {
        if (attacking && now - lastAttackFrameTime > GameConfig.ATTACK_FRAME_INTERVAL) {
            currentAttackFrame++;
            lastAttackFrameTime = now;
            if (currentAttackFrame >= attackFrames.length) {
                attacking = false;
                currentAttackFrame = 0;
            }
        }
    }

    private void updateDefend(long now) {
        if (defending && now - lastDefendFrameTime > GameConfig.DEFEND_FRAME_INTERVAL) {
            currentDefendFrame++;
            lastDefendFrameTime = now;
            if (currentDefendFrame >= defendFrames.length) {
                defending = false;
                currentDefendFrame = 0;
            }
        }
    }

    private void updateMovement(long now, boolean left, boolean right) {
        if (!dashing) {
            if (left) {
                x -= GameConfig.PLAYER_SPEED;
                facingRight = false;
            }
            if (right) {
                x += GameConfig.PLAYER_SPEED;
                facingRight = true;
            }

            if (now - lastFrameTime > 150 && (left || right)) {
                currentFrame = (currentFrame + 1) % walkFrames.length;
                lastFrameTime = now;
            }
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
        } else {
            onGround = false;
        }
    }

    private void regenMana() {
        if (!dashing && currentMana < GameConfig.PLAYER_MAX_MANA) {
            currentMana += GameConfig.MANA_REGEN;
            if (currentMana > GameConfig.PLAYER_MAX_MANA) currentMana = GameConfig.PLAYER_MAX_MANA;
        }
    }

    public void attack() {
        long now = System.currentTimeMillis();
        if (!attacking && now - lastAttackTime >= GameConfig.ATTACK_COOLDOWN) {
            attacking = true;
            currentAttackFrame = 0;
            lastAttackFrameTime = now;
            lastAttackTime = now;
        }
    }

    public void defend() {
        long now = System.currentTimeMillis();
        if (!defending && now - lastDefendTime >= GameConfig.DEFEND_DURATION) {
            defending = true;
            currentDefendFrame = 0;
            lastDefendFrameTime = now;
            lastDefendTime = now;
        }
    }

    public void dash() {
        long now = System.currentTimeMillis();
        if (!dashing && now - lastDashTime >= GameConfig.DASH_COOLDOWN && currentMana >= GameConfig.DASH_MANA_COST) {
            dashing = true;
            dashStartTime = now;
            lastDashTime = now;
            currentMana -= GameConfig.DASH_MANA_COST;
        }
    }

    public void jump() {
        if (onGround) {
            velocityY = GameConfig.JUMP_STRENGTH;
            onGround = false;
        }
    }

    public void render(GraphicsContext gc, Camera camera) {
        Image frame = getCurrentFrame();

        double drawX = x - camera.getX();
        double drawY = y - camera.getY();
        double drawWidth = frame.getWidth() * 2;
        double drawHeight = frame.getHeight() * 2;

        double barWidth = 50;
        double barHeight = 6;
        double barX = drawX + (drawWidth / 2) - (barWidth / 2);
        double barY = drawY - 15;

        gc.setFill(Color.GRAY);
        gc.fillRect(barX, barY, barWidth, barHeight);

        gc.setFill(Color.RED);
        gc.fillRect(barX, barY, barWidth * currentHealth / GameConfig.PLAYER_MAX_HEALTH, barHeight);

        if (facingRight) {
            gc.drawImage(frame, drawX, drawY, drawWidth, drawHeight);
        } else {
            gc.drawImage(frame, 0, 0, frame.getWidth(), frame.getHeight(),
                         drawX + drawWidth, drawY, -drawWidth, drawHeight);
        }
    }

    private Image getCurrentFrame() {
        if (attacking) return attackFrames[currentAttackFrame];
        if (defending) return defendFrames[currentDefendFrame];
        if (dashing) return dashFrames[dashFrame];
        if (!onGround) return jumpFrames[jumpFrame];
        return walkFrames[currentFrame];
    }

    public void takeDamage(int damage) {
        currentHealth -= damage;
        if (currentHealth < 0) currentHealth = 0;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public int getCurrentHealth() { return currentHealth; }
    public int getMaxHealth() { return GameConfig.PLAYER_MAX_HEALTH; }
    public double getCurrentMana() { return currentMana; }
    public int getMaxMana() { return GameConfig.PLAYER_MAX_MANA; }
}
