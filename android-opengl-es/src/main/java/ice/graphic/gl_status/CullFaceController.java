package ice.graphic.gl_status;

import ice.node.Overlay;
import ice.util.GlUtil;

import javax.microedition.khronos.opengles.GL11;

import static javax.microedition.khronos.opengles.GL10.*;
import static javax.microedition.khronos.opengles.GL11.GL_FRONT_FACE;

/**
 * User: jason
 * Date: 12-2-21
 * Time: 下午2:22
 */
public class CullFaceController implements GlStatusController {

    public enum FaceMode {
        Front, Back, BothSide
    }

    public CullFaceController(FaceMode faceMode) {
        this.faceMode = faceMode;
    }

    @Override
    public void attach(GL11 gl) {

        originalCullFace = gl.glIsEnabled(GL_CULL_FACE);
        originalFaceMode = GlUtil.getInteger(gl, GL_FRONT_FACE);

        switch (faceMode) {

            case Front:
                if (!originalCullFace)
                    gl.glEnable(GL_CULL_FACE);
                gl.glFrontFace(GL_CCW);
                break;

            case Back:
                if (!originalCullFace)
                    gl.glEnable(GL_CULL_FACE);
                gl.glFrontFace(GL_CW);
                break;

            case BothSide:
                gl.glDisable(GL_CULL_FACE);
                break;
        }


    }

    @Override
    public boolean detach(GL11 gl, Overlay overlay) {
        if (originalCullFace) {
            gl.glEnable(GL_CULL_FACE);
        }
        else {
            gl.glDisable(GL_CULL_FACE);
        }

        gl.glFrontFace(originalFaceMode);

        return true;
    }

    private boolean originalCullFace;
    private int originalFaceMode;
    private FaceMode faceMode;
}
