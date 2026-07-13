package com.tedu.show;

import com.tedu.controller.GameKeyListener;
import com.tedu.controller.GameThread;
import com.tedu.element.*;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameLoad;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameMainJPanel extends JPanel {
    private ElementManager em = ElementManager.getManager();
    private GameThread gameThread;
    private GameKeyListener keyListener;
    private boolean showStartScreen = true;
    private boolean showPauseScreen = false;
    private boolean showGameOverScreen = false;
    private boolean showGameWonScreen = false;

    public GameMainJPanel() {
        setSize(500, 700);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        addFocusListener(new java.awt.event.FocusListener() {
            public void focusGained(java.awt.event.FocusEvent e) {}
            public void focusLost(java.awt.event.FocusEvent e) {
                requestFocusInWindow();
            }
        });

        keyListener = new GameKeyListener(gameThread, this);
        addKeyListener(keyListener);

        GameLoad.getLoad().loadObjPro();
        GameLoad.getLoad().loadGameDataPro();

        new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
            requestFocusInWindow();
        }).start();
    }

    public void startGame() {
        showStartScreen = false;
        showPauseScreen = false;
        showGameOverScreen = false;
        showGameWonScreen = false;

        em.clearAll();
        GameLoad gl = GameLoad.getLoad();
        gl.createPlayer();
        gl.createBackground();
        gl.loadLevel(1);

        gameThread = new GameThread(this);
        keyListener.setGameThread(gameThread);

        requestFocusInWindow();
        new Thread(gameThread).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (showStartScreen) {
            drawStartScreen(g);
            return;
        }

        if (showGameWonScreen) {
            drawGameWonScreen(g);
            return;
        }

        if (showGameOverScreen) {
            drawGameOverScreen(g);
            return;
        }

        for (ElementObj obj : em.getElementsByType(GameElement.BACKGROUND)) {
            obj.showElement(g);
        }
        
        for (GameElement ge : GameElement.values()) {
            if (ge != GameElement.BACKGROUND) {
                for (ElementObj obj : em.getElementsByType(ge)) {
                    obj.showElement(g);
                }
            }
        }

        drawHUD(g);

        if (showPauseScreen) {
            drawPauseScreen(g);
        }
    }

    private Font getChineseFont(int style, int size) {
        Font font = new Font("Microsoft YaHei", style, size);
        if (font.getFamily().contains("Microsoft YaHei")) {
            return font;
        }
        font = new Font("SimHei", style, size);
        if (font.getFamily().contains("SimHei")) {
            return font;
        }
        font = new Font("SimSun", style, size);
        if (font.getFamily().contains("SimSun")) {
            return font;
        }
        return new Font("Dialog", style, size);
    }

    private void drawHUD(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(getChineseFont(Font.BOLD, 18));
        g.drawString("分数: " + em.getScore(), 10, 30);
        g.drawString("关卡: " + em.getLevel(), 10, 55);
        g.drawString("波次: " + em.getWave(), 10, 80);
        g.drawString("连击: " + em.getCombo(), 10, 105);

        List<ElementObj> players = em.getElementsByType(GameElement.PLAYER);
        if (!players.isEmpty()) {
            Player player = (Player) players.get(0);
            g.drawString("生命: " + player.getHp(), 400, 30);
            g.drawString("火力: " + player.getPowerLevel(), 400, 55);
            g.drawString("伤害: " + player.getBulletDamage(), 400, 80);
            if (player.isHasShield()) {
                g.drawString("护盾: ON", 400, 105);
            }
        }
    }

    private void drawStartScreen(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 500, 700);

        g.setColor(Color.RED);
        g.setFont(getChineseFont(Font.BOLD, 48));
        g.drawString("飞机大战", 120, 200);

        g.setColor(Color.WHITE);
        g.setFont(getChineseFont(Font.PLAIN, 24));
        g.drawString("雷霆战机", 180, 260);

        g.setFont(getChineseFont(Font.PLAIN, 18));
        g.drawString("操作说明:", 200, 350);
        g.drawString("方向键 - 移动", 170, 390);
        g.drawString("Z键 - 射击", 190, 420);
        g.drawString("Shift - 减速", 190, 450);
        g.drawString("P/ESC - 暂停", 180, 480);

        g.setColor(Color.GREEN);
        g.setFont(getChineseFont(Font.BOLD, 24));
        g.drawString("按 ENTER 开始游戏", 130, 550);
    }

    private void drawPauseScreen(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, 500, 700);

        g.setColor(Color.YELLOW);
        g.setFont(getChineseFont(Font.BOLD, 48));
        g.drawString("暂停", 200, 350);

        g.setColor(Color.WHITE);
        g.setFont(getChineseFont(Font.PLAIN, 18));
        g.drawString("按 P 或 ESC 继续", 160, 420);
    }

    private void drawGameOverScreen(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 500, 700);

        g.setColor(Color.RED);
        g.setFont(getChineseFont(Font.BOLD, 48));
        g.drawString("你死了", 160, 200);
        g.drawString("游戏结束", 130, 250);

        g.setColor(Color.WHITE);
        g.setFont(getChineseFont(Font.PLAIN, 24));
        g.drawString("最终得分: " + em.getScore(), 170, 350);
        g.drawString("到达关卡: " + em.getLevel(), 180, 400);

        g.setColor(Color.GREEN);
        g.setFont(getChineseFont(Font.BOLD, 24));
        g.drawString("按 ENTER 回到主菜单", 120, 500);
    }

    private void drawGameWonScreen(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 500, 700);

        g.setColor(new Color(255, 215, 0));
        g.setFont(getChineseFont(Font.BOLD, 48));
        g.drawString("通关成功", 120, 250);

        g.setColor(Color.WHITE);
        g.setFont(getChineseFont(Font.PLAIN, 24));
        g.drawString("恭喜你通关了所有关卡！", 120, 350);
        g.drawString("最终得分: " + em.getScore(), 170, 420);

        g.setColor(Color.GREEN);
        g.setFont(getChineseFont(Font.BOLD, 24));
        g.drawString("按 ENTER 回到主菜单", 120, 520);
    }

    public void showPause() {
        showPauseScreen = true;
    }

    public void hidePause() {
        showPauseScreen = false;
    }

    public void showGameOver() {
        showGameOverScreen = true;
        requestFocusInWindow();
    }

    public void showGameWon() {
        showGameWonScreen = true;
        requestFocusInWindow();
    }

    public void showStartScreen() {
        showStartScreen = true;
        showPauseScreen = false;
        showGameOverScreen = false;
        showGameWonScreen = false;
        
        if (gameThread != null) {
            gameThread.stop();
            gameThread = null;
        }
        
        em.clearAll();
        repaint();
    }

    public void restartGame() {
        showStartScreen = false;
        showPauseScreen = false;
        showGameOverScreen = false;
        showGameWonScreen = false;
    }

    public boolean isStartScreen() {
        return showStartScreen;
    }

    public boolean isGameWonScreen() {
        return showGameWonScreen;
    }

    public boolean isGameOverScreen() {
        return showGameOverScreen;
    }

    public void stopGame() {
        if (gameThread != null) {
            gameThread.stop();
        }
    }
}