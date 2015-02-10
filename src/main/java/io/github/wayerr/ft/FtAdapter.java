package io.github.wayerr.ft;

import java.util.List;

/**
 * iface for adapter to different gui libraries <p/>
 * Created by wayerr on 10.02.15.
 * @param <C> gui component class
 */
public interface FtAdapter<C> {

    void getBounds(C comp, Rectangle rect);

    void setBounds(Rectangle rect, C comp);

    boolean isFocused(C comp);

    /**
     * retrieve only childs which can be focused
     * @param root
     * @param childs
     */
    void getChilds(C root, List<C> childs);
}
