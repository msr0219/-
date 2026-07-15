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

        if (panel.isStartScreen()) {
            handleStartScreen(key);
            return;
        }

        if (panel.isHelpScreen()) {
            handleHelpScreen(key);
            return;
        }

        if (panel.isModeScreen()) {
            handleModeScreen(key);
            return;
        }

        if (panel.isCharacterScreen()) {
            handleCharacterScreen(key);
            return;
        }

        if (key == KeyEvent.VK_ENTER) {
            if (panel.isGameOverScreen()) {
                panel.showStartScreen();
            } else if (panel.isGameWonScreen()) {
                panel.showStartScreen();
            }
            return;
        }

        if (key == KeyEvent.VK_P) {
            if (gameThread != null && !gameThread.isGameOver()) {
                gameThread.pause();
                if (gameThread.isPaused()) {
                    panel.showPause();
                } else {
                    panel.hidePause();
                }
            }
            return;
        }

        if (key == KeyEvent.VK_ESCAPE) {
            if (gameThread != null && gameThread.isPaused()) {
                panel.showStartScreen();
                return;
            }
            if (gameThread != null && !gameThread.isGameOver()) {
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

    private void handleStartScreen(int key) {
        if (key == KeyEvent.VK_ENTER) {
            int selected = panel.getSelectedStartOption();
            if (selected == 0) {
                panel.showModeScreen();
            } else if (selected == 1) {
                panel.showHelpScreen();
            } else if (selected == 2) {
                System.exit(0);
            }
        } else if (key == KeyEvent.VK_UP) {
            panel.prevStartOption();
        } else if (key == KeyEvent.VK_DOWN) {
            panel.nextStartOption();
        }
    }

    private void handleHelpScreen(int key) {
        if (key == KeyEvent.VK_ESCAPE) {
            panel.showStartScreen();
        }
    }

    private void handleModeScreen(int key) {
        if (key == KeyEvent.VK_ENTER) {
            panel.showCharacterScreen();
        } else if (key == KeyEvent.VK_UP) {
            panel.prevMode();
        } else if (key == KeyEvent.VK_DOWN) {
            panel.nextMode();
        } else if (key == KeyEvent.VK_ESCAPE) {
            panel.showStartScreen();
        }
    }

    private void handleCharacterScreen(int key) {
        if (key == KeyEvent.VK_ENTER) {
            panel.startGame();
        } else if (key == KeyEvent.VK_LEFT) {
            panel.prevCharacter();
        } else if (key == KeyEvent.VK_RIGHT) {
            panel.nextCharacter();
        } else if (key == KeyEvent.VK_ESCAPE) {
            panel.showModeScreen();
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