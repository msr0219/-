package com.tedu.element;

import com.tedu.manager.GameLoad;

import java.util.Random;

public class ElitePro extends EnemyPlane {
    private Random random = new Random();
    private int targetX;
    private int changeTimer = 0;
    private boolean stopped = false;
    private static final int STOP_Y = 50;

    public ElitePro() {
        this.type = GameElement.ELITE_PRO;
        this.hp = 30;
        this.score = 1000;
        this.speed = 5;
        this.shootInterval = 400;
        this.targetX = random.nextInt(400) + 50;
    }

    @Override
    public void move() {
        if (!stopped) {
            y += speed;
            if (y >= STOP_Y) {
                y = STOP_Y;
                stopped = true;
            }
        }

        changeTimer++;
        
        if (changeTimer > 20) {
            targetX = random.nextInt(400) + 50;
            changeTimer = 0;
        }

        if (x < targetX) x += 5;
        else if (x > targetX) x -= 5;

        if (x < 0) x = 0;
        if (x > 500 - width) x = 500 - width;
        if (y > 700) {
            live = false;
        }
    }

    @Override
    protected void shoot() {
        EnemyBullet bullet1 = new EnemyBullet();
        bullet1.setX(x + width / 2);
        bullet1.setY(y + height);
        bullet1.setIcon(GameLoad.getLoad().getImage("enemy_bullet"));
        em.addElement(bullet1, GameElement.ENEMY_BULLET);

        EnemyBullet bullet2 = new EnemyBullet();
        bullet2.setX(x);
        bullet2.setY(y + height);
        bullet2.setDx(-1);
        bullet2.setIcon(GameLoad.getLoad().getImage("enemy_bullet"));
        em.addElement(bullet2, GameElement.ENEMY_BULLET);

        EnemyBullet bullet3 = new EnemyBullet();
        bullet3.setX(x + width - 10);
        bullet3.setY(y + height);
        bullet3.setDx(1);
        bullet3.setIcon(GameLoad.getLoad().getImage("enemy_bullet"));
        em.addElement(bullet3, GameElement.ENEMY_BULLET);
    }
}