package io.github.wayerr.ft.swing;

import io.github.wayerr.ft.FocusTraverse;

import javax.swing.*;
import java.awt.*;
import java.util.WeakHashMap;

/**
 * Basic implementation for focus travesing by arrow and pgUp/pgDown keys in swing application <p/>
 * Usage: <code>new TraverseFocusSupport().install(frame);</code> <p/>
 * Created by wayerr on 10.02.15.
 */
public final class TraverseFocusSupport {

    final SwingAdapter swingAdapter = new SwingAdapter();
    final FocusTraverse<Component> focusTraverse = new FocusTraverse<>(swingAdapter);
    private final WeakHashMap<RootPaneContainer, WindowData> states = new WeakHashMap<>();
    private FocusPainter focusPainter;

    public TraverseFocusSupport() {
    }

    public FocusPainter getFocusPainter() {
        return focusPainter;
    }

    public void setFocusPainter(FocusPainter focusPainter) {
        this.focusPainter = focusPainter;
    }

    public void install() {
        if(focusPainter == null) {
            throw new RuntimeException("focusPainter is null");
        }
        //now we do not need anything this
    }


    public WindowData getWindowData(RootPaneContainer window) {
        if(window == null) {
            throw new NullPointerException("window is null");
        }
        WindowData data = states.get(window);
        if(data == null) {
            data = new WindowData(this, window);
            states.put(window, data);
        }
        return data;
    }

    /**
     * return RootPaneContainer which contains specified component <p/>
     * method behavior not equal with <code>SwingUtilities.getAncestorOfClass(RootPaneContainer.class, component)</>
     * @param component
     * @return
     */
    public RootPaneContainer getRootPaneContainer(Component component) {
        // we find only top level containers
        Window windowAncestor;
        if(component instanceof Window) {
            windowAncestor = (Window) component;
        } else {
            windowAncestor = SwingUtilities.getWindowAncestor(component);
        }
        if (!(windowAncestor instanceof RootPaneContainer)) {
            return null;
        }
        return (RootPaneContainer) windowAncestor;
    }
}
