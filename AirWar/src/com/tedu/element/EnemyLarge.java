package com.tedu.element;

import com.tedu.manager.GameLoad;

public class EnemyLarge extends EnemyPlane {
    private int moveDir = 2;
    private long lastMoveTime = 0;
    private static final long MOVE_DELAY = 10;

    public EnemyLarge() {
        this.type = GameElement.ENEMY_LARGE;
        this.hp = 20;
        this.score = 600;
        this.speed = 2;
        this.shootInterval = 1500;
    }

    @Override
    public void move() {
        long now = System.currentTimeMillis();
        if (now - lastMoveTime < MOVE_DELAY) {
            return;
        }
        lastMoveTime = now;
        y += speed;
        x += moveDir * 1;

        if (x <= 0 || x >= 500 - width) {
            moveDir *= -1;
        }
        if (y > 700) {
            live = false;
        }
    }

    @Override
    protected void shoot() {
        EnemyBullet bullet1 = new EnemyBullet();
        bullet1.setX(x + width / 3);
        bullet1.setY(y + height);
        bullet1.setIcon(GameLoad.getLoad().getImage("enemy_bullet"));
        em.addElement(bullet1, GameElement.ENEMY_BULLET);

        EnemyBullet bullet2 = new EnemyBullet();
        bullet2.setX(x + width * 2 / 3);
        bullet2.setY(y + height);
        bullet2.setIcon(GameLoad.getLoad().getImage("enemy_bullet"));
        em.addElement(bullet2, GameElement.ENEMY_BULLET);
    }
}
