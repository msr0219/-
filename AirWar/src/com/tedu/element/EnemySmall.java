package com.tedu.element;

public class EnemySmall extends EnemyPlane {
    public EnemySmall() {
        this.type = GameElement.ENEMY_SMALL;
        this.hp = 1;
        this.score = 100;
        this.speed = 5;
        this.shootInterval = 0;
    }

    @Override
    public void move() {
        y += speed;
        if (y > 700) {
            live = false;
        }
    }
}