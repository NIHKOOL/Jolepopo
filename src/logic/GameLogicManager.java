package logic;

import java.util.List;

import entities.Monster;
import entities.SamuraiArcher;
import entities.SamuraiMelee;
import entities.Character;
import javafx.geometry.Rectangle2D;
import utils.SoundManager;
import entities.projectiles.Arrow;
import entities.projectiles.BigArrow;

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
        // Update all monsters
        for (Monster m : monsters) {
            m.update();
        }

        // Melee attack detection
        if (player instanceof SamuraiMelee && player.isAttacking()) {
            Rectangle2D playerAtk = player.getAttackBox();
            for (Monster m : monsters) {
                if (playerAtk.intersects(m.getHitbox())) {
                    m.takeDamage(1);
                    SoundManager.playSEF("effects/hit-swing-sword-small-2-95566.mp3", 2, 100);
                }
            }
        }

        // Archer logic
        if (player instanceof SamuraiArcher archer) {
            List<Arrow> arrows = archer.getArrows();
            List<BigArrow> bigArrows = archer.getBigArrows();
            
            for (Arrow arrow : arrows) {
                Rectangle2D arrowBox = arrow.getHitbox();
                for (Monster m : monsters) {
                    if (arrowBox.intersects(m.getHitbox())) {
                        m.takeDamage(10);
                        arrow.deactive();
                        SoundManager.playSEF("effects/hit-swing-sword-small-2-95566.mp3", 2, 100);
                    }
                }
            }

            for (BigArrow ba : bigArrows) {
                Rectangle2D box = ba.getHitbox();
                for (Monster m : monsters) {
                    if (box.intersects(m.getHitbox())) {
                        m.takeDamage(50);
                        ba.deactive();
                        SoundManager.playSEF("effects/hit-swing-sword-small-2-95566.mp3", 2, 100);
                    }
                }
            }

            arrows.removeIf(a -> !a.isActive());
        }

        // Remove dead monsters
        monsters.removeIf(m -> m.isDead());

        // Player death check
        if (player.isDead()) {
            System.out.println(" ##-{ YOU DIED }-## ");
        }
    }
    
}
