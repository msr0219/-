package com.tedu.element;

public class EnemyMedium extends EnemyPlane {
    private int moveDir = 2;
    private long lastMoveTime = 0;
    private static final long MOVE_DELAY = 10;
    private static final long SWING_DELAY = 3000;
    private long lastSwingTime = 0;

    public EnemyMedium() {
        this.type = GameElement.ENEMY_MEDIUM;
        this.hp = 8;
        this.score = 300;
        this.speed = 3;
        this.shootInterval = 2000;
    }

    @Override
    public void move() {
        long now = System.currentTimeMillis();
        if (now - lastMoveTime < MOVE_DELAY) {
            return;
        }
        lastMoveTime = now;
        y += speed;
        
        if (now - lastSwingTime > SWING_DELAY) {
            moveDir *= -1;
            lastSwingTime = now;
        }
        x += moveDir * 1;

        if (x < 0) {
            x = 0;
        } if (x > 500 - width) {
            x = 500 - width;
        }
        if (y > 700) {
            live = false;
        }
    }
}
