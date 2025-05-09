package camera;

import config.GameConfig;
import entities.Character;

public class Camera {
    private double x, y;
    private final double screenWidth;

    public Camera(double screenWidth, double screenHeight) {
        this.screenWidth = screenWidth;
    }

    public void update(Character player, boolean isScrollable) {
        if (isScrollable) {
        	x = Math.max(0, Math.min(player.getX() - screenWidth / 2.0, GameConfig.MAP_WIDTH - screenWidth));
        	if (x < 0) x = 0;
        } else {
        	x = 0;
        }     
        y = 0;
    }

    public double getX() { return x;}
    public double getY() { return y;}
    
}
