package io.github.wayerr.ft;

import java.util.ArrayList;
import java.util.List;

/**
 * entry point for FocusTraverse tool <p/>
 * Created by wayerr on 10.02.15.
 */
public final class FocusTraverse<C> {

    private FtAdapter<C> adapter;

    public FocusTraverse(FtAdapter<C> adapter) {
        this.adapter = adapter;
    }

    /**
     * traverse focus to specified direction
     * @param root
     * @param direction direction in which focus must be traversed
     */
    public void traverse(C root, Direction direction) {
        List<C> childs = new ArrayList<C>();
        adapter.getChilds(root, childs);
        System.out.println("traverse to " + direction);
        for(C child: childs) {

        }
    }
}
