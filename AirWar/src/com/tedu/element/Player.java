package com.tedu.element;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameLoad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashSet;

public class Player extends ElementObj {
    private int hp = 3;
    private int powerLevel = 1;
    private int bulletDamage = 1;
    private boolean hasShield = false;
    private int speed = 10;
    private long lastShootTime = 0;
    private int shootInterval = 200;
    private HashSet<Integer> keys = new HashSet<>();
    private ElementManager em = ElementManager.getManager();
    private ImageIcon shieldIcon;

    public Player() {
        this.type = GameElement.PLAYER;
    }

    @Override
    public void showElement(Graphics g) {
        if (icon != null) {
            g.drawImage(icon.getImage(), x, y, width / 2, height / 2, null);
        }
        if (hasShield && shieldIcon != null) {
            g.drawImage(shieldIcon.getImage(), x - 5, y - 5, shieldIcon.getIconWidth() / 2, shieldIcon.getIconHeight() / 2, null);
        }
    }

    @Override
    public void move() {
        int currentSpeed = keys.contains(KeyEvent.VK_SHIFT) ? speed / 2 : speed;
        
        if (keys.contains(KeyEvent.VK_UP)) {
            y -= currentSpeed;
        }
        if (keys.contains(KeyEvent.VK_DOWN)) {
            y += currentSpeed;
        }
        if (keys.contains(KeyEvent.VK_LEFT)) {
            x -= currentSpeed;
        }
        if (keys.contains(KeyEvent.VK_RIGHT)) {
            x += currentSpeed;
        }

        if (x < 0) x = 0;
        if (x > 500 - width / 2) x = 500 - width / 2;
        if (y < 0) y = 0;
        if (y > 700 - height / 2) y = 700 - height / 2;
    }

    @Override
    public void add() {
        long now = System.currentTimeMillis();
        boolean hasZ = keys.contains(KeyEvent.VK_Z);
        boolean canShoot = now - lastShootTime > shootInterval;
        if (hasZ && canShoot) {
            lastShootTime = now;
            shoot();
            System.out.println("Shooting!");
        }
    }

    private void shoot() {
        int bulletX = x + width / 4;
        int bulletY = y - 10;

        switch (powerLevel) {
            case 1:
                createBullet(bulletX, bulletY, 0);
                break;
            case 2:
                createBullet(bulletX - 15, bulletY, -2);
                createBullet(bulletX + 15, bulletY, 2);
                break;
            case 3:
                createBullet(bulletX, bulletY, 0);
                createBullet(bulletX - 20, bulletY, -3);
                createBullet(bulletX + 20, bulletY, 3);
                break;
            case 4:
                createBullet(bulletX - 25, bulletY, -4);
                createBullet(bulletX - 8, bulletY, -1);
                createBullet(bulletX + 8, bulletY, 1);
                createBullet(bulletX + 25, bulletY, 4);
                break;
            case 5:
                createBullet(bulletX, bulletY, 0);
                createBullet(bulletX - 30, bulletY, -5);
                createBullet(bulletX - 15, bulletY, -2);
                createBullet(bulletX + 15, bulletY, 2);
                createBullet(bulletX + 30, bulletY, 5);
                break;
        }
    }

    private void createBullet(int bx, int by, int dx) {
        PlayerBullet bullet = new PlayerBullet();
        bullet.setX(bx);
        bullet.setY(by);
        bullet.setDx(dx);
        bullet.setDamage(bulletDamage);
        bullet.setIcon(GameLoad.getLoad().getImage("player_bullet"));
        em.addElement(bullet, GameElement.PLAYER_BULLET);
    }

    @Override
    public void keyClick(boolean isPressed, int key) {
        if (isPressed) {
            keys.add(key);
            if (key == KeyEvent.VK_Z) {
                System.out.println("Z key pressed, keys size: " + keys.size());
            }
        } else {
            keys.remove(key);
        }
    }

    public void takeDamage() {
        if (hasShield) {
            hasShield = false;
            return;
        }
        hp--;
        if (hp <= 0) {
            hp = 0;
            live = false;
        }
    }

    public void addPower() {
        if (powerLevel < 5) powerLevel++;
    }

    public void addBulletDamage() {
        if (bulletDamage < 3) bulletDamage++;
    }

    public void addLife() {
        if (hp < 5) hp++;
    }

    public void addShield() {
        hasShield = true;
    }

    public void bomb() {
        em.clearEnemies();
    }

    public int getHp() { return hp; }
    public int getPowerLevel() { return powerLevel; }
    public int getBulletDamage() { return bulletDamage; }
    public boolean isHasShield() { return hasShield; }
}