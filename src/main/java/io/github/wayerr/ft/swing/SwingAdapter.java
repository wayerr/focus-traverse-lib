package io.github.wayerr.ft.swing;

import io.github.wayerr.ft.FtAdapter;
import io.github.wayerr.ft.Rectangle;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * a Swing library adapter for focus traverse <p/>
 *
 * Created by wayerr on 10.02.15.
 */
public final class SwingAdapter implements FtAdapter<JComponent> {


    @Override
    public void getBounds(JComponent comp, Rectangle rect) {
        rect.x = comp.getX();
        rect.y = comp.getY();
        rect.w = comp.getWidth();
        rect.h = comp.getHeight();
    }

    @Override
    public void setBounds(Rectangle rect, JComponent comp) {
        comp.setBounds(rect.x, rect.y, rect.w, rect.h);
    }

    @Override
    public boolean isFocused(JComponent comp) {
        return comp.hasFocus();
    }

    @Override
    public void getChilds(JComponent root, List<JComponent> childs) {
        final int count = root.getComponentCount();
        for(int i = 0; i < count; i++) {
            Component component = root.getComponent(i);
            if(component instanceof JComponent && component.isFocusable()) {
               childs.add((JComponent) component);
            }
        }
    }
}
