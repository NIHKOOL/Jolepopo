package config;

public class GameConfig {

    // Screen configuration
    public static final int SCREEN_WIDTH = 1244;
    public static final int SCREEN_HEIGHT = 700;

    // Map boundery
    public static final int MAP_WIDTH = 3000;
    
    // Player movement and mechanics
    public static final double PLAYER_SPEED = 5.0;
    public static final double DASH_SPEED = 20.0;
    public static final long DASH_DURATION = 200;
    public static final long DASH_COOLDOWN = 1000;
    public static final int DASH_MANA_COST = 30;
    public static final double MANA_REGEN = 0.2;
    
    // Special abilities
    public static final int BIG_ARROW_MANA_COST = 100;
    public static final long SHOOTING_FRAME_INTERVAL = 50;

    // Player stats
    public static final int PLAYER_MAX_HEALTH = 100;
    public static final int PLAYER_MAX_MANA = 100;

    // Physics
    public static final double GRAVITY = 0.5;
    public static final double JUMP_STRENGTH = -15;

    // Combat timings
    public static final long ATTACK_COOLDOWN = 500;
    public static final long DEFEND_DURATION = 1000;
    public static final long ATTACK_FRAME_INTERVAL = 100;
    public static final long DEFEND_FRAME_INTERVAL = 150;

    // Monster stats
    public static final int MONSTER_MAX_HEALTH = 50;
    public static final double MONSTER_SPEED = 1.5;
    public static final long MONSTER_ATTACK_COOLDOWN = 1000;
    public static final int BOSS_MAX_HEALTH = 500;
    
    // Ground level configuration
    public static final int GROUND_LEVEL = 350; 

    private GameConfig() {}
    
}
