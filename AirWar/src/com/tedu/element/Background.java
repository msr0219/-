package com.tedu.element;

import javax.swing.*;
import java.awt.*;

public class Background extends ElementObj {
    private int yOffset = 0;
    private int speed = 2;

    public Background() {
        this.type = GameElement.BACKGROUND;
        this.x = 0;
        this.y = 0;
    }

    @Override
    public void showElement(Graphics g) {
        if (icon != null) {
            g.drawImage(icon.getImage(), 0, yOffset, 500, 700, null);
            g.drawImage(icon.getImage(), 0, yOffset - 700, 500, 700, null);
        }
    }

    @Override
    public void move() {
        yOffset += speed;
        if (yOffset >= 700) {
            yOffset = 0;
        }
    }
}