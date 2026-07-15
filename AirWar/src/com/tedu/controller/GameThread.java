package com.tedu.controller;

import com.tedu.element.*;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameLoad;
import com.tedu.show.GameMainJPanel;

import java.awt.*;
import java.util.List;

public class GameThread implements Runnable {
    private GameMainJPanel panel;
    private ElementManager em = ElementManager.getManager();
    private GameLoad gl = GameLoad.getLoad();
    private boolean running = true;
    private boolean paused = false;
    private boolean gameOver = false;
    private boolean gameWon = false;
    private boolean waitingForNextWave = false;
    private long waitStartTime = 0;
    private static final long WAVE_DELAY = 2000;
    private static final long LEVEL_DELAY = 3000;
    private boolean waitingForLevel = false;

    public GameThread(GameMainJPanel panel) {
        this.panel = panel;
    }

    @Override
    public void run() {
        while (running) {
            if (!paused && !gameOver) {
                gameRun();
            }
            panel.repaint();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void gameRun() {
        moveAndUpdate();
        ElementPK();
        em.removeDeadElements();
        checkWaveComplete();
        checkGameOver();
        checkGameWon();
    }

    private void moveAndUpdate() {
        long gameTime = System.currentTimeMillis();
        for (GameElement ge : GameElement.values()) {
            for (ElementObj obj : em.getElementsByType(ge)) {
                obj.model(gameTime);
            }
        }
    }

    private void ElementPK() {
        List<ElementObj> players = em.getElementsByType(GameElement.PLAYER);
        List<ElementObj> playerBullets = em.getElementsByType(GameElement.PLAYER_BULLET);
        List<ElementObj> enemyBullets = em.getElementsByType(GameElement.ENEMY_BULLET);
        List<ElementObj> enemies = em.getElementsByType(GameElement.ENEMY_SMALL);
        enemies.addAll(em.getElementsByType(GameElement.ENEMY_MEDIUM));
        enemies.addAll(em.getElementsByType(GameElement.ENEMY_LARGE));
        enemies.addAll(em.getElementsByType(GameElement.ELITE_PRO));
        enemies.addAll(em.getElementsByType(GameElement.BOSS));
        List<ElementObj> powerUps = em.getElementsByType(GameElement.POWERUP);

        for (ElementObj playerObj : players) {
            Player player = (Player) playerObj;
            if (!player.isLive()) continue;
            Rectangle playerHitbox = player.getHitbox();

            for (ElementObj enemyObj : enemies) {
                if (!enemyObj.isLive()) continue;
                if (playerHitbox.intersects(enemyObj.getRectangle())) {
                    EnemyPlane enemy = (EnemyPlane) enemyObj;
                    if (enemyObj.getType() == GameElement.BOSS) {
                        player.takeDamage(100);
                    } else {
                        player.takeDamage();
                        enemy.takeDamage(999);
                    }
                }
            }

            for (ElementObj bulletObj : enemyBullets) {
                if (!bulletObj.isLive()) continue;
                if (playerHitbox.intersects(bulletObj.getRectangle())) {
                    player.takeDamage();
                    bulletObj.setLive(false);
                }
            }

            for (ElementObj powerUpObj : powerUps) {
                if (!powerUpObj.isLive()) continue;
                if (player.getRectangle().intersects(powerUpObj.getRectangle())) {
                    applyPowerUp(player, (PowerUp) powerUpObj);
                    powerUpObj.setLive(false);
                }
            }
        }

        for (ElementObj bulletObj : playerBullets) {
            if (!bulletObj.isLive()) continue;
            PlayerBullet bullet = (PlayerBullet) bulletObj;

            for (ElementObj enemyObj : enemies) {
                if (!enemyObj.isLive()) continue;
                if (bullet.getRectangle().intersects(enemyObj.getRectangle())) {
                    EnemyPlane enemy = (EnemyPlane) enemyObj;
                    enemy.takeDamage(bullet.getDamage());
                    if (!enemy.isLive()) {
                        dropPowerUp(enemyObj);
                    }
                    bullet.setLive(false);
                    break;
                }
            }
        }
    }

    private void applyPowerUp(Player player, PowerUp powerUp) {
        switch (powerUp.getPowerUpType()) {
            case BULLET:
                player.addPower();
                break;
            case BULLET_PLUS:
                player.addBulletDamage();
                break;
            case BOMB:
                player.bomb();
                break;
            case BLOOD:
                player.addLife();
                break;
            case FREEZE:
                em.freezeEnemies(3000);
                break;
        }
    }

    private void dropPowerUp(ElementObj enemy) {
        if (Math.random() < 0.3) {
            PowerUpType[] types = PowerUpType.values();
            PowerUpType type = types[(int) (Math.random() * types.length)];
            gl.createPowerUp(enemy.getX(), enemy.getY(), type);
        }
    }

    private void checkWaveComplete() {
        if (waitingForNextWave || waitingForLevel) {
            long now = System.currentTimeMillis();
            if (waitingForNextWave && now - waitStartTime >= WAVE_DELAY) {
                waitingForNextWave = false;
                int currentLevel = em.getLevel();
                em.setWave(em.getWave() + 1);
                gl.loadLevel(currentLevel);
            } else if (waitingForLevel && now - waitStartTime >= LEVEL_DELAY) {
                waitingForLevel = false;
                int currentLevel = em.getLevel();
                em.setLevel(currentLevel + 1);
                em.setWave(1);
                gl.switchBackground(currentLevel + 1);
                gl.loadLevel(em.getLevel());
            }
            return;
        }

        List<ElementObj> enemies = em.getElementsByType(GameElement.ENEMY_SMALL);
        enemies.addAll(em.getElementsByType(GameElement.ENEMY_MEDIUM));
        enemies.addAll(em.getElementsByType(GameElement.ENEMY_LARGE));
        enemies.addAll(em.getElementsByType(GameElement.ELITE_PRO));
        enemies.addAll(em.getElementsByType(GameElement.BOSS));

        boolean hasAliveEnemy = enemies.stream().anyMatch(ElementObj::isLive);
        if (!hasAliveEnemy) {
            int currentLevel = em.getLevel();
            int currentWave = em.getWave();
            int maxWaves = gl.getMaxWaves(currentLevel);
            
            if (currentWave >= maxWaves) {
                if (currentLevel >= gl.getMaxLevel()) {
                    gameWon = true;
                } else {
                    waitingForLevel = true;
                    waitStartTime = System.currentTimeMillis();
                }
            } else {
                waitingForNextWave = true;
                waitStartTime = System.currentTimeMillis();
            }
        }
    }

    private void checkGameOver() {
        List<ElementObj> players = em.getElementsByType(GameElement.PLAYER);
        if (players.isEmpty() || !players.get(0).isLive()) {
            gameOver = true;
            running = false;
            panel.showGameOver();
        }
    }

    private void checkGameWon() {
        if (gameWon) {
            gameOver = true;
            running = false;
            panel.showGameWon();
        }
    }

    public void pause() {
        paused = !paused;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void restart() {
        em.clearAll();
        gameOver = false;
        gameWon = false;
        paused = false;
        gl.createPlayer();
        gl.createBackground();
        gl.loadLevel(1);
        panel.restartGame();
    }

    public void stop() {
        running = false;
    }
}