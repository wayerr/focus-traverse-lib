package io.github.wayerr.ft.swing;

import javax.swing.*;
import java.awt.*;

/**
 * focus painter iface <p/>
 * Created by wayerr on 13.02.15.
 */
public interface FocusPainter {

    void setCurrentFocusOwner(Component component);

    void setFocusTraverseModeOn(boolean focusTraverseMode, RootPaneContainer rootPaneContainer);
}
