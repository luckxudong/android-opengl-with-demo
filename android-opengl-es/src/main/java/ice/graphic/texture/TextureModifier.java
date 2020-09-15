package ice.graphic.texture;

import android.graphics.Bitmap;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

/**
 * User: jason
 * Date: 12-2-16
 * Time: 下午12:16
 */
public abstract class TextureModifier {

    protected TextureModifier(long duration) {
        this.duration = duration;
        interpolator = new AccelerateDecelerateInterpolator();
    }

    public void reset() {
        startTime = 0;
        stoped = false;
    }

    public void stop() {
        stoped = true;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    /**
     * step.
     *
     * @param originalPixels
     * @return changed true  ,not changed false
     */
    boolean step(Bitmap originalPixels) {

        if (startTime == 0) {
            startTime = AnimationUtils.currentAnimationTimeMillis();
        }

        long currentTime = AnimationUtils.currentAnimationTimeMillis();
        if (currentTime - startTime > duration)
            return false;

        int width = originalPixels.getWidth();
        int height = originalPixels.getHeight();

        if (result == null)
            result = Bitmap.createBitmap(width, height, originalPixels.getConfig());


        float normalizedTime = ((float) (currentTime - startTime)) / duration;

        float interpolatedTime = interpolator.getInterpolation(normalizedTime);// 根据归一化时间调整时间插值

        return onStep(interpolatedTime, originalPixels);
    }

    protected abstract boolean onStep(float interpolatedTime, Bitmap originalPixels);

    boolean isFinished() {
        if (stoped) return true;

        if (startTime == 0) return false;

        return AnimationUtils.currentAnimationTimeMillis() - startTime > duration;
    }

    int getXOffset() {
        return xOffset;
    }

    int getYOffset() {
        return yOffset;
    }

    Bitmap getResult() {
        return result;
    }

    private boolean stoped;
    protected long duration;
    protected Bitmap result;
    protected long startTime;
    protected int xOffset, yOffset;
    private Interpolator interpolator;

}
