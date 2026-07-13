package com.tedu.element;

public class EnemyBullet extends Bullet {
    public EnemyBullet() {
        this.type = GameElement.ENEMY_BULLET;
        this.isPlayerBullet = false;
        this.speed = 6;
    }
}