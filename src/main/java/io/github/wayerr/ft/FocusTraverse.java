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
    public C traverse(C root, C current, Direction direction) {
        List<C> childs = getNeighbors(current);
        if(childs.isEmpty()) {
            return null;
        }

        Rectangle currrect = new Rectangle();
        adapter.getBounds(root, current, currrect);

        final int size = childs.size();
        C nearest = null;
        Rectangle neighborrect = new Rectangle();
        float nearestDistance = Float.MAX_VALUE;
        for(int i = 0; i < size; i++) {
            C child = childs.get(i);
            adapter.getBounds(root, child, neighborrect);
            //we must consider only components which placed in true direction from current
            if(!isPlacedIn(direction, currrect, neighborrect)) {
                continue;
            }

            float distance = distance(direction, currrect, neighborrect);
            if(nearest == null || nearestDistance > distance) {
                nearest = child;
                nearestDistance = distance;
            }
        }
        return nearest;
    }

    private boolean isPlacedIn(Direction direction, Rectangle current, Rectangle neighborrect) {
        boolean ok;
        switch (direction) {
            case RIGHT: {
                ok = current.x + current.w < neighborrect.x ;
            }
            break;
            case DOWN: {
                ok = current.y + current.h < neighborrect.y;
            }
            break;
            case LEFT: {
                ok = current.x > neighborrect.x + neighborrect.w;
            }
            break;
            case UP: {
                ok = current.y > neighborrect.y + neighborrect.h;
            }
            break;
            default:
                throw new IllegalArgumentException(direction + " illegal direction");
        }
        return ok;
    }

    private List<C> getNeighbors(C current) {
        List<C> childs = new ArrayList<C>();
        int size = 0;
        C parent = adapter.getParent(current);
        while(true) {
            if(parent == null) {
                break;
            }
            adapter.getChilds(parent, childs);
            size = childs.size();
            if(size > 1) {
                break;
            }
            parent = adapter.getParent(current);
        }
        return childs;
    }

    private float distance(Direction direction, Rectangle from, Rectangle to) {
        Line srcLine = new Line();
        Line dstLine = new Line();
        switch (direction) {
            case RIGHT: {
                getRightEdge(from, srcLine);
                getLeftEdge(to, dstLine);
            }
            break;
            case DOWN: {
                getDownEdge(from, srcLine);
                getUpEdge(to, dstLine);
            }
            break;
            case LEFT: {
                getLeftEdge(from, srcLine);
                getRightEdge(to, dstLine);
            }
            break;
            case UP: {
                getUpEdge(from, srcLine);
                getDownEdge(to, dstLine);
            }
            break;
            default:
                throw new IllegalArgumentException(direction + " illegal direction");
        }
        return distance(direction, srcLine, dstLine);
    }

    private float distance(Direction direction, Line src, Line dst) {
        // in future we must consider all axes
        float res;
        if(direction == Direction.LEFT || direction == Direction.RIGHT) {
            res = Math.max(src.fx, src.tx) - Math.max(dst.fx, dst.tx);
        } else {
            res = Math.max(src.fy, src.ty) - Math.max(dst.fy, dst.ty);
        }
        return Math.abs(res);
    }

    private void getLeftEdge(Rectangle r, Line line) {
        line.fx = line.tx = r.x;
        line.fy = r.y;
        line.ty = r.y + r.h;
    }

    private void getRightEdge(Rectangle r, Line line) {
        line.fx = line.tx = r.x + r.w;
        line.fy = r.y;
        line.ty = r.y + r.h;
    }

    private void getUpEdge(Rectangle r, Line line) {
        line.fx = r.x;
        line.tx = r.x + r.w;
        line.fy = line.ty = r.y;
    }

    private void getDownEdge(Rectangle r, Line line) {
        line.fx = r.x;
        line.tx = r.x + r.w;
        line.fy = line.ty = r.y + r.h;
    }
}
