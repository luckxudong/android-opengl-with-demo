package ice.practical;

import android.view.animation.AnimationUtils;
import ice.animation.Interpolator.AccelerateDecelerateInterpolator;
import ice.animation.Interpolator.Interpolator;
import ice.graphic.gl_status.ScissorController;
import ice.model.Point3F;
import ice.node.widget.TextOverlay;

import javax.microedition.khronos.opengles.GL11;

/**
 * User: jason
 * Date: 12-2-6
 * Time: 上午11:47
 */
public class ComesMoreText extends TextOverlay {

    private static Interpolator interpolator = new AccelerateDecelerateInterpolator();

    public ComesMoreText(int width, int height, long during) {
        super(width, height);
        this.during = during;

        addGlStatusController(controller = new ScissorController());
    }

    @Override
    protected void onDraw(GL11 gl) {

        long current = AnimationUtils.currentAnimationTimeMillis();

        if (startStamp == 0)
            startStamp = current;

        long sub = current - startStamp;

        finished = sub > during;

        if (!finished) {

            float interpolatedTime = interpolator.getInterpolation(sub / (float) during);

            currentScissor = (int) (getRealWidth() * interpolatedTime) + 1;

            Point3F absolutePos = getAbsolutePos();

            float height = getHeight();

            controller.set((int) (absolutePos.x - getWidth() / 2), (int) (absolutePos.y - height / 2), currentScissor, (int) height);
        }
        else {
            removeGlStatusController(controller);
        }

        super.onDraw(gl);
    }

    public boolean isFinished() {
        return finished;
    }

    private ScissorController controller;

    private long during;
    private long startStamp;
    private boolean finished;
    private int currentScissor;
}
