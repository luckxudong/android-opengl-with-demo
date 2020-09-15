package ice.graphic.gl_status;

import ice.node.Overlay;

import javax.microedition.khronos.opengles.GL11;

import static javax.microedition.khronos.opengles.GL10.GL_SCISSOR_TEST;

/**
 * User: jason
 * Date: 12-2-21
 * Time: 下午12:15
 */
public class ScissorController implements GlStatusController {

    public ScissorController() {
    }

    public ScissorController(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void attach(GL11 gl) {
        //originalScissor = GlUtil.isEnabled(gl, GL_SCISSOR_TEST);
        if (width > 0 || height > 0) {
            gl.glEnable(GL_SCISSOR_TEST);
            gl.glScissor(x, y, width, height);
        }
    }

    @Override
    public boolean detach(GL11 gl, Overlay overlay) {
        gl.glDisable(GL_SCISSOR_TEST);
        return true;
    }

    public void set(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    private int x, y, width, height;
}
