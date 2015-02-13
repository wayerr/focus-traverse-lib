package io.github.wayerr.ft.swing;

import javax.swing.*;
import java.awt.*;

/**
 * default implementation of focus painter, based on glassPane <p/>
 * current implementation is horrible and is suitable only for demonstration<p/>
 * Created by wayerr on 13.02.15.
 */
public final class DefaultFocusPainter implements FocusPainter {

    private final TraverseFocusSupport support;
    private GlassPane _glassPane;
    private Component oldGlassPane;

    public DefaultFocusPainter(TraverseFocusSupport support) {
        this.support = support;
    }

    private Component getGlassPane() {
        if(_glassPane == null) {
            _glassPane = new GlassPane();
        }
        return _glassPane;
    }

    @Override
    public void setCurrentFocusOwner(Component component) {
        Rectangle rectangle = component.getBounds();
        RootPaneContainer rootPaneContainer = this.support.getRootPaneContainer(component);
        rectangle.setLocation(SwingUtilities.convertPoint(component.getParent(), rectangle.getLocation(), rootPaneContainer.getRootPane()));
        _glassPane.setRectangle(rectangle);
    }

    @Override
    public void setFocusTraverseModeOn(boolean focusTraverseMode, RootPaneContainer rootPaneContainer) {
        if(focusTraverseMode) {
            //TODO update glassPane size
            Component glassPane = getGlassPane();
            rootPaneContainer.setGlassPane(glassPane);
            glassPane.setVisible(true);
        } else {
            Component glassPane = rootPaneContainer.getGlassPane();
            if (glassPane == this._glassPane) {
                glassPane.setVisible(false);
            }
        }
    }
}
