package com.tedu.element;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameLoad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashSet;

public abstract class Player extends ElementObj {
    protected int hp = 3;
    protected int powerLevel = 1;
    protected int bulletDamage = 1;
    protected int speed = 10;
    protected long lastShootTime = 0;
    protected int shootInterval = 200;
    protected HashSet<Integer> keys = new HashSet<>();
    protected ElementManager em = ElementManager.getManager();
    protected static final int HITBOX_SIZE = 5;

    public Player() {
        this.type = GameElement.PLAYER;
    }

    @Override
    public void showElement(Graphics g) {
        if (icon != null) {
            g.drawImage(icon.getImage(), x, y, width / 2, height / 2, null);
        }
        if (isSlowing()) {
            showHitbox(g);
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
        }
    }

    protected abstract void shoot();

    protected void createBullet(int bx, int by, int dx) {
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
        } else {
            keys.remove(key);
        }
    }

    public void takeDamage() {
        takeDamage(1);
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            hp = 0;
            live = false;
        }
    }

    public void addPower() {
        if (powerLevel < 5) powerLevel++;
    }

    public void addBulletDamage() {
        if (bulletDamage < 5) bulletDamage++;
    }

    public void addLife() {
        if (hp < 5) hp++;
    }

    public void bomb() {
        em.damageAllEnemies(100);
    }

    public int getHp() { return hp; }
    public int getMaxHp() { return 5; }
    public int getPowerLevel() { return powerLevel; }
    public int getMaxPowerLevel() { return 5; }
    public int getBulletDamage() { return bulletDamage; }
    public int getMaxBulletDamage() { return 5; }

    public Rectangle getHitbox() {
        int hitboxX = x + width / 4 - HITBOX_SIZE / 2;
        int hitboxY = y + height / 4 - HITBOX_SIZE / 2;
        return new Rectangle(hitboxX, hitboxY, HITBOX_SIZE, HITBOX_SIZE);
    }

    public boolean isSlowing() {
        return keys.contains(KeyEvent.VK_SHIFT);
    }

    public void showHitbox(Graphics g) {
        Rectangle hitbox = getHitbox();
        g.setColor(Color.RED);
        g.fillRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
    }
}