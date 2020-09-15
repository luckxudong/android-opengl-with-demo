package ice.animation;

import ice.node.Overlay;

import javax.microedition.khronos.opengles.GL11;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AnimationGroup extends Animation {

    public AnimationGroup() {
        super(0);
        children = new ArrayList<Animation>();
        childrenOffsetTime = new ArrayList<Long>(6);
    }

    public void add(Animation animation) {
        add(animation, 0L);
    }

    public void add(Animation animation, long offsetTime) {
        children.add(animation);
        childrenOffsetTime.add(offsetTime);
        duration = Math.max(duration, offsetTime + animation.duration);
    }

    @Override
    protected void start() {
        super.start();

        for (Animation animation : children) {
            animation.start();
        }
    }

    @Override
    protected void applyFillAfter(Overlay overlay) {
        for (Animation animation : children) {
            animation.applyFillAfter(overlay);
        }
    }

    @Override
    protected void onAttach(GL11 gl, float interpolatedTime) {
        for (Animation animation : children) {
            animation.onAttach(gl, interpolatedTime);
        }
    }

    @Override
    protected void onDetach(Overlay overlay, GL11 gl) {

        for (Iterator<Animation> iterator = children.iterator(); iterator.hasNext(); ) {

            Animation animation = iterator.next();

            animation.onDetach(overlay, gl);

            if (animation.isCompleted()) {
                iterator.remove();
                animation.onComplete(overlay, gl);
            }

        }
    }

    @Override
    public void setLoop(boolean loopEnabled) {
        super.setLoop(loopEnabled);
        for (Animation child : children) {
            child.setLoop(loopEnabled);
        }
    }

    private List<Animation> children;
    private List<Long> childrenOffsetTime;
}
