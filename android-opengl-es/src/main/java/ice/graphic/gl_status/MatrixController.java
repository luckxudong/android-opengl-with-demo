package ice.graphic.gl_status;

import ice.node.Overlay;

import javax.microedition.khronos.opengles.GL11;

/**
 * User: jason
 * Date: 12-2-21
 * Time: 上午11:28
 */
public class MatrixController implements GlStatusController {

    @Override
    public void attach(GL11 gl) {
        boolean posChanged = posX != 0 || posY != 0 || posZ != 0;

        boolean scaleChanged = scaleX != 1 || scaleY != 1 || scaleZ != 1;

        boolean rotateChanged = rotate != 0;

        matrixChange = posChanged || scaleChanged || rotateChanged;

        if (matrixChange) {
            gl.glPushMatrix();

            if (posChanged)
                gl.glTranslatef(posX, posY, posZ);

            if (scaleChanged)
                gl.glScalef(scaleX, scaleY, scaleZ);

            if (rotateChanged)
                gl.glRotatef(rotate, axleX, axleY, axleZ);
        }

    }

    @Override
    public boolean detach(GL11 gl, Overlay overlay) {
        if (matrixChange)
            gl.glPopMatrix();
        return true;
    }


    public void setPos(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void setPos(float posX, float posY, float posZ) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public float getPosZ() {
        return posZ;
    }

    public void setPosZ(float posZ) {
        this.posZ = posZ;
    }

    public void setScale(float scaleX, float scaleY, float scaleZ) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
    }

    public void setRotate(float rotate, float axleX, float axleY, float axleZ) {
        this.rotate = rotate;
        this.axleX = axleX;
        this.axleY = axleY;
        this.axleZ = axleZ;
    }

    private boolean matrixChange;

    private float posX, posY, posZ;
    private float scaleX = 1, scaleY = 1, scaleZ = 1;
    private float rotate;
    private float axleX = 1, axleY = 1, axleZ = 1;
}
