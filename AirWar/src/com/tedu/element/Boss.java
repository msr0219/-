package com.tedu.element;

import com.tedu.manager.GameLoad;

public class Boss extends EnemyPlane {
    private int moveDir = 1;
    protected int phase = 1;
    private long lastMoveTime = 0;
    private static final long MOVE_DELAY = 100;
    protected int maxHp = 1600;

    public Boss() {
        this.type = GameElement.BOSS;
        this.hp = 100;
        this.score = 5000;
        this.speed = 2;
        this.shootInterval = 500;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
        this.hp = maxHp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    @Override
    public void move() {
        long now = System.currentTimeMillis();
        if (now - lastMoveTime < MOVE_DELAY) {
            return;
        }
        lastMoveTime = now;
        if (y < 50) {
            y += 1;
            return;
        }
        x += moveDir * 1;
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
        bullet1.setIcon(GameLoad.getLoad().getImage("enemy_bullet"));
        em.addElement(bullet1, GameElement.ENEMY_BULLET);
    }

    private void shootPhase2() {
        for (int i = -2; i <= 2; i++) {
            EnemyBullet bullet = new EnemyBullet();
            bullet.setX(x + width / 2 - 5 + i * 20);
            bullet.setY(y + height);
            bullet.setDx(i);
            bullet.setIcon(GameLoad.getLoad().getImage("enemy_bullet"));
            em.addElement(bullet, GameElement.ENEMY_BULLET);
        }
    }

    private void shootPhase3() {
        for (int i = -3; i <= 3; i++) {
            EnemyBullet bullet = new EnemyBullet();
            bullet.setX(x + width / 2 - 5 + i * 15);
            bullet.setY(y + height);
            bullet.setDx(i * 2);
            bullet.setIcon(GameLoad.getLoad().getImage("enemy_bullet"));
            em.addElement(bullet, GameElement.ENEMY_BULLET);
        }
    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        updatePhase();
    }

    private void updatePhase() {
        if (hp <= maxHp * 0.3) {
            phase = 3;
        } else if (hp <= maxHp * 0.6) {
            phase = 2;
        }
    }

    @Override
    public void applyDifficulty(String gameMode) {
        switch (gameMode) {
            case "EASY":
                maxHp = (int) (maxHp * 0.5);
                shootInterval = (int) (shootInterval * 2);
                break;
            case "HARD":
                maxHp = (int) (maxHp * 2);
                shootInterval = (int) (shootInterval * 0.5);
                break;
        }
        this.hp = maxHp;
        if (shootInterval < 0) shootInterval = 0;
    }
}
