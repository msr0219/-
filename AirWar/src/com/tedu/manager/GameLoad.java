package com.tedu.manager;

import com.tedu.element.*;

import javax.swing.*;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameLoad {
    private static GameLoad load = null;
    private Map<String, String> objMap = new HashMap<>();
    private Map<String, ImageIcon> imageMap = new HashMap<>();
    private ElementManager em = ElementManager.getManager();

    private GameLoad() {}

    public static GameLoad getLoad() {
        if (load == null) {
            synchronized (GameLoad.class) {
                if (load == null) {
                    load = new GameLoad();
                }
            }
        }
        return load;
    }

    public void loadObjPro() {
        objMap.clear();
        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(getClass().getResourceAsStream("/com/tedu/text/obj.pro")))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty() && !line.startsWith("#")) {
                    String[] parts = line.split("=");
                    if (parts.length == 2) {
                        objMap.put(parts[0].trim(), parts[1].trim());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadGameDataPro() {
        imageMap.clear();
        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(getClass().getResourceAsStream("/com/tedu/text/GameData.pro")))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty() && !line.startsWith("#")) {
                    String[] parts = line.split("=");
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String path = parts[1].trim();
                        ImageIcon icon = new ImageIcon(getClass().getResource("/" + path));
                        imageMap.put(key, icon);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ElementObj getObj(String str) {
        String className = objMap.get(str);
        if (className == null) return null;
        try {
            Class<?> clazz = Class.forName(className);
            ElementObj obj = (ElementObj) clazz.newInstance();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ImageIcon getImage(String key) {
        return imageMap.get(key);
    }

    public void loadLevel(int level) {
        String fileName = "/com/tedu/text/level_" + level + ".map";
        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(getClass().getResourceAsStream(fileName)))) {
            int currentWave = 0;
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) continue;
                if (line.startsWith("WAVE=")) {
                    currentWave = Integer.parseInt(line.split("=")[1]);
                } else if (currentWave == em.getWave()) {
                    String[] parts = line.split("=");
                    String type = parts[0];
                    String coords = parts[1];
                    String[] coordPairs = coords.split(";");
                    for (String pair : coordPairs) {
                        String[] xy = pair.split(",");
                        int x = Integer.parseInt(xy[0]);
                        int y = Integer.parseInt(xy[1]);
                        createEnemy(type, x, y);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getMaxWaves(int level) {
        switch (level) {
            case 1: return 3;
            case 2: return 4;
            case 3: return 5;
            case 4: return 6;
            case 5: return 7;
            default: return 3;
        }
    }

    public int getMaxLevel() {
        return 5;
    }

    public int getBossHp(int level) {
        return 100 * (int) Math.pow(2, level - 1);
    }

    private void createEnemy(String type, int x, int y) {
        ElementObj obj = null;
        boolean isBeliar = false;
        
        if (type.equals("BOSS") && em.getLevel() == 5) {
            try {
                Class<?> clazz = Class.forName("com.tedu.element.BeliarBoss");
                obj = (ElementObj) clazz.getDeclaredConstructor().newInstance();
                isBeliar = true;
            } catch (Exception e) {
                e.printStackTrace();
                obj = getObj(type);
            }
        } else {
            obj = getObj(type);
        }
        
        if (obj != null) {
            obj.setX(x);
            obj.setY(y);
            if (type.equals("BOSS")) {
                Boss boss = (Boss) obj;
                int baseHp = getBossHp(em.getLevel());
                boss.setMaxHp(baseHp);
                boss.applyDifficulty(em.getGameMode());
            } else if (obj instanceof EnemyPlane) {
                ((EnemyPlane) obj).applyDifficulty(em.getGameMode());
            }
            
            ImageIcon icon;
            if (isBeliar) {
                icon = getImage("beliar");
            } else {
                icon = getImage(type.toLowerCase().replace("_", "_"));
                if (icon == null) {
                    icon = getImage(type.toLowerCase());
                }
            }
            
            if (icon != null) {
                obj.setIcon(icon);
            }
            em.addElement(obj, getGameElement(type));
        }
    }

    private GameElement getGameElement(String type) {
        try {
            return GameElement.valueOf(type);
        } catch (Exception e) {
            return GameElement.ENEMY_SMALL;
        }
    }

    public void createPlayer() {
        createPlayer("NAILONG");
    }

    public void createPlayer(String characterType) {
        Player player;
        ImageIcon icon;
        
        if ("NAIWA".equals(characterType)) {
            player = new NaiwaPlayer();
            icon = getImage("naiwa");
        } else {
            player = new NailongPlayer();
            icon = getImage("player");
        }
        
        player.setX(250 - 25);
        player.setY(600);
        player.setIcon(icon);
        em.addElement(player, GameElement.PLAYER);
    }

    public void createBackground() {
        Background bg = new Background();
        bg.setIcon(getImage("background_1"));
        em.addElement(bg, GameElement.BACKGROUND);
    }

    public void switchBackground(int level) {
        List<ElementObj> backgrounds = em.getElementsByType(GameElement.BACKGROUND);
        for (ElementObj bg : backgrounds) {
            bg.setLive(false);
        }
        Background newBg = new Background();
        String bgKey = "background_" + level;
        if (!imageMap.containsKey(bgKey)) {
            bgKey = "background";
        }
        newBg.setIcon(getImage(bgKey));
        em.addElement(newBg, GameElement.BACKGROUND);
    }

    public void createPowerUp(int x, int y, PowerUpType type) {
        PowerUp powerUp = new PowerUp();
        powerUp.setX(x);
        powerUp.setY(y);
        powerUp.setPowerUpType(type);
        String imageKey = "powerup_" + type.name().toLowerCase();
        powerUp.setIcon(getImage(imageKey));
        em.addElement(powerUp, GameElement.POWERUP);
    }
}