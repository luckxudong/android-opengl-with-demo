package ice.animation;

import ice.node.Overlay;

import javax.microedition.khronos.opengles.GL11;

public class RotateAnimation extends Animation {

    public RotateAnimation(long duration, float toAngle) {
        this(duration, 0, toAngle);
    }

    public RotateAnimation(long duration, float fromAngle, float toAngle) {
        super(duration);
        this.fromAngle = fromAngle;
        this.toAngle = toAngle;
        axleZ = 1;
    }


    public void setRotateVector(float rotateX, float rotateY, float rotateZ) {
        this.axleX = rotateX;
        this.axleY = rotateY;
        this.axleZ = rotateZ;
    }

    public void setCenterOffset(float translateX, float translateY, float translateZ) {
        this.translateX = translateX;
        this.translateY = translateY;
        this.translateZ = translateZ;
    }

    @Override
    protected void applyFillAfter(Overlay overlay) {
        overlay.setRotate(toAngle, axleX, axleY, axleZ);
    }

    @Override
    protected void onAttach(GL11 gl, float interpolatedTime) {

        boolean offset = translateX != 0 || translateY != 0 || translateZ != 0;

        if (offset)
            gl.glTranslatef(translateX, translateY, translateZ);

        float angle = fromAngle + ((toAngle - fromAngle) * interpolatedTime);
        gl.glRotatef(angle, axleX, axleY, axleZ);

        if (offset)
            gl.glTranslatef(-translateX, -translateY, -translateZ);
    }

    private float translateX, translateY, translateZ;

    private float fromAngle, toAngle;
    private float axleX, axleY, axleZ;
}
