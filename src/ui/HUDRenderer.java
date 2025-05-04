package ui;

import entities.Character;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class HUDRenderer {
	
	private Character player;
	
	public HUDRenderer(Character player) {
		this.player = player;
	}
	
	public void setCharacter(Character c) {
		this.player = c;
	}
	
	public void renderHUD(GraphicsContext gc) {
        double x = 20;
        double y = 20;

        double healthWidth = 200;
        double healthHeight = 20;
        gc.setFill(Color.GRAY);
        gc.fillRect(x, y, healthWidth, healthHeight);
        gc.setFill(Color.RED);
        double healthPercent = (double) player.getCurrentHealth() / player.getMaxHealth();
        gc.fillRect(x, y, healthWidth * healthPercent, healthHeight);

        gc.setFill(Color.WHITE);
        gc.fillText(player.getCurrentHealth() + " / " + player.getMaxHealth(), x + 5, y + 14);

        double manaWidth = 300;
        double manaHeight = 20;
        double manaY = y + healthHeight + 5;
        gc.setFill(Color.BISQUE);
        gc.fillRect(x, manaY, manaWidth, manaHeight);
        gc.setFill(Color.LIGHTYELLOW);
        double manaPercent = player.getCurrentMana() / player.getMaxMana();
        gc.fillRect(x, manaY, manaWidth * manaPercent, manaHeight);

        gc.setFill(Color.BLACK);
        gc.fillText((int) player.getCurrentMana() + " / " + player.getMaxMana(), x + 5, manaY + 14);
    }
	
}
