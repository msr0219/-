package com.tedu.element;

import javax.swing.*;
import java.awt.*;

public class PowerUp extends ElementObj {
    private PowerUpType powerUpType;
    private int speed = 3;

    public PowerUp() {
        super.setType(GameElement.POWERUP);
    }

    @Override
    public void showElement(Graphics g) {
        if (icon != null) {
            icon.paintIcon(null, g, x, y);
        }
    }

    @Override
    public void move() {
        y += speed;
        if (y > 700) {
            live = false;
        }
    }

    public PowerUpType getPowerUpType() { return powerUpType; }
    public void setPowerUpType(PowerUpType powerUpType) { this.powerUpType = powerUpType; }
}