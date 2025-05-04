package entities;

import camera.Camera;
import config.GameConfig;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import utils.Assets;
import utils.SoundManager;

public class SamuraiMelee extends Character{
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

    private boolean dead = false;
    
    private int tempHealth = 0;
    private long tempHealthStartTime = 0;
    private static final int TEMP_HEAL_AMOUNT = 10;
    private static final long TEMP_HEAL_DURATION = 1000;
    
    public SamuraiMelee(double x, double y) {
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

        defendFrames = new Image[] {
            Assets.loadImage("defend01.png"), 
            Assets.loadImage("defend02.png")
        };
    }
    
    @Override
    public void update(boolean left, boolean right) {
        long now = System.currentTimeMillis();
        updateAttack(now);
        updateDefend(now);
        updateDash(now);
        updateMovement(now, left, right);
        updateJump(now);
        regenMana();
        if (System.currentTimeMillis() - tempHealthStartTime > TEMP_HEAL_DURATION) {
        	tempHealth = 0;
        }
        
    }
    
    @Override
    public void render(GraphicsContext gc, Camera camera) {
        Image frame = getCurrentFrame();

        double drawX = x - camera.getX();
        double drawY = y - camera.getY();
        double drawWidth = frame.getWidth() * 2;
        double drawHeight = frame.getHeight() * 2;

        double barWidth = 80;
        double barHeight = 6;
        double barX = drawX + (drawWidth / 2) - (barWidth / 2);
        double barY = drawY - 15;
        
        
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(barX, barY, barWidth, barHeight);

        gc.setFill(Color.YELLOW);
        double totalHP = currentHealth + tempHealth;
        double hpRatio = Math.min(totalHP / GameConfig.PLAYER_MAX_HEALTH, 1.0);
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
    public void attack() {
        long now = System.currentTimeMillis();
        if (!attacking && now - lastAttackTime >= GameConfig.ATTACK_COOLDOWN) {
            attacking = true;
            currentAttackFrame = 0;
            lastAttackFrameTime = now;
            lastAttackTime = now;
        }
        
        SoundManager.playSEF("sword-sound-260274.mp3");
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

    @Override
    public void dash() {
        long now = System.currentTimeMillis();
        if (!dashing && now - lastDashTime >= GameConfig.DASH_COOLDOWN && currentMana >= GameConfig.DASH_MANA_COST) {
            dashing = true;
            dashStartTime = now;
            lastDashTime = now;
            currentMana -= GameConfig.DASH_MANA_COST;
        }
        
        SoundManager.playSEF("clean-fast-swooshaiff-14784.mp3");
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
        }
        
        SoundManager.playSEF("metal-crunch-263638.mp3");
        
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
    
    @Override
    public void defend() {
        long now = System.currentTimeMillis();
        if (!defending && now - lastDefendTime >= GameConfig.DEFEND_DURATION) {
            defending = true;
            currentDefendFrame = 0;
            lastDefendFrameTime = now;
            lastDefendTime = now;
        }
        
        if (now - tempHealthStartTime >= TEMP_HEAL_DURATION) {
        	tempHealth = TEMP_HEAL_AMOUNT;
        	tempHealthStartTime = now;
        	//System.out.println("BLOCK"+"+"+TEMP_HEAL_AMOUNT);
        }
        
        SoundManager.playSEF("metal-clang-284809.mp3");
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
    
    @Override
    public boolean isDead() { return dead; }
    
    @Override
    public Rectangle2D getAttackBox() {
        double attackWidth = 50;
        double attackHeight = 60;
        double offsetX = facingRight ? 40 : -40;

        return new Rectangle2D(x + offsetX, y, attackWidth, attackHeight);
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
                
                if (onGround) {
                	SoundManager.playSEF("st3-footstep-sfx-323056.mp3", 625);
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
    	if (tempHealth > 0) { //Block najaaaaa
    		int absorbed = Math.min(damage, tempHealth);
    		tempHealth -= absorbed;
    		damage -= absorbed;
    		SoundManager.playSEF("sword-clash-241729.mp3");
    	}
    	
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
    public boolean isAttacking() {
        return attacking;
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
    public int getCurrentHealth() { return currentHealth; }
    public int getMaxHealth() { return GameConfig.PLAYER_MAX_HEALTH; }
    public double getCurrentMana() { return currentMana; }
    public int getMaxMana() { return GameConfig.PLAYER_MAX_MANA; }
    
    
}
