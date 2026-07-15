package com.tedu.element;

public class EnemySmall extends EnemyPlane {
    private long lastMoveTime = 0;
    private static final long MOVE_DELAY = 50;

    public EnemySmall() {
        this.type = GameElement.ENEMY_SMALL;
        this.hp = 3;
        this.score = 100;
        this.speed = 3;
        this.shootInterval = 0;
    }

    @Override
    public void move() {
        long now = System.currentTimeMillis();
        if (now - lastMoveTime < MOVE_DELAY) {
            return;
        }
        lastMoveTime = now;
        y += speed;
        if (y > 700) {
            live = false;
        }
    }
}