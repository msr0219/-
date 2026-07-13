package com.tedu.element;

import javax.swing.*;
import java.awt.*;

public abstract class Bullet extends ElementObj {
    protected int damage = 1;
    protected boolean isPlayerBullet;
    protected int speed = 10;
    protected int dx = 0;

    @Override
    public void showElement(Graphics g) {
        if (icon != null) {
            icon.paintIcon(null, g, x, y);
        }
    }

    @Override
    public void move() {
        if (isPlayerBullet) {
            y -= speed;
        } else {
            y += speed;
        }
        x += dx;

        if (y < -height || y > 700 || x < -width || x > 500) {
            live = false;
        }
    }

    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; }
    public boolean isPlayerBullet() { return isPlayerBullet; }
    public void setPlayerBullet(boolean playerBullet) { isPlayerBullet = playerBullet; }
    public int getDx() { return dx; }
    public void setDx(int dx) { this.dx = dx; }
}