package com.tedu.element;

import com.tedu.manager.GameLoad;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class BeliarBoss extends Boss {
    private Random random = new Random();
    private long waveTimer = 0;
    private boolean isWaveMode = false;
    private static final int BOSS_WIDTH = 100;
    private static final int BOSS_HEIGHT = 100;

    public BeliarBoss() {
        this.type = GameElement.BOSS;
        this.hp = 1600;
        this.score = 10000;
        this.speed = 2;
        this.shootInterval = 300;
        this.maxHp = 1600;
        this.width = BOSS_WIDTH;
        this.height = BOSS_HEIGHT;
    }

    @Override
    public void showElement(Graphics g) {
        if (icon != null) {
            g.drawImage(icon.getImage(), x, y, BOSS_WIDTH, BOSS_HEIGHT, null);
        }
    }

    @Override
    public void setIcon(ImageIcon icon) {
        this.icon = icon;
        this.width = BOSS_WIDTH;
        this.height = BOSS_HEIGHT;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
        this.hp = maxHp;
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
                shootPhase3WaveParticle();
                break;
        }
    }

    private void shootPhase1() {
        for (int i = -1; i <= 1; i++) {
            EnemyBullet bullet = new EnemyBullet();
            bullet.setX(x + width / 2 - 5 + i * 30);
            bullet.setY(y + height);
            bullet.setDx(i);
            bullet.setIcon(GameLoad.getLoad().getImage("enemy_bullet"));
            em.addElement(bullet, GameElement.ENEMY_BULLET);
        }
    }

    private void shootPhase2() {
        for (int i = -3; i <= 3; i++) {
            EnemyBullet bullet = new EnemyBullet();
            bullet.setX(x + width / 2 - 5 + i * 18);
            bullet.setY(y + height);
            bullet.setDx(i * 2);
            bullet.setIcon(GameLoad.getLoad().getImage("enemy_bullet"));
            em.addElement(bullet, GameElement.ENEMY_BULLET);
        }
    }

    private void shootPhase3WaveParticle() {
        long now = System.currentTimeMillis();
        if (now - waveTimer > 1500) {
            waveTimer = now;
            isWaveMode = !isWaveMode;
        }

        if (isWaveMode) {
            shootWavePattern();
        } else {
            shootParticlePattern();
        }
    }

    private void shootWavePattern() {
        int bulletCount = 8;
        for (int i = 0; i < bulletCount; i++) {
            EnemyBullet bullet = new EnemyBullet();
            bullet.setX(x + width / 2 - 5 + (i - bulletCount / 2) * 25);
            bullet.setY(y + height);
            bullet.setDx((i - bulletCount / 2) * 2);
            bullet.setDy(3);
            bullet.setWaveAmplitude(15);
            bullet.setWaveFrequency(0.05);
            bullet.setIcon(GameLoad.getLoad().getImage("enemy_bullet"));
            em.addElement(bullet, GameElement.ENEMY_BULLET);
        }
    }

    private void shootParticlePattern() {
        int bulletCount = 12;
        for (int i = 0; i < bulletCount; i++) {
            EnemyBullet bullet = new EnemyBullet();
            double angle = (Math.PI / 2) + (i - bulletCount / 2) * (Math.PI / (bulletCount + 1));
            bullet.setX(x + width / 2 - 5);
            bullet.setY(y + height);
            bullet.setDx((int) (Math.cos(angle) * 4));
            bullet.setDy((int) (Math.sin(angle) * 4));
            bullet.setIcon(GameLoad.getLoad().getImage("enemy_bullet"));
            em.addElement(bullet, GameElement.ENEMY_BULLET);
        }
    }
}