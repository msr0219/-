package com.tedu.element;

public class EnemyMedium extends EnemyPlane {
    private int moveDir = 1;
    private int moveCounter = 0;

    public EnemyMedium() {
        this.type = GameElement.ENEMY_MEDIUM;
        this.hp = 3;
        this.score = 300;
        this.speed = 5;
        this.shootInterval = 2000;
    }

    @Override
    public void move() {
        y += speed;
        moveCounter++;
        if (moveCounter > 30) {
            moveDir *= -1;
            moveCounter = 0;
        }
        x += moveDir * 2;

        if (x < 0) x = 0;
        if (x > 500 - width) x = 500 - width;
        if (y > 700) {
            live = false;
        }
    }
}