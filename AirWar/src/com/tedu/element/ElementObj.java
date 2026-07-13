package com.tedu.element;

import javax.swing.*;
import java.awt.*;

public abstract class ElementObj {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected ImageIcon icon;
    protected boolean live = true;
    protected GameElement type;

    public abstract void showElement(Graphics g);

    public void model(long gameTime) {
        updateImage();
        move();
        add();
    }

    public void updateImage() {}

    public void move() {}

    public void add() {}

    public boolean pk(ElementObj other) {
        return false;
    }

    public void keyClick(boolean isPressed, int key) {}

    public Rectangle getRectangle() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
    public ImageIcon getIcon() { return icon; }
    public void setIcon(ImageIcon icon) { 
        this.icon = icon;
        if (icon != null) {
            this.width = icon.getIconWidth();
            this.height = icon.getIconHeight();
        }
    }
    public boolean isLive() { return live; }
    public void setLive(boolean live) { this.live = live; }
    public GameElement getType() { return type; }
    public void setType(GameElement type) { this.type = type; }
}