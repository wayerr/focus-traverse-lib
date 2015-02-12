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
public final class SwingAdapter implements FtAdapter<Component> {


    @Override
    public void getBounds(Component root, Component comp, Rectangle rect) {
        Point point = SwingUtilities.convertPoint(comp.getParent(), comp.getX(), comp.getY(), root);
        rect.x = point.x;
        rect.y = point.y;
        rect.w = comp.getWidth();
        rect.h = comp.getHeight();
    }

    @Override
    public boolean isFocused(Component comp) {
        return comp.hasFocus();
    }

    @Override
    public void getChilds(Component comp, List<Component> childs) {
        if(!(comp instanceof Container)) {
            return;
        }
        Container cont = (Container) comp;
        final int count = cont.getComponentCount();
        for(int i = 0; i < count; i++) {
            Component component = cont.getComponent(i);
            if(component instanceof JScrollPane) {
                component = ((JScrollPane)component).getViewport().getView();
            }
            if(component.isFocusable()) {
               childs.add(component);
            }
        }
    }

    @Override
    public Component getParent(Component child) {
        Container parent = child.getParent();
        if(parent instanceof JViewport) {
            parent = parent.getParent();
        }
        if(parent instanceof JScrollPane) {
            parent = parent.getParent();
        }
        return parent;
    }
}
