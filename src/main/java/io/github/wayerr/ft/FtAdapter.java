package io.github.wayerr.ft;

import java.util.List;

/**
 * iface for adapter to different gui libraries <p/>
 * Created by wayerr on 10.02.15.
 * @param <C> gui component class
 */
public interface FtAdapter<C> {

    void getBounds(C root, C comp, Rectangle rect);

    boolean isFocused(C comp);

    /**
     * retrieve only childs which can be focused
     * @param root
     * @param childs
     */
    void getChilds(C root, List<C> childs);

    /**
     * return parent of child or null
     * @param child
     * @return
     */
    C getParent(C child);
}
