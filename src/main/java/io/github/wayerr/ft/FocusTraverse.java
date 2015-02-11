package io.github.wayerr.ft;

import java.util.ArrayList;
import java.util.Arrays;
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
        List<C> childs = new ArrayList<C>();
        int size = 0;
        C parent = adapter.getParent(current);
        while(true) {
            if(parent == null) {
                return null;
            }
            adapter.getChilds(parent, childs);
            size = childs.size();
            if(size > 1) {
                break;
            }
            parent = adapter.getParent(current);
        }
        //we must exclude current
        childs.remove(current);
        size = childs.size();

        Rectangle currrect = new Rectangle();
        adapter.getBounds(root, current, currrect);
        Rectangle neighborrect = new Rectangle();
        C nearest = null;
        float nearestDistance = Float.MAX_VALUE;
        System.out.println("curr rect: " + currrect);
        for(int i = 0; i < size; i++) {
            C child = childs.get(i);
            adapter.getBounds(root, child, neighborrect);
            float distance = distance(direction, currrect, neighborrect);
            System.out.println("rect: " + neighborrect);
            System.out.println("distance: " + distance);
            if(nearest == null || nearestDistance > distance) {
                nearest = child;
                nearestDistance = distance;
            }
        }
        System.out.println(" res: " + nearestDistance);
        return nearest;
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
