package ice.animation;

import android.view.animation.AnimationUtils;
import ice.animation.Interpolator.AccelerateDecelerateInterpolator;
import ice.animation.Interpolator.Interpolator;
import ice.graphic.gl_status.GlStatusController;
import ice.node.Overlay;

import javax.microedition.khronos.opengles.GL11;

public abstract class Animation implements GlStatusController {

    public interface Listener {

        void onAnimationEnd(Overlay overlay);
    }

    private static final long NOT_STARTED = 0;

    public Animation(long duration) {
        this.duration = duration;
        interpolator = new AccelerateDecelerateInterpolator();
        startTime = NOT_STARTED;
    }

    protected void start() {
        finished = false;
        startTime = AnimationUtils.currentAnimationTimeMillis();
    }

    public void cancel() {
        cancel = true;
    }

    @Override
    public void attach(GL11 gl) {

        if (startTime == 0)
            start();

        long currentTime = AnimationUtils.currentAnimationTimeMillis();

        boolean over = currentTime - startTime > duration;

        if (over) {
            if (loop) {
                startTime = currentTime;
            }
            else {
                finished = true;
            }
        }

        float normalizedTime = 0;

        if (over) {
            normalizedTime = 1.0f;
        }
        else {
            if (duration != 0 && currentTime >= startTime) {
                normalizedTime = ((float) (currentTime - startTime)) / (float) duration;
            }
        }

        //根据归一化时间调整时间插值
        float interpolatedTime = interpolator.getInterpolation(normalizedTime);

        onAttach(gl, interpolatedTime);
    }

    @Override
    public boolean detach(GL11 gl, Overlay overlay) {

        onDetach(overlay, gl);

        if (isCompleted()) {
            onComplete(overlay, gl);
            return false;
        }

        return !isCanceled();
    }


    public void onComplete(final Overlay overlay, GL11 gl) {

        if (fillAfter)
            applyFillAfter(overlay);

        if (listener != null)
            listener.onAnimationEnd(overlay);
    }

    protected abstract void applyFillAfter(Overlay overlay);

    protected abstract void onAttach(GL11 gl, float interpolatedTime);

    protected void onDetach(Overlay overlay, GL11 gl) {

    }

    public long getDuration() {
        return duration;
    }

    public boolean isCompleted() {
        return finished;
    }


    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loopEnabled) {
        this.loop = loopEnabled;
    }


    public boolean isCanceled() {
        return cancel;
    }

    public void setFillAfter(boolean fillAfter) {
        this.fillAfter = fillAfter;
    }

    private boolean finished;

    private boolean fillAfter = true;

    protected long startTime;
    protected long duration;

    protected boolean loop;

    private boolean cancel;

    private Interpolator interpolator;
    private Listener listener;
}
