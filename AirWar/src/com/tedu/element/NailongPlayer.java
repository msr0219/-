package com.tedu.element;

public class NailongPlayer extends Player {
    public NailongPlayer() {
        this.type = GameElement.PLAYER;
        this.bulletDamage = 1;
        this.shootInterval = 200;
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
}