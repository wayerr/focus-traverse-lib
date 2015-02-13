package io.github.wayerr.ft.swing;

import io.github.wayerr.ft.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;

/**
 * default keyboard adapter which responsible for toggling focus mode and transferring method invocation <p/>
 * Created by wayerr on 13.02.15.
 */
public final class DefaultKeyboardAdapter {
    private int modifiersMask = KeyEvent.ALT_DOWN_MASK;
    private boolean lastEventIsTraverse = false;
    private final TraverseFocusSupport support;

    public DefaultKeyboardAdapter(TraverseFocusSupport support) {
        this.support = support;
    }

    public int getModifiersMask() {
        return modifiersMask;
    }

    public void setModifiersMask(int modifiersMask) {
        this.modifiersMask = modifiersMask;
    }

    public void install() {
        Toolkit.getDefaultToolkit().addAWTEventListener(new KeyboardEventListener(), AWTEvent.KEY_EVENT_MASK);
    }


    private class KeyboardEventListener implements AWTEventListener {


        @Override
        public void eventDispatched(AWTEvent event) {
            KeyEvent keyEvent = (KeyEvent) event;
            final boolean traverseMode = (keyEvent.getModifiersEx() & modifiersMask) != 0;
            if(!traverseMode && !lastEventIsTraverse) {
                return;
            }

            Component component = keyEvent.getComponent();
            RootPaneContainer rootPaneContainer = support.getRootPaneContainer(component);
            if (rootPaneContainer == null) {
                return;
            }
            WindowData data = support.getWindowData(rootPaneContainer);
            data.setFocusTraverseMode(traverseMode);
            lastEventIsTraverse = traverseMode;
            if(traverseMode && keyEvent.getID() == KeyEvent.KEY_RELEASED) {
                traverse(data, keyEvent);
            }
        }
    }

    private void traverse(WindowData data, KeyEvent keyEvent) {
        final int keyCode = keyEvent.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                data.traverseFocusTo(Direction.LEFT);
                keyEvent.consume();
                break;
            case KeyEvent.VK_RIGHT:
                data.traverseFocusTo(Direction.RIGHT);
                keyEvent.consume();
                break;
            case KeyEvent.VK_UP:
                data.traverseFocusTo(Direction.UP);
                keyEvent.consume();
                break;
            case KeyEvent.VK_DOWN:
                data.traverseFocusTo(Direction.DOWN);
                keyEvent.consume();
                break;
            case KeyEvent.VK_PAGE_DOWN:
                keyEvent.consume();
                data.cycleDown();
                break;
            case KeyEvent.VK_PAGE_UP:
                data.cycleUp();
                keyEvent.consume();
                break;
        }
    }
}
