package logic;

import java.util.List;

import entities.Monster;
import entities.Player;
import javafx.geometry.Rectangle2D;

public class GameLogicManager {
	private Player player;
	private List<Monster> monsters;
	
	public GameLogicManager(Player player, List<Monster> monsters) {
		this.player = player;
		this.monsters = monsters;
		
	}

	public void updateLogic() {
		
		for (Monster m : monsters) {
            m.update();
        }
        
        if (player.isAttacking()) {
            Rectangle2D playerAtk = player.getAttackBox();
            
            for (Monster m : monsters) {
                if (playerAtk.intersects(m.getHitbox())) {
                    m.takeDamage(1);  
                }
            }
        }
        
        monsters.removeIf(m -> m.isDead());
        
	}
}
