package camera;

import entities.Player;

public class Camera {
    private double x, y;
    private final double screenWidth;
    private final double screenHeight;

    public Camera(double screenWidth, double screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void update(Player player) {
        x = player.getX() - screenWidth / 2.0;
        y = 0;

        if (x < 0) x = 0;
        if (y < 0) y = 0; 
    }

    public double getX() { return x; }
    public double getY() { return y; }
}
