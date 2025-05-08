package ui;

import entities.Character;
import entities.Monster;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import utils.Assets;

public class HUDRenderer {
    private Character player;
    private Monster boss;
    private final Image hudFrame = Assets.loadImage("Hbar_Mbar.png");
    private final Image samuraiFace = Assets.loadImage("faceSumuraimelee.png");
    

    public HUDRenderer(Character player) {
        this.player = player;
    }

    public void setCharacter(Character c) {
        this.player = c;
    }
    
    public void setBoss (Monster boss) {
    	this.boss = boss;
    }

    public void renderHUD(GraphicsContext gc) {
        double hudX = 20, hudY = 20;
        double healthPercent = (double) player.getCurrentHealth() / player.getMaxHealth();
        double manaPercent = (double) player.getCurrentMana() / player.getMaxMana();

        // Background fill
        gc.setFill(Color.BLACK);
        gc.fillRect(hudX + 30, hudY + 25, 80, 60);

        // Health bar
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(hudX + 121, hudY + 30, 215, 16);
        gc.setFill(Color.RED);
        gc.fillRect(hudX + 121, hudY + 30, 215 * healthPercent, 16);

        // Mana bar
        gc.setFill(Color.GRAY);
        gc.fillRect(hudX + 121, hudY + 63, 215, 16);
        gc.setFill(Color.AQUA);
        gc.fillRect(hudX + 121, hudY + 63, 215 * manaPercent, 16);

        // HUD graphics
        gc.drawImage(samuraiFace, hudX + 40, hudY + 20);
        gc.drawImage(hudFrame, hudX, hudY);

        // Text info
        gc.setFill(Color.BLACK);
        gc.fillText(player.getCurrentHealth() + "/" + player.getMaxHealth(), hudX + 126, hudY + 42);
        gc.fillText((int) player.getCurrentMana() + "/" + player.getMaxMana(), hudX + 126, hudY + 75);
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
    }
}
