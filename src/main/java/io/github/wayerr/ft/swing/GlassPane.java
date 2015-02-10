package io.github.wayerr.ft.swing;

import javax.swing.*;
import java.awt.*;

/**
* Created by wayerr on 10.02.15.
*/
final class GlassPane extends JComponent {

    private Color color = new Color(0x3f3fe9);
    private Rectangle rectangle;

    public GlassPane() {
        setOpaque(false);
    }

    void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(withAlpha(64));
        g.fillRect(0, 0, getWidth(), getHeight());

        if(rectangle != null) {
            g.setColor(withAlpha(128));
            g.drawRect(0, 0, getWidth(), getHeight());
        }
    }

    private Color withAlpha(int alpha) {
        Color colorWithAlpha = new Color(color.getRGB() & 0xffffff | (alpha & 0xff) << 24, true);
        return colorWithAlpha;
    }
}
