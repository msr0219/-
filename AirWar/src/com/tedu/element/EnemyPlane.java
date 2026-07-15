package com.tedu.element;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameLoad;

import javax.swing.*;
import java.awt.*;

public abstract class EnemyPlane extends ElementObj {
    protected int hp;
    protected int score;
    protected int speed;
    protected int shootInterval;
    protected long lastShootTime;
    protected ElementManager em = ElementManager.getManager();
    protected boolean isFrozen = false;
    protected long freezeEndTime = 0;

    @Override
    public void showElement(Graphics g) {
        if (icon != null) {
            icon.paintIcon(null, g, x, y);
        }
    }

    @Override
    public void model(long gameTime) {
        if (isFrozen && System.currentTimeMillis() < freezeEndTime) {
            return;
        } else if (isFrozen) {
            isFrozen = false;
        }
        updateImage();

        move();
        add();
    }

    @Override
    public void add() {
        if (shootInterval > 0) {
            long now = System.currentTimeMillis();
            if (now - lastShootTime > shootInterval) {
                lastShootTime = now;
                shoot();
            }
        }
    }

    protected void shoot() {
        EnemyBullet bullet = new EnemyBullet();
        bullet.setX(x + width / 2);
        bullet.setY(y + height);
        bullet.setIcon(GameLoad.getLoad().getImage("enemy_bullet"));
        em.addElement(bullet, GameElement.ENEMY_BULLET);
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            hp = 0;
            live = false;
            createExplosion();
            em.addScore(score);
        }
    }

    protected void createExplosion() {
        Explosion explosion = new Explosion();
        explosion.setX(x + width / 2);
        explosion.setY(y + height / 2);
        explosion.setRadius(Math.max(width, height) / 2);
        em.addElement(explosion, GameElement.EXPLOSION);
    }

    public void freeze(long duration) {
        isFrozen = true;
        freezeEndTime = System.currentTimeMillis() + duration;
    }

    public int getHp() { return hp; }
    public int getScore() { return score; }

    public void applyDifficulty(String gameMode) {
        switch (gameMode) {
            case "EASY":
                hp = (int) (hp * 0.5);
                shootInterval = (int) (shootInterval * 2);
                break;
            case "HARD":
                hp = (int) (hp * 2);
                shootInterval = (int) (shootInterval * 0.5);
                break;
        }
        if (hp < 1) hp = 1;
        if (shootInterval < 0) shootInterval = 0;
    }

    public int getMaxHp() {
        return hp;
    }
}