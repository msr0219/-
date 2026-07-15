package com.tedu.element;

import javax.swing.*;
import java.awt.*;

public abstract class Bullet extends ElementObj {
    protected int damage = 1;
    protected boolean isPlayerBullet;
    protected int speed = 10;
    protected int dx = 0;
    protected int dy = 0;
    protected double waveAmplitude = 0;
    protected double waveFrequency = 0;
    protected double waveOffset = 0;
    protected long spawnTime = 0;

    @Override
    public void showElement(Graphics g) {
        if (icon != null) {
            icon.paintIcon(null, g, x, y);
        }
    }

    @Override
    public void move() {
        if (spawnTime == 0) {
            spawnTime = System.currentTimeMillis();
        }

        if (isPlayerBullet) {
            y -= speed;
        } else {
            if (dy != 0) {
                y += dy;
            } else {
                y += speed;
            }
        }

        if (waveAmplitude > 0 && waveFrequency > 0) {
            double time = (System.currentTimeMillis() - spawnTime) * 0.001;
            x += dx + Math.sin(time * waveFrequency + waveOffset) * waveAmplitude * 0.1;
        } else {
            x += dx;
        }

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
    public int getDy() { return dy; }
    public void setDy(int dy) { this.dy = dy; }
    public double getWaveAmplitude() { return waveAmplitude; }
    public void setWaveAmplitude(double waveAmplitude) { this.waveAmplitude = waveAmplitude; }
    public double getWaveFrequency() { return waveFrequency; }
    public void setWaveFrequency(double waveFrequency) { this.waveFrequency = waveFrequency; }
}