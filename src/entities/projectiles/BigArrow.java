package entities.projectiles;

import config.GameConfig;
import interfaces.Renderable;
import interfaces.Updatable;
import utils.Assets;

public class BigArrow extends Arrow implements Renderable, Updatable {

	public BigArrow(double x, double y, boolean facingRight) {
		super(x, y, facingRight);
		this.speed = GameConfig.PLAYER_BIGARROW_SPEED;
		this.arrowImage = Assets.loadImage("samuraiArcher/BigArrow.png");
	}

	@Override
	public int getDamage() {
		return GameConfig.PLAYER_BIGARROW_DAMAGE;
	}
}
