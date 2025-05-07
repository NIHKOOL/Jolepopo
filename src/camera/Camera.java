package camera;

import entities.Character;

public class Camera {
    private double x, y;
    private final double screenWidth;
    
    public Camera(double screenWidth, double screenHeight) {
        this.screenWidth = screenWidth;
    }

    public void update(Character player) {
        x = player.getX() - screenWidth / 2.0;
        y = 0;

        if (x < 0) x = 0;
        if (y < 0) y = 0; 
        
    }

    public double getX() { return x; }
    public double getY() { return y; }
}
