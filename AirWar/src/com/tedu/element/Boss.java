package com.tedu.element;

public class Boss extends EnemyPlane {
    private int moveDir = 1;
    private int phase = 1;

    public Boss() {
        this.type = GameElement.BOSS;
        this.hp = 50;
        this.score = 5000;
        this.speed = 2;
        this.shootInterval = 500;
    }

    @Override
    public void move() {
        if (y < 50) {
            y += 2;
            return;
        }
        x += moveDir * 3;
        if (x <= 0 || x >= 500 - width) {
            moveDir *= -1;
        }
    }

    @Override
    protected void shoot() {
        switch (phase) {
            case 1:
                shootPhase1();
                break;
            case 2:
                shootPhase2();
                break;
            case 3:
                shootPhase3();
                break;
        }
    }

    private void shootPhase1() {
        EnemyBullet bullet1 = new EnemyBullet();
        bullet1.setX(x + width / 2 - 5);
        bullet1.setY(y + height);
        em.addElement(bullet1, GameElement.ENEMY_BULLET);
    }

    private void shootPhase2() {
        for (int i = -2; i <= 2; i++) {
            EnemyBullet bullet = new EnemyBullet();
            bullet.setX(x + width / 2 - 5 + i * 20);
            bullet.setY(y + height);
            bullet.setDx(i);
            em.addElement(bullet, GameElement.ENEMY_BULLET);
        }
    }

    private void shootPhase3() {
        for (int i = -3; i <= 3; i++) {
            EnemyBullet bullet = new EnemyBullet();
            bullet.setX(x + width / 2 - 5 + i * 15);
            bullet.setY(y + height);
            bullet.setDx(i * 2);
            em.addElement(bullet, GameElement.ENEMY_BULLET);
        }
    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        updatePhase();
    }

    private void updatePhase() {
        if (hp <= 15) {
            phase = 3;
        } else if (hp <= 30) {
            phase = 2;
        }
    }
}