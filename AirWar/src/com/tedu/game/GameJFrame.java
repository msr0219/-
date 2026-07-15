package com.tedu.game;

import com.tedu.show.GameMainJPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameJFrame extends JFrame {
    private GameMainJPanel panel;

    public GameJFrame() {
        setTitle("奶龙大战贝利亚");
        setSize(500, 725);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        panel = new GameMainJPanel();
        add(panel);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                panel.stopGame();
                System.exit(0);
            }
        });

        setVisible(true);
    }
}