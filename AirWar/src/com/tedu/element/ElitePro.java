package com.tedu.element;

import java.util.Random;

public class ElitePro extends EnemyPlane {
    private Random random = new Random();
    private int targetX;
    private int changeTimer = 0;

    public ElitePro() {
        this.type = GameElement.ELITE_PRO;
        this.hp = 10;
        this.score = 1000;
        this.speed = 4;
        this.shootInterval = 1000;
        this.targetX = random.nextInt(400) + 50;
    }

    @Override
    public void move() {
        y += speed;
        changeTimer++;
        
        if (changeTimer > 40) {
            targetX = random.nextInt(400) + 50;
            changeTimer = 0;
        }

        if (x < targetX) x += 3;
        else if (x > targetX) x -= 3;

        if (x < 0) x = 0;
        if (x > 500 - width) x = 500 - width;
        if (y > 700) {
            live = false;
        }
    }

    @Override
    protected void shoot() {
        EnemyBullet bullet1 = new EnemyBullet();
        bullet1.setX(x + width / 2 - 5);
        bullet1.setY(y + height);
        em.addElement(bullet1, GameElement.ENEMY_BULLET);

        EnemyBullet bullet2 = new EnemyBullet();
        bullet2.setX(x);
        bullet2.setY(y + height);
        bullet2.setDx(-1);
        em.addElement(bullet2, GameElement.ENEMY_BULLET);

        EnemyBullet bullet3 = new EnemyBullet();
        bullet3.setX(x + width - 10);
        bullet3.setY(y + height);
        bullet3.setDx(1);
        em.addElement(bullet3, GameElement.ENEMY_BULLET);
    }
}