package io.github.wayerr.ft.swing;

import io.github.wayerr.ft.Direction;
import io.github.wayerr.ft.FocusTraverse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Basic implementation for focus travesing by arrow and pgUp/pgDown keys in swing application <p/>
 * Usage: <code>new TraverseFocusSupport().install(frame);</code> <p/>
 * Created by wayerr on 10.02.15.
 */
public final class TraverseFocusSupport {

    private final SwingAdapter swingAdapter = new SwingAdapter();
    private final FocusTraverse<Component> focusTraverse = new FocusTraverse<Component>(swingAdapter);
    private GlassPane _glassPane;

    private boolean focusTraverseMode;
    private RootPaneContainer rootPaneOwner;
    private Component oldGlassPane;
    private Component newFocusOwner;
    private Container focusCycleRoot;

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
                switch (keyCode) {
                    case KeyEvent.VK_LEFT:
                        traverseFocusTo(Direction.LEFT);
                        break;
                    case KeyEvent.VK_RIGHT:
                        traverseFocusTo(Direction.RIGHT);
                        break;
                    case KeyEvent.VK_UP:
                        traverseFocusTo(Direction.UP);
                        break;
                    case KeyEvent.VK_DOWN:
                        traverseFocusTo(Direction.DOWN);
                        break;
                    case KeyEvent.VK_PAGE_DOWN:
                        cycleDown();
                        break;
                    case KeyEvent.VK_PAGE_UP:
                        cycleUp();
                        break;
                }
            }
        }, AWTEvent.KEY_EVENT_MASK);
    }

    /**
     * use parent of current focus cycle root for focus cycling
     */
    private void cycleUp() {
        if(this.focusCycleRoot == null || this.focusCycleRoot == rootPaneOwner.getRootPane()) {
            return;
        }
        setFocusCycleRoot(this.swingAdapter.getParent(this.focusCycleRoot));
    }

    /**
     * use current component for focus cycling
     */
    private void cycleDown() {
        Component focusOwner = getFocusOwner();
        if(!(focusOwner instanceof Component)) {
            return;
        }
        setFocusCycleRoot((Container) focusOwner);
    }


    private void traverseFocusTo(Direction direction) {
        Component newFocusOwner = focusTraverse.traverse(getFocusCycleRoot(), getFocusOwner(), direction);
        updateFocusOwner(newFocusOwner);
    }

    private Container getFocusCycleRoot() {
        if(this.focusCycleRoot == null) {
            return rootPaneOwner.getRootPane();
        }
        return this.focusCycleRoot;
    }

    private void setFocusCycleRoot(Container newFocusCycleRoot) {
        if(newFocusCycleRoot.getComponentCount() == 0) {
            return;
        }
        this.focusCycleRoot = newFocusCycleRoot;
        updateFocusOwner(newFocusCycleRoot.getComponent(0));
    }

    /**
     * install TraverseFocus support on specified RootPaneContainer
     * @param rpc
     */
    public void install(final RootPaneContainer rpc) {
        this.rootPaneOwner = rpc;
    }

    public void setFocusTraverseMode(boolean focusTraverseMode) {
        if (this.focusTraverseMode == focusTraverseMode) {
            return;
        }
        this.focusTraverseMode = focusTraverseMode;
        //TODO support for multiple containers
        if (this.focusTraverseMode) {
            this.newFocusOwner = null;
            this.oldGlassPane = rootPaneOwner.getGlassPane();
            //TODO update glassPane size
            Component glassPane = getGlassPane();
            rootPaneOwner.setGlassPane(glassPane);
            updateFocusOwner(getFocusOwner());
            glassPane.setVisible(true);
        } else {
            Component glassPane = rootPaneOwner.getGlassPane();
            if (glassPane == this._glassPane) {
                rootPaneOwner.setGlassPane(this.oldGlassPane);
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
        rectangle.setLocation(SwingUtilities.convertPoint(component.getParent(), rectangle.getLocation(), rootPaneOwner.getRootPane()));
        this.newFocusOwner = component;
        _glassPane.setRectangle(rectangle);
    }

    private Component getFocusOwner() {
        if(this.newFocusOwner == null) {
            return KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        }
        return this.newFocusOwner;
    }

    public Component getGlassPane() {
        if(_glassPane == null) {
            _glassPane = new GlassPane();
        }
        return _glassPane;
    }
}
