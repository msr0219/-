package com.tedu.controller;

import com.tedu.element.ElementObj;
import com.tedu.element.GameElement;
import com.tedu.element.Player;
import com.tedu.manager.ElementManager;
import com.tedu.show.GameMainJPanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class GameKeyListener implements KeyListener {
    private GameThread gameThread;
    private GameMainJPanel panel;
    private ElementManager em = ElementManager.getManager();

    public GameKeyListener(GameThread gameThread, GameMainJPanel panel) {
        this.gameThread = gameThread;
        this.panel = panel;
    }

    public void setGameThread(GameThread gameThread) {
        this.gameThread = gameThread;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_ENTER) {
            if (panel.isStartScreen()) {
                panel.startGame();
            } else if (panel.isGameOverScreen()) {
                panel.showStartScreen();
            } else if (panel.isGameWonScreen()) {
                panel.showStartScreen();
            }
            return;
        }

        if (key == KeyEvent.VK_P || key == KeyEvent.VK_ESCAPE) {
            if (gameThread != null && !gameThread.isGameOver() && !panel.isStartScreen()) {
                gameThread.pause();
                if (gameThread.isPaused()) {
                    panel.showPause();
                } else {
                    panel.hidePause();
                }
            }
            return;
        }

        List<ElementObj> players = em.getElementsByType(GameElement.PLAYER);
        if (!players.isEmpty()) {
            Player player = (Player) players.get(0);
            player.keyClick(true, key);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        List<ElementObj> players = em.getElementsByType(GameElement.PLAYER);
        if (!players.isEmpty()) {
            Player player = (Player) players.get(0);
            player.keyClick(false, e.getKeyCode());
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}