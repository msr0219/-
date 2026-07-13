package com.tedu.element;

public class PlayerBullet extends Bullet {
    public PlayerBullet() {
        this.type = GameElement.PLAYER_BULLET;
        this.isPlayerBullet = true;
        this.speed = 12;
    }
}