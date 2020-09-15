package ice.graphic.projection;

import android.opengl.GLU;

import javax.microedition.khronos.opengles.GL11;

import static javax.microedition.khronos.opengles.GL11.GL_MODELVIEW;
import static javax.microedition.khronos.opengles.GL11.GL_PROJECTION;

/**
 * User: Jason
 * Date: 11-12-3
 * Time: 下午5:43
 */
public class OrthoProjection implements Projection {

    public OrthoProjection(GLU glu) {
        this.glu = glu;
    }

    @Override
    public void setUp(GL11 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL_PROJECTION);
        gl.glLoadIdentity();


        glu.gluOrtho2D(
                gl,
                0,
                width,
                0,
                height
        );

        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    private GLU glu;
}
