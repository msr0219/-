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
    private boolean showHelpScreen = false;
    private boolean showModeScreen = false;
    private boolean showCharacterScreen = false;
    private boolean showPauseScreen = false;
    private boolean showGameOverScreen = false;
    private boolean showGameWonScreen = false;
    
    private int selectedMode = 1;
    private int selectedCharacter = 0;
    private int selectedStartOption = 0;
    private String gameMode = "NORMAL";
    private String characterType = "NAILONG";

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
        showHelpScreen = false;
        showModeScreen = false;
        showCharacterScreen = false;
        showPauseScreen = false;
        showGameOverScreen = false;
        showGameWonScreen = false;

        switch (selectedMode) {
            case 0: gameMode = "EASY"; break;
            case 1: gameMode = "NORMAL"; break;
            case 2: gameMode = "HARD"; break;
        }

        switch (selectedCharacter) {
            case 0: characterType = "NAILONG"; break;
            case 1: characterType = "NAIWA"; break;
        }

        em.setGameMode(gameMode);

        em.clearAll();
        GameLoad gl = GameLoad.getLoad();
        gl.createPlayer(characterType);
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

        if (showHelpScreen) {
            drawHelpScreen(g);
            return;
        }

        if (showModeScreen) {
            drawModeScreen(g);
            return;
        }

        if (showCharacterScreen) {
            drawCharacterScreen(g);
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
            g.drawString("生命: " + player.getHp() + "/" + player.getMaxHp(), 400, 30);
            g.drawString("火力: " + player.getPowerLevel() + "/" + player.getMaxPowerLevel(), 400, 55);
            g.drawString("伤害: " + player.getBulletDamage() + "/" + player.getMaxBulletDamage(), 400, 80);
            if (player.isHasShield()) {
                g.drawString("护盾: ON", 400, 105);
            }
        }

        drawBossHealthBar(g);
    }

    private void drawBossHealthBar(Graphics g) {
        List<ElementObj> bosses = em.getElementsByType(GameElement.BOSS);
        if (bosses.isEmpty()) return;
        
        Boss boss = (Boss) bosses.get(0);
        if (!boss.isLive()) return;
        
        int barWidth = 300;
        int barHeight = 20;
        int barX = (550 - barWidth) / 2;
        int barY = 50;
        
        g.setColor(Color.BLACK);
        g.fillRect(barX - 2, barY - 2, barWidth + 4, barHeight + 4);
        
        g.setColor(Color.RED);
        g.fillRect(barX, barY, barWidth, barHeight);
        
        double hpPercent = (double) boss.getHp() / boss.getMaxHp();
        int healthWidth = (int) (barWidth * hpPercent);
        
        g.setColor(Color.GREEN);
        g.fillRect(barX, barY, healthWidth, barHeight);
        
        g.setColor(Color.WHITE);
        g.setFont(getChineseFont(Font.BOLD, 14));
        String hpText = "BOSS: " + boss.getHp() + "/" + boss.getMaxHp();
        int textX = barX + (barWidth - g.getFontMetrics().stringWidth(hpText)) / 2;
        g.drawString(hpText, textX, barY + 15);
    }

    private void drawStartScreen(Graphics g) {
        ImageIcon mainBgIcon = GameLoad.getLoad().getImage("main_background");
        if (mainBgIcon != null) {
            g.drawImage(mainBgIcon.getImage(), 0, 0, 500, 700, null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 500, 700);
        }

        String[] options = {"进入游戏", "获得帮助", "退出游戏"};
        int[] yPositions = {380, 450, 520};

        for (int i = 0; i < options.length; i++) {
            if (selectedStartOption == i) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.WHITE);
            }
            g.setFont(getChineseFont(Font.BOLD, 28));
            g.drawString(options[i], 170, yPositions[i]);
        }

        g.setColor(Color.GREEN);
        g.setFont(getChineseFont(Font.BOLD, 20));
        g.drawString("按方向键上下切换", 150, 600);
        g.drawString("按 ENTER 确认选择", 150, 640);
    }

    private void drawHelpScreen(Graphics g) {
        ImageIcon mainBgIcon = GameLoad.getLoad().getImage("main_background");
        if (mainBgIcon != null) {
            g.drawImage(mainBgIcon.getImage(), 0, 0, 500, 700, null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 500, 700);
        }

        g.setColor(Color.WHITE);
        g.setFont(getChineseFont(Font.PLAIN, 18));
        g.drawString("操作说明:", 200, 150);
        g.drawString("方向键 - 移动", 170, 190);
        g.drawString("Z键 - 射击", 190, 220);
        g.drawString("Shift - 减速", 190, 250);
        g.drawString("P/ESC - 暂停", 180, 280);
        g.drawString("B/ENTER - 炸弹", 180, 310);

        g.setFont(getChineseFont(Font.PLAIN, 16));
        g.drawString("道具说明:", 200, 380);
        g.drawString("子弹 - 提升火力等级", 150, 410);
        g.drawString("子弹+ - 提升伤害", 160, 435);
        g.drawString("炸弹 - 对全场敌人造成伤害", 130, 460);
        g.drawString("血包 - 恢复生命", 170, 485);
        g.drawString("护盾 - 抵挡一次伤害", 160, 510);

        g.setColor(Color.GREEN);
        g.setFont(getChineseFont(Font.BOLD, 24));
        g.drawString("按 ESC 返回主菜单", 140, 620);
    }

    private void drawModeScreen(Graphics g) {
        ImageIcon mainBgIcon = GameLoad.getLoad().getImage("main_background");
        if (mainBgIcon != null) {
            g.drawImage(mainBgIcon.getImage(), 0, 0, 500, 700, null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 500, 700);
        }

        g.setColor(Color.WHITE);
        g.setFont(getChineseFont(Font.BOLD, 32));
        g.drawString("选择游戏模式", 130, 200);

        String[] modes = {"简单模式", "普通模式", "困难模式"};
        int[] yPositions = {300, 380, 460};

        for (int i = 0; i < modes.length; i++) {
            if (selectedMode == i) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.WHITE);
            }
            g.setFont(getChineseFont(Font.BOLD, 28));
            g.drawString(modes[i], 170, yPositions[i]);
        }

        g.setColor(Color.GREEN);
        g.setFont(getChineseFont(Font.BOLD, 20));
        g.drawString("按方向键上下切换", 150, 550);
        g.drawString("按 ENTER 确认选择", 150, 590);
        g.drawString("按 ESC 返回主菜单", 150, 630);
    }

    private void drawCharacterScreen(Graphics g) {
        ImageIcon mainBgIcon = GameLoad.getLoad().getImage("main_background");
        if (mainBgIcon != null) {
            g.drawImage(mainBgIcon.getImage(), 0, 0, 500, 700, null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 500, 700);
        }

        g.setColor(Color.WHITE);
        g.setFont(getChineseFont(Font.BOLD, 32));
        g.drawString("选择角色", 170, 150);

        ImageIcon nailongIcon = GameLoad.getLoad().getImage("player");
        ImageIcon naiwaIcon = GameLoad.getLoad().getImage("naiwa");

        if (nailongIcon != null) {
            g.drawImage(nailongIcon.getImage(), 80, 250, 100, 100, null);
        }
        if (naiwaIcon != null) {
            g.drawImage(naiwaIcon.getImage(), 320, 250, 100, 100, null);
        }

        if (selectedCharacter == 0) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.WHITE);
        }
        g.setFont(getChineseFont(Font.BOLD, 24));
        g.drawString("奶龙", 105, 400);

        if (selectedCharacter == 1) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.WHITE);
        }
        g.drawString("奶蛙", 345, 400);

        g.setColor(Color.WHITE);
        g.setFont(getChineseFont(Font.PLAIN, 16));
        g.drawString("射速正常，弹道扩散", 60, 450);
        g.drawString("射速慢一倍，威力大一倍", 280, 450);
        g.drawString("弹道垂直向前", 330, 475);

        g.setColor(Color.GREEN);
        g.setFont(getChineseFont(Font.BOLD, 20));
        g.drawString("按方向键左右切换", 150, 580);
        g.drawString("按 ENTER 开始游戏", 150, 620);
        g.drawString("按 ESC 返回上一界面", 150, 660);
    }

    private void drawPauseScreen(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, 500, 700);

        g.setColor(Color.YELLOW);
        g.setFont(getChineseFont(Font.BOLD, 48));
        g.drawString("暂停", 200, 350);

        g.setColor(Color.WHITE);
        g.setFont(getChineseFont(Font.PLAIN, 18));
        g.drawString("按 P 继续", 200, 420);
        g.setColor(Color.GREEN);
        g.setFont(getChineseFont(Font.BOLD, 20));
        g.drawString("按 ESC 回到主菜单", 150, 470);
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
        showHelpScreen = false;
        showModeScreen = false;
        showCharacterScreen = false;
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

    public void showHelpScreen() {
        showStartScreen = false;
        showHelpScreen = true;
        showModeScreen = false;
        showCharacterScreen = false;
        repaint();
    }

    public void showModeScreen() {
        showStartScreen = false;
        showHelpScreen = false;
        showModeScreen = true;
        showCharacterScreen = false;
        selectedMode = 1;
        repaint();
    }

    public void showCharacterScreen() {
        showStartScreen = false;
        showHelpScreen = false;
        showModeScreen = false;
        showCharacterScreen = true;
        selectedCharacter = 0;
        repaint();
    }

    public void prevMode() {
        selectedMode = (selectedMode - 1 + 3) % 3;
        repaint();
    }

    public void nextMode() {
        selectedMode = (selectedMode + 1) % 3;
        repaint();
    }

    public void prevCharacter() {
        selectedCharacter = (selectedCharacter - 1 + 2) % 2;
        repaint();
    }

    public void nextCharacter() {
        selectedCharacter = (selectedCharacter + 1) % 2;
        repaint();
    }

    public int getSelectedStartOption() {
        return selectedStartOption;
    }

    public void prevStartOption() {
        selectedStartOption = (selectedStartOption - 1 + 3) % 3;
        repaint();
    }

    public void nextStartOption() {
        selectedStartOption = (selectedStartOption + 1) % 3;
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

    public boolean isHelpScreen() {
        return showHelpScreen;
    }

    public boolean isModeScreen() {
        return showModeScreen;
    }

    public boolean isCharacterScreen() {
        return showCharacterScreen;
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