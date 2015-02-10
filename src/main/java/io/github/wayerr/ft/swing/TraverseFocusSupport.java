package io.github.wayerr.ft.swing;

import io.github.wayerr.ft.Direction;
import io.github.wayerr.ft.FocusTraverse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by wayerr on 10.02.15.
 */
public final class TraverseFocusSupport {

    private final FocusTraverse<JComponent> focusTraverse = new FocusTraverse<JComponent>(new SwingAdapter());
    private boolean focusTraverseMode;
    private RootPaneContainer container;

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
                    focusTraverse.traverse(container.getRootPane(), direction);
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
        GlassPane glassPane = new GlassPane();
        rpc.setGlassPane(glassPane);
        //rpc.getLayeredPane().setLayer(glassPane, JLayeredPane.DEFAULT_LAYER, 0);
        //rpc.getLayeredPane().setLayer(b, JLayeredPane.DRAG_LAYER, -1);
        //rpc.getLayeredPane().setLayer(glassPane, JLayeredPane.DEFAULT_LAYER, 0);
        //rpc.getLayeredPane().setVisible(true);
    }

    public void setFocusTraverseMode(boolean focusTraverseMode) {
        if(this.focusTraverseMode == focusTraverseMode) {
            return;
        }
        this.focusTraverseMode = focusTraverseMode;
        //TODO support for multiple containers
        container.getGlassPane().setVisible(this.focusTraverseMode);
    }
}
