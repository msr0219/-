package com.tedu.element;

public class EnemyLarge extends EnemyPlane {
    private int moveDir = 1;

    public EnemyLarge() {
        this.type = GameElement.ENEMY_LARGE;
        this.hp = 6;
        this.score = 600;
        this.speed = 3;
        this.shootInterval = 1500;
    }

    @Override
    public void move() {
        y += speed;
        x += moveDir * 2;

        if (x <= 0 || x >= 500 - width) {
            moveDir *= -1;
        }
        if (y > 700) {
            live = false;
        }
    }
}