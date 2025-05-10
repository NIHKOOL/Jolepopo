package config;

public class GameConfig {

    // Screen configuration
    public static final int SCREEN_WIDTH = 1244;
    public static final int SCREEN_HEIGHT = 700;

    // Map
    public static final int MAP_WIDTH = 5000;
    public static final int MAP_LOCK_WIDTH = 900;
    
    // Meteor
    public static final long METEOR_SPAWN_INTERVAL = 400;
    public static final double METEOR_SPEED = 8;
    public static final int MEMTEOR_DAMAGE = 5;
    
    // Player movement and mechanics
    public static final double PLAYER_SPEED = 5.0;
    public static final double DASH_SPEED = 20.0;
    public static final long DASH_DURATION = 200;
    public static final long DASH_COOLDOWN = 1000;
    public static final int DASH_MANA_COST = 30;
    public static double MANA_REGEN = 0.2;
    public static double PLAYER_DAMAGE_MULTIPLIER = 1.0;
    
    // Special abilities
    public static final int BIG_ARROW_MANA_COST = 60;
    public static final long SHOOTING_FRAME_INTERVAL = 50;

    // Player stats
    public static final int PLAYER_MAX_HEALTH = 100;
    public static final int PLAYER_MAX_MANA = 100;
    public static final int PLAYER_SWORD_DAMAGE = 3;
    public static final int PLAYER_ARROW_DAMAGE = 10;
    public static final int PLAYER_BIGARROW_DAMAGE = 125;

    // Physics
    public static final double GRAVITY = 0.5;
    public static final double JUMP_STRENGTH = -15;

    // Combat timings
    public static final long ATTACK_COOLDOWN = 400;
    public static final long DEFEND_DURATION = 1500;
    public static final long ATTACK_FRAME_INTERVAL = 100;
    public static final long DEFEND_FRAME_INTERVAL = 150;

    // Monster stats
    public static final int MONSTER_MAX_HEALTH = 150;
    public static final double MONSTER_SPEED = 1.5;
    public static final long MONSTER_ATTACK_COOLDOWN = 1000;
    public static final int BOSS_MAX_HEALTH = 1000;
    public static final int SKELETON_MAX_HEALTH = 10;
    
    // Ground level configuration
    public static final int GROUND_LEVEL = 350; 

    private GameConfig() {}
    
}
