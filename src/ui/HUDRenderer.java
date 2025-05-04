package ui;

import entities.Character;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import utils.Assets;

public class HUDRenderer {
	
	private Character player;
	private final Image hudFrame;
	private final Image SamuraiFace;
	
	public HUDRenderer(Character player) {
		this.player = player;
		this.hudFrame = Assets.loadImage("Hbar_Mbar.png");
		this.SamuraiFace = Assets.loadImage("faceSumuraimelee.png"); 
	}
	
	public void setCharacter(Character c) {
		this.player = c;
	}
	
	public void renderHUD(GraphicsContext gc) {
		double hudX = 20;
		double hudY = 20;
		
		double healthPercent = (double) player.getCurrentHealth() / player.getMaxHealth();
		double manaPercent = (double) player.getCurrentMana() / player.getMaxMana();
		
		gc.setFill(Color.BLACK);
		gc.fillRect(hudX + 30, hudY + 25, 80, 60); //Very Very First 
		
		gc.setFill(Color.LIGHTGRAY);
		gc.fillRect(hudX + 121, hudY + 30, 215, 16);
		gc.setFill(Color.RED);
		gc.fillRect(hudX + 121, hudY + 30, 215 * healthPercent, 16);
		
		gc.setFill(Color.GRAY);
		gc.fillRect(hudX + 121, hudY + 63, 215, 16);
		gc.setFill(Color.AQUA);
		gc.fillRect(hudX + 121, hudY + 63, 215 * manaPercent, 16);
		
		gc.drawImage(SamuraiFace, hudX + 40, hudY + 20);
		gc.drawImage(hudFrame, hudX, hudY);
		
		gc.setFill(Color.BLACK);
		gc.fillText(player.getCurrentHealth() + "/" + player.getMaxHealth(), hudX + 126, hudY + 42);
		gc.setFill(Color.BLACK);
		gc.fillText((int) player.getCurrentMana() + "/" + player.getMaxHealth(), hudX + 126, hudY + 75);
    }
	
}
