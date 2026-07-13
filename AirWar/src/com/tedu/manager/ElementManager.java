package com.tedu.manager;

import com.tedu.element.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElementManager {
    private static ElementManager manager = null;
    private Map<GameElement, List<ElementObj>> gameElements = new HashMap<>();
    private int score = 0;
    private int level = 1;
    private int wave = 1;
    private int combo = 0;
    private long lastKillTime = 0;

    private ElementManager() {
        for (GameElement ge : GameElement.values()) {
            gameElements.put(ge, new ArrayList<>());
        }
    }

    public static ElementManager getManager() {
        if (manager == null) {
            synchronized (ElementManager.class) {
                if (manager == null) {
                    manager = new ElementManager();
                }
            }
        }
        return manager;
    }

    public void addElement(ElementObj obj, GameElement type) {
        gameElements.get(type).add(obj);
    }

    public void removeElement(ElementObj obj, GameElement type) {
        gameElements.get(type).remove(obj);
    }

    public List<ElementObj> getElementsByType(GameElement type) {
        return gameElements.get(type);
    }

    public void addScore(int baseScore) {
        long now = System.currentTimeMillis();
        if (now - lastKillTime < 2000) {
            combo++;
        } else {
            combo = 0;
        }
        lastKillTime = now;

        double multiplier = 1.0;
        if (combo >= 20) multiplier = 3.0;
        else if (combo >= 10) multiplier = 2.0;
        else if (combo >= 5) multiplier = 1.5;

        score += (int) (baseScore * multiplier);
    }

    public void clearEnemies() {
        for (ElementObj enemy : gameElements.get(GameElement.ENEMY_SMALL)) {
            enemy.setLive(false);
        }
        for (ElementObj enemy : gameElements.get(GameElement.ENEMY_MEDIUM)) {
            enemy.setLive(false);
        }
        for (ElementObj enemy : gameElements.get(GameElement.ENEMY_LARGE)) {
            enemy.setLive(false);
        }
        for (ElementObj enemy : gameElements.get(GameElement.ELITE_PRO)) {
            enemy.setLive(false);
        }
        for (ElementObj enemy : gameElements.get(GameElement.BOSS)) {
            enemy.setLive(false);
        }
        for (ElementObj bullet : gameElements.get(GameElement.ENEMY_BULLET)) {
            bullet.setLive(false);
        }
    }

    public void freezeEnemies(long duration) {
        for (ElementObj enemy : gameElements.get(GameElement.ENEMY_SMALL)) {
            ((EnemyPlane) enemy).freeze(duration);
        }
        for (ElementObj enemy : gameElements.get(GameElement.ENEMY_MEDIUM)) {
            ((EnemyPlane) enemy).freeze(duration);
        }
        for (ElementObj enemy : gameElements.get(GameElement.ENEMY_LARGE)) {
            ((EnemyPlane) enemy).freeze(duration);
        }
        for (ElementObj enemy : gameElements.get(GameElement.ELITE_PRO)) {
            ((EnemyPlane) enemy).freeze(duration);
        }
        for (ElementObj enemy : gameElements.get(GameElement.BOSS)) {
            ((EnemyPlane) enemy).freeze(duration);
        }
    }

    public void clearAll() {
        for (GameElement ge : GameElement.values()) {
            gameElements.get(ge).clear();
        }
        score = 0;
        level = 1;
        wave = 1;
        combo = 0;
    }

    public void removeDeadElements() {
        for (GameElement ge : GameElement.values()) {
            gameElements.get(ge).removeIf(obj -> !obj.isLive());
        }
    }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public int getWave() { return wave; }
    public void setWave(int wave) { this.wave = wave; }
    public int getCombo() { return combo; }
}