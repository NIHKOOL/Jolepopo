package logic;

import java.util.List;
import java.util.function.Supplier;

import config.GameConfig;
import entities.Monster;
import entities.SamuraiArcher;
import entities.SamuraiMelee;
import entities.Character;
import entities.GorgonBoss;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.stage.Stage;
import utils.SoundManager;
import entities.projectiles.Arrow;
import entities.projectiles.BigArrow;
import gui.EndingPage;
import gui.GameOverPage;

public class GameLogicManager {
	private Character player;
	private List<Monster> monsters;
	private Stage stage;
	private boolean gameOverShown = false;
	private Supplier<Integer> mapIndexSupplier;

	public GameLogicManager(Character player, List<Monster> monsters, Stage stage) {
		this.player = player;
		this.monsters = monsters;
		this.stage = stage;
	}

	public void setPlayer(Character c) {
		this.player = c;
	}
	
	public void setMapIndexSupplier(Supplier<Integer> supplier) {
		this.mapIndexSupplier = supplier;
	}

	// เรียกใช้เมื่อจำเป็น
	private int getCurrentMapIndex() {
		return mapIndexSupplier != null ? mapIndexSupplier.get() : -1;
	}

	public void updateLogic() {
		// Update all monsters
		int mapIndex = getCurrentMapIndex();
		for (Monster m : monsters) {
			m.update();
		}

		// Melee attack detection
		if (player instanceof SamuraiMelee && player.isAttacking()) {
			Rectangle2D playerAtk = player.getAttackBox();
			for (Monster m : monsters) {
				if (playerAtk.intersects(m.getHitbox())) {
					m.takeDamage(GameConfig.PLAYER_SWORD_DAMAGE);
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
						m.takeDamage(GameConfig.PLAYER_ARROW_DAMAGE);
						arrow.deactive();
						SoundManager.playSEF("effects/hit-swing-sword-small-2-95566.mp3", 2, 100);
						SoundManager.getActiveEffects().add(SoundManager.getBgmPlayer());
					}
				}
			}

			for (BigArrow ba : bigArrows) {
				Rectangle2D box = ba.getHitbox();
				for (Monster m : monsters) {
					if (box.intersects(m.getHitbox())) {
						m.takeDamage(GameConfig.PLAYER_BIGARROW_DAMAGE);
						ba.deactive();
						SoundManager.playSEF("effects/hit-swing-sword-small-2-95566.mp3", 2, 100);
						SoundManager.getActiveEffects().add(SoundManager.getBgmPlayer());
					}
				}
			}

			arrows.removeIf(a -> !a.isActive());
		}

		// Remove dead monsters
		monsters.removeIf(m -> {
			if (m.isDead()) {
				if (m instanceof GorgonBoss) {
					player.setWin(true);
					SoundManager.playSEFOnce("effects/monster-warrior-roar-195877.mp3", 0.5, () -> {
						EndingPage endingscene = new EndingPage(this.stage);
						stage.setScene(endingscene.getScene());
					});
				}
				return true;
			}
			return false;
		});

		// Player death check
		if (player.isDead() && !gameOverShown) {
			Platform.runLater(() -> {
				SoundManager.stopAllSounds();
				
				GameOverPage gameOverOverlay = new GameOverPage(stage,mapIndex);
				stage.setScene(gameOverOverlay.getScene());
				gameOverShown = true;
			});
		}
	}

	public List<Monster> getMonsters() {
		return monsters;
	}

}
