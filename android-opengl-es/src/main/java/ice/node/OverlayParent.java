package ice.node;

import android.view.MotionEvent;

import javax.microedition.khronos.opengles.GL11;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * User: ice
 * Date: 11-11-14
 * Time: 下午12:00
 */
public class OverlayParent extends Overlay {


    public OverlayParent() {
        children = new CopyOnWriteArrayList<Overlay>();

        setupTouchDispatcher();
    }

    @Override
    protected void onDraw(GL11 gl) {

        for (Overlay overlay : children) {

            if (overlay.isRemovable()) {
                overlay.onOutdated(gl);
                remove(overlay);
                continue;
            }

            overlay.draw(gl);
        }
    }

    public void addChild(Overlay child) {
        addChildren(child);
    }

    public void addChild(int index, Overlay child) {
        child.setParent(this);
        children.add(index, child);
    }

    public void addChildren(Overlay... children) {
        addChildren(Arrays.asList(children));
    }

    public void addChildren(Collection<? extends Overlay> children) {

        for (Overlay child : children) {
            if (child == null) throw new NullPointerException();
            child.setParent(this);
        }

        this.children.addAll(children);
    }

    public boolean containsChild(Overlay child) {
        return children.contains(child);
    }

    public Overlay get(int index) {
        return children.get(index);
    }

    public void remove(Overlay child) {
        children.remove(child);
    }

    public void remove(Overlay... children) {
        remove(Arrays.asList(children));
    }

    public void remove(Collection<? extends Overlay> children) {
        this.children.removeAll(children);
    }

    public void clear() {
        children.clear();
    }

    public Overlay top() {
        if (children.size() == 0)
            return null;

        return children.get(children.size() - 1);
    }

    @Override
    protected void onOutdated(GL11 gl) {
        for (Overlay child : children)
            child.onOutdated(gl);
    }

    public int size() {
        return children.size();
    }

    private void setupTouchDispatcher() {

        OnTouchListener TouchEventDispatcher = new OnTouchListener() {
            @Override
            public boolean onTouch(Overlay drawable, MotionEvent event) {

                for (Overlay child : children) {
                    if (onDispatchTouch(child, event))
                        return true;
                }

                return false;
            }
        };
        setOnTouchListener(TouchEventDispatcher);
    }

    protected boolean onDispatchTouch(Overlay child, MotionEvent event) {
        return child.onTouchEvent(event);
    }

    public List<Overlay> getChildren() {
        return children;
    }

    private List<Overlay> children;
}
