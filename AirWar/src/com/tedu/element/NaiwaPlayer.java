package com.tedu.element;

public class NaiwaPlayer extends Player {
    public NaiwaPlayer() {
        this.type = GameElement.PLAYER;
        this.bulletDamage = 2;
        this.shootInterval = 400;
    }

    @Override
    protected void shoot() {
        int bulletX = x + width / 4;
        int bulletY = y - 10;

        switch (powerLevel) {
            case 1:
                createBullet(bulletX, bulletY, 0);
                break;
            case 2:
                createBullet(bulletX - 15, bulletY, 0);
                createBullet(bulletX + 15, bulletY, 0);
                break;
            case 3:
                createBullet(bulletX, bulletY, 0);
                createBullet(bulletX - 20, bulletY, 0);
                createBullet(bulletX + 20, bulletY, 0);
                break;
            case 4:
                createBullet(bulletX - 25, bulletY, 0);
                createBullet(bulletX - 8, bulletY, 0);
                createBullet(bulletX + 8, bulletY, 0);
                createBullet(bulletX + 25, bulletY, 0);
                break;
            case 5:
                createBullet(bulletX, bulletY, 0);
                createBullet(bulletX - 30, bulletY, 0);
                createBullet(bulletX - 15, bulletY, 0);
                createBullet(bulletX + 15, bulletY, 0);
                createBullet(bulletX + 30, bulletY, 0);
                break;
        }
    }

    @Override
    public void addBulletDamage() {
        if (bulletDamage < 10) bulletDamage += 2;
    }

    @Override
    public int getMaxBulletDamage() {
        return 10;
    }
}