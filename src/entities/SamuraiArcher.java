package entities;

import java.util.ArrayList;
import java.util.List;

import camera.Camera;
import config.GameConfig;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import utils.Assets;
import utils.SoundManager;

public class SamuraiArcher extends Character{
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
    private int currentDefendFrame = 0;
    private long lastDefendFrameTime = 0;

    private int currentFrame = 0;
    private long lastFrameTime = 0;
    private int jumpFrame = 0;
    private long lastJumpFrameTime = 0;

    private Image[] walkFrames, dashFrames, jumpFrames, attackFrames, defendFrames;

    private boolean dead = false;
    private List<BigArrow> bigArrows = new ArrayList<>();
    private List<Arrow> arrows = new ArrayList<>();
    
    private long lastBigArrowTime = 0;
    private static final long BIG_ARROW_COOLDOWN = 1500;
    
    public SamuraiArcher(double x, double y) {
        this.x = x;
        this.y = y;

        walkFrames = new Image[] {
        	Assets.loadImage("arWalk_1.png"), 
        	Assets.loadImage("arWalk_2.png"), 
            Assets.loadImage("arWalk_3.png"), 
            Assets.loadImage("arWalk_4.png"), 
            Assets.loadImage("arWalk_5.png"), 
            Assets.loadImage("arWalk_6.png"), 
            Assets.loadImage("arWalk_7.png"), 
            Assets.loadImage("arWalk_8.png")
        };

        dashFrames = new Image[] {
            Assets.loadImage("arDash_1.png"),
            Assets.loadImage("arDash_2.png"), 
            Assets.loadImage("arDash_3.png"), 
            Assets.loadImage("arDash_4.png"), 
        
        };

        jumpFrames = new Image[] {
            Assets.loadImage("arJump_1.png"),
            Assets.loadImage("arJump_2.png"),
            Assets.loadImage("arJump_3.png"),
            Assets.loadImage("arJump_4.png"),
            Assets.loadImage("arJump_5.png"),
            Assets.loadImage("arJump_6.png"),
            Assets.loadImage("arJump_7.png")
            
        };

        attackFrames = new Image[] {
            Assets.loadImage("arShoot_0.png"), 
            Assets.loadImage("arShoot_1.png"), 
            Assets.loadImage("arShoot_2.png"), 
            Assets.loadImage("arShoot_3.png"), 
            Assets.loadImage("arShoot_4.png"), 
            Assets.loadImage("arShoot_5.png"), 
            Assets.loadImage("arShoot_6.png"), 
            Assets.loadImage("arShoot_7.png"), 
            Assets.loadImage("arShoot_8.png"), 
            Assets.loadImage("arShoot_9.png"), 
            Assets.loadImage("arShoot_10.png"), 
            Assets.loadImage("arShoot_11.png"), 
            Assets.loadImage("arShoot_12.png"), 
            Assets.loadImage("arShoot_13.png"),         
        };

        defendFrames = new Image[] {
                Assets.loadImage("arShoot_5.png"), 
                Assets.loadImage("arShoot_6.png"), 
                Assets.loadImage("arShoot_7.png"), 
                Assets.loadImage("arShoot_8.png"), 
                Assets.loadImage("arShoot_9.png"), 
                Assets.loadImage("arShoot_10.png"), 
                Assets.loadImage("arShoot_11.png"), 
                Assets.loadImage("arShoot_12.png"), 
                Assets.loadImage("arShoot_13.png"), 
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
        
        for (Arrow arrow : arrows) {
        	arrow.update();
        }
        arrows.removeIf(a -> !a.isActive());
        
        for (BigArrow ba : bigArrows) {
            ba.update();
        }
        bigArrows.removeIf(b -> !b.isActive());
    }
    
    @Override
    public void render(GraphicsContext gc, Camera camera) {
        Image frame = getCurrentFrame();

        double baseHeight = walkFrames[0].getHeight();
        double drawOffsetY = (frame.getHeight() - baseHeight) * 2;
        double drawOffsetX = 50;
        double drawX = x - camera.getX() - drawOffsetX;
        double drawY = y - camera.getY() - drawOffsetY;
        double drawWidth = frame.getWidth() * 2;
        double drawHeight = frame.getHeight() * 2;

        double barWidth = 80;
        double barHeight = 6;
        double barX = drawX + (drawWidth / 2) - (barWidth / 2);
        double barY = drawY - 15;
        
        
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(barX, barY, barWidth, barHeight);

        gc.setFill(Color.YELLOW);
        double totalHP = currentHealth ;
        double hpRatio = Math.min(totalHP / GameConfig.PLAYER_MAX_HEALTH, 1.0);
        gc.fillRect(barX, barY, barWidth * hpRatio, barHeight);
        
        gc.setFill(Color.RED);
        gc.fillRect(barX, barY, barWidth * currentHealth / GameConfig.PLAYER_MAX_HEALTH , barHeight);

        if (facingRight) {
            gc.drawImage(frame, drawX, drawY, drawWidth, drawHeight);
        } else {
            gc.drawImage(frame, 0, 0, frame.getWidth(), frame.getHeight(),
                         drawX + drawWidth, drawY, -drawWidth, drawHeight);
        }
        
        for (Arrow arrow : arrows) {
            arrow.render(gc, camera);
        }
        
        for (BigArrow ba : bigArrows) {
            ba.render(gc, camera);
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
            
            double arrowX = facingRight ? x + 40 : x - 40;
            arrows.add(new Arrow(arrowX, y - 20, facingRight));
            
            currentMana -= 20;
            if (currentMana < 0) currentMana = 0;
            
            SoundManager.playSEF("sword-sound-260274.mp3", 1);
            
            
            
        }
        
    }
    
    private void updateAttack(long now) {
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
        }
        
        SoundManager.playSEF("clean-fast-swooshaiff-14784.mp3", 1);
    }
    
    private void updateDash(long now) {
        if (dashing) {
            double dashDirection = facingRight ? 1 : -1;
            x -= dashDirection * GameConfig.DASH_SPEED * 0.5 ;

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
        
        SoundManager.playSEF("metal-crunch-263638.mp3", 1);
        
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
        } else {
            onGround = false;
            
        }
    }
    
    @Override
    public void defend() {
    	long now = System.currentTimeMillis();
    	
    	new BigArrow(x, y, facingRight);

    	if (now - lastBigArrowTime >= BIG_ARROW_COOLDOWN && currentMana >= GameConfig.BIG_ARROW_MANA_COST) {
        	double arrowX = facingRight ? x + 40 : x - 40;
        	bigArrows.add(new BigArrow(arrowX, y - 20, facingRight));
        	currentMana -= GameConfig.BIG_ARROW_MANA_COST;
        	if (currentMana < 0) currentMana = 0;
        	SoundManager.playSEF("metal-clang-284809.mp3", 1);
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
                x -= GameConfig.PLAYER_SPEED - 1;
                facingRight = false;
            }
            if (right) {
                x += GameConfig.PLAYER_SPEED - 1;
                facingRight = true;
   
            }

            if (now - lastFrameTime > 150 && (left || right)) {
                currentFrame = (currentFrame + 1) % walkFrames.length;
                lastFrameTime = now;
                
                if (onGround) {
                	SoundManager.playSEF("st3-footstep-sfx-323056.mp3", 1, 625);
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
    public boolean isAttacking() {
        return attacking;
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
    public int getCurrentHealth() { return (int) (currentHealth * 0.40); }
    public int getMaxHealth() { return (int) (GameConfig.PLAYER_MAX_HEALTH * 0.40); }
    public double getCurrentMana() { return currentMana * 2; }
    public int getMaxMana() { return GameConfig.PLAYER_MAX_MANA * 2; }
    public List<Arrow> getArrows() { return arrows;}
    public List<BigArrow> getBigArrows() { return bigArrows;}
    
}
