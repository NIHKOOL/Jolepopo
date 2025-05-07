package entities.projectiles;

import utils.Assets;

public class BigArrow extends Arrow {

    public BigArrow(double x, double y, boolean facingRight) {
        super(x, y, facingRight);
        this.speed = 20;
        this.arrowImage = Assets.loadImage("samuraiArcher/BigArrow.png");
    }

    @Override
    public int getDamage() {
        return 50;
    }
}  
