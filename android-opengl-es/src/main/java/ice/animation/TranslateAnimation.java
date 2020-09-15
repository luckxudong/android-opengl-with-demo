package ice.animation;

import ice.node.Overlay;

import javax.microedition.khronos.opengles.GL11;

public class TranslateAnimation extends Animation {

    public TranslateAnimation(long duration, float toXDelta, float toYDelta) {
        this(duration, toXDelta, toYDelta, 0);
    }

    public TranslateAnimation(long duration, float toXDelta, float toYDelta, float toZDelta) {
        super(duration);
        this.toXDelta = toXDelta;
        this.toYDelta = toYDelta;
        this.toZDelta = toZDelta;
    }

    public TranslateAnimation(long duration, float fromXDelta, float toXDelta, float fromYDelta, float toYDelta) {
        super(duration);
        this.fromXDelta = fromXDelta;
        this.toXDelta = toXDelta;
        this.fromYDelta = fromYDelta;
        this.toYDelta = toYDelta;
    }

    @Override
    protected void applyFillAfter(Overlay overlay) {
        if (toXDelta != 0 || toYDelta != 0 || toZDelta != 0) {
            overlay.setPos(
                    overlay.getPosX() + toXDelta,
                    overlay.getPosY() + toYDelta,
                    overlay.getPosZ() + toZDelta
            );
        }
    }

    @Override
    protected void onAttach(GL11 gl, float interpolatedTime) {

        float translateX = 0;
        float translateY = 0;
        float translateZ = 0;

        if (fromXDelta != toXDelta)
            translateX = fromXDelta + ((toXDelta - fromXDelta) * interpolatedTime);

        if (fromYDelta != toYDelta)
            translateY = fromYDelta + ((toYDelta - fromYDelta) * interpolatedTime);


        if (fromZDelta != toZDelta)
            translateZ = fromZDelta + ((toZDelta - fromZDelta) * interpolatedTime);

        if (translateX != 0 || translateY != 0 || translateZ != 0)
            gl.glTranslatef(translateX, translateY, translateZ);
    }

    private float fromXDelta, toXDelta;
    private float fromYDelta, toYDelta;
    private float fromZDelta, toZDelta;
}
