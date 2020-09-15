package ice.graphic.gl_status;

import ice.node.Overlay;
import ice.util.GlUtil;

import javax.microedition.khronos.opengles.GL11;

import static javax.microedition.khronos.opengles.GL10.GL_DEPTH_TEST;

/**
 * User: jason
 * Date: 12-2-21
 * Time: 上午10:26
 */
public class DepthController implements GlStatusController {

    public DepthController(boolean depthTest) {
        this.depthTest = depthTest;
    }

    @Override
    public void attach(GL11 gl) {

        originalDepthTest = GlUtil.isEnabled(gl, GL_DEPTH_TEST);

        if (originalDepthTest != depthTest) {

            if (depthTest) {
                gl.glEnable(GL_DEPTH_TEST);
            }
            else {
                gl.glDisable(GL_DEPTH_TEST);
            }

        }
    }

    @Override
    public boolean detach(GL11 gl, Overlay overlay) {

        if (originalDepthTest != depthTest) {

            if (originalDepthTest) {
                gl.glEnable(GL_DEPTH_TEST);
            }
            else {
                gl.glDisable(GL_DEPTH_TEST);
            }

        }

        return true;
    }

    private boolean originalDepthTest;
    private boolean depthTest;
}
