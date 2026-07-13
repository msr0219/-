package com.tedu.element;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Explosion extends ElementObj {
    private int frame = 0;
    private int maxFrames = 10;
    private int radius = 30;
    private Color color = Color.ORANGE;

    public Explosion() {
        this.type = GameElement.EXPLOSION;
        this.width = radius * 2;
        this.height = radius * 2;
    }

    @Override
    public void showElement(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        float alpha = 1.0f - (frame / (float) maxFrames);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        int currentRadius = radius * (frame + 1) / maxFrames * 2;
        GradientPaint gp = new GradientPaint(
            x, y, Color.WHITE,
            x, y, color,
            false
        );
        g2d.setPaint(gp);
        g2d.fill(new Ellipse2D.Double(x - currentRadius, y - currentRadius, currentRadius * 2, currentRadius * 2));

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    @Override
    public void model(long gameTime) {
        frame++;
        if (frame >= maxFrames) {
            live = false;
        }
    }

    public int getRadius() { return radius; }
    public void setRadius(int radius) { 
        this.radius = radius; 
        this.width = radius * 2;
        this.height = radius * 2;
    }
    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }
}