package logic;

import java.util.List;

import entities.Monster;
import entities.Character;
import javafx.geometry.Rectangle2D;
import utils.SoundManager;

public class GameLogicManager {
	private Character player;
	private List<Monster> monsters;
	
	public GameLogicManager(Character player, List<Monster> monsters) {
		this.player = player;
		this.monsters = monsters;
		
	}
	
	public void setPlayer(Character c) {
		this.player = c;
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
                    SoundManager.playSEF("hit-swing-sword-small-2-95566.mp3", 100);
                }
            }
        }
        
        monsters.removeIf(m -> m.isDead());
        
        if (player.isDead()) {
        	System.out.println(" ##-{ YOU DIED }-## ");
        }
        
	}
}
