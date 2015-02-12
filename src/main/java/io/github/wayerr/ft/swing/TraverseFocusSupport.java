package io.github.wayerr.ft.swing;

import io.github.wayerr.ft.Direction;
import io.github.wayerr.ft.FocusTraverse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by wayerr on 10.02.15.
 */
public final class TraverseFocusSupport {

    private final FocusTraverse<Component> focusTraverse = new FocusTraverse<Component>(new SwingAdapter());
    private GlassPane _glassPane;

    private boolean focusTraverseMode;
    private RootPaneContainer container;
    private Component oldGlassPane;
    private Component newFocusOwner;

    public TraverseFocusSupport() {
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                KeyEvent keyEvent = (KeyEvent) event;
                boolean traverseMode = keyEvent.isAltDown();
                setFocusTraverseMode(traverseMode);
                if(traverseMode && keyEvent.getID() == KeyEvent.KEY_RELEASED) {
                    traverse(keyEvent);
                }
            }

            private void traverse(KeyEvent keyEvent) {
                final int keyCode = keyEvent.getKeyCode();
                Direction direction;
                switch (keyCode) {
                    case KeyEvent.VK_LEFT: direction = Direction.LEFT;
                        break;
                    case KeyEvent.VK_RIGHT: direction = Direction.RIGHT;
                        break;
                    case KeyEvent.VK_UP: direction = Direction.UP;
                        break;
                    case KeyEvent.VK_DOWN: direction = Direction.DOWN;
                        break;
                    default:
                        direction = null;
                }
                if(direction != null) {
                    final Component traverseRoot = container.getRootPane();
                    Component currentFocusOwner = TraverseFocusSupport.this.newFocusOwner;
                    if(currentFocusOwner == null) {
                        currentFocusOwner = getFocusOwner();
                    }
                    Component newFocusOwner = focusTraverse.traverse(traverseRoot, currentFocusOwner, direction);
                    updateFocusOwner(newFocusOwner);
                }
            }
        }, AWTEvent.KEY_EVENT_MASK);
    }

    /**
     * install TraverseFocus support on specified RootPaneContainer
     * @param rpc
     */
    public void install(final RootPaneContainer rpc) {
        this.container = rpc;
    }

    public void setFocusTraverseMode(boolean focusTraverseMode) {
        if (this.focusTraverseMode == focusTraverseMode) {
            return;
        }
        this.focusTraverseMode = focusTraverseMode;
        //TODO support for multiple containers
        if (this.focusTraverseMode) {
            this.newFocusOwner = null;
            this.oldGlassPane = container.getGlassPane();
            //TODO update glassPane size
            Component glassPane = getGlassPane();
            container.setGlassPane(glassPane);
            updateFocusOwner(getFocusOwner());
            glassPane.setVisible(true);
        } else {
            Component glassPane = container.getGlassPane();
            if (glassPane == this._glassPane) {
                container.setGlassPane(this.oldGlassPane);
            }
            if(this.newFocusOwner != null) {
                this.newFocusOwner.requestFocusInWindow();
            }
        }
    }

    private void updateFocusOwner(Component component) {
        if(component == null) {
            return;
        }
        Rectangle rectangle = component.getBounds();
        rectangle.setLocation(SwingUtilities.convertPoint(component.getParent(), rectangle.getLocation(), container.getRootPane()));
        this.newFocusOwner = component;
        _glassPane.setRectangle(rectangle);
    }

    private Component getFocusOwner() {
        return KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
    }

    public Component getGlassPane() {
        if(_glassPane == null) {
            _glassPane = new GlassPane();
        }
        return _glassPane;
    }
}
