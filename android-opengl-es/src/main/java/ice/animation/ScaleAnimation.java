package ice.animation;

import ice.node.Overlay;

import javax.microedition.khronos.opengles.GL11;

public class ScaleAnimation extends Animation {

    public ScaleAnimation(long duration, float toX, float toY) {
        this(duration, 1, toX, 1, toY, 1, 1);
    }

    public ScaleAnimation(long duration, float fromX, float toX, float fromY, float toY) {
        this(duration, fromX, toX, fromY, toY, 1, 1);
    }

    public ScaleAnimation(long duration, float fromX, float toX, float fromY, float toY, int fromZ, float toZ) {
        super(duration);
        this.fromX = fromX;
        this.toX = toX;
        this.fromY = fromY;
        this.toY = toY;
        this.fromZ = fromZ;
        this.toZ = toZ;
    }

    @Override
    protected void applyFillAfter(Overlay overlay) {
        overlay.setScale(toX, toY, toZ);
    }

    @Override
    protected void onAttach(GL11 gl, float interpolatedTime) {

        float scaleX = fromX;
        float scaleY = fromY;
        float scaleZ = fromZ;

        if (fromX != toX)
            scaleX = fromX + ((toX - fromX) * interpolatedTime);

        if (fromY != toY)
            scaleY = fromY + ((toY - fromY) * interpolatedTime);

        if (fromZ != toZ)
            scaleY = fromZ + ((toZ - fromZ) * interpolatedTime);

        if (scaleX != 1 || scaleY != 1 || scaleZ != 1)
            gl.glScalef(scaleX, scaleY, scaleZ);
    }

    private float fromX = 1, fromY = 1, fromZ = 1;
    private float toX = 1, toY = 1, toZ = 1;
}
