package io.github.wayerr.ft.swing;

import io.github.wayerr.ft.Direction;

import javax.swing.*;
import java.awt.*;
import java.lang.ref.WeakReference;

/**
 * focus traverse data associated with concrete RootPaneContainer
* Created by wayerr on 13.02.15.
*/
public final class WindowData {
    private final TraverseFocusSupport support;

    private boolean focusTraverseMode;
    private WeakReference<RootPaneContainer> rootPaneOwnerRef;
    private Component newFocusOwner;
    private Container focusCycleRoot;

    WindowData(TraverseFocusSupport support, RootPaneContainer rootPaneContainer) {
        this.support = support;
        this.rootPaneOwnerRef = new WeakReference<>(rootPaneContainer);
    }

    /**
     * use parent of current focus cycle root for focus cycling
     */
    public void cycleUp() {
        if(this.focusCycleRoot == null || this.focusCycleRoot == getRootPane()) {
            return;
        }
        setFocusCycleRoot(this.support.swingAdapter.getParent(this.focusCycleRoot));
    }

    /**
     * use current component for focus cycling
     */
    public void cycleDown() {
        Component focusOwner = getFocusOwner();
        if(!(focusOwner instanceof Component)) {
            return;
        }
        setFocusCycleRoot((Container) focusOwner);
    }


    public void traverseFocusTo(Direction direction) {
        Component newFocusOwner = this.support.focusTraverse.traverse(getFocusCycleRoot(), getFocusOwner(), direction);
        updateFocusOwner(newFocusOwner);
    }

    private Container getFocusCycleRoot() {
        if(this.focusCycleRoot == null) {
            return getRootPane();
        }
        return this.focusCycleRoot;
    }

    private Container getRootPane() {
        RootPaneContainer rootPaneContainer = rootPaneOwnerRef.get();
        if(rootPaneContainer == null) {
            return null;
        }
        return rootPaneContainer.getRootPane();
    }

    private void setFocusCycleRoot(Container newFocusCycleRoot) {
        if(newFocusCycleRoot.getComponentCount() == 0) {
            return;
        }
        this.focusCycleRoot = newFocusCycleRoot;
        updateFocusOwner(newFocusCycleRoot.getComponent(0));
    }

    public boolean isFocusTraverseMode() {
        return focusTraverseMode;
    }

    public void setFocusTraverseMode(boolean focusTraverseMode) {
        if (this.focusTraverseMode == focusTraverseMode) {
            return;
        }
        final RootPaneContainer rootPaneContainer = rootPaneOwnerRef.get();
        if(rootPaneContainer == null) {
            this.focusTraverseMode = false;
            return;
        }
        this.focusTraverseMode = focusTraverseMode;
        FocusPainter focusPainter = this.support.getFocusPainter();
        focusPainter.setFocusTraverseModeOn(this.focusTraverseMode, rootPaneContainer);
        if (this.focusTraverseMode) {
            this.newFocusOwner = null;
            updateFocusOwner(getFocusOwner());
        } else {
            if(this.newFocusOwner != null) {
                this.newFocusOwner.requestFocusInWindow();
            }
        }
    }

    private void updateFocusOwner(Component component) {
        if(component == null) {
            return;
        }
        this.newFocusOwner = component;
        this.support.getFocusPainter().setCurrentFocusOwner(component);
    }

    private Component getFocusOwner() {
        if(this.newFocusOwner == null) {
            return KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        }
        return this.newFocusOwner;
    }
}
