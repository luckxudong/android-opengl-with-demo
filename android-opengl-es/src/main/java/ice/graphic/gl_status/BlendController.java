package ice.graphic.gl_status;

import ice.node.Overlay;
import ice.util.GlUtil;

import javax.microedition.khronos.opengles.GL11;

import static javax.microedition.khronos.opengles.GL11.*;

/**
 * User: jason
 * Date: 12-2-21
 * Time: 上午10:50
 */
public class BlendController implements GlStatusController {

    /**
     * 关闭混合
     */
    public BlendController() {
    }

    /**
     * 开启混合
     *
     * @param blend_S
     * @param factor_D
     */
    public BlendController(int blend_S, int factor_D) {
        blend = true;
        this.factorS = blend_S;
        this.factorD = factor_D;
    }


    @Override
    public void attach(GL11 gl) {
        originalBlend = GlUtil.isEnabled(gl, GL_BLEND);
        originalFactorS = GlUtil.getInteger(gl, GL_BLEND_SRC);
        originalFactorD = GlUtil.getInteger(gl, GL_BLEND_DST);

        if (blend) {
            gl.glEnable(GL_BLEND);
            gl.glBlendFunc(factorS, factorD);
        }
        else {
            gl.glDisable(GL_BLEND);
        }
    }

    @Override
    public boolean detach(GL11 gl, Overlay overlay) {
        if (originalBlend) {
            gl.glEnable(GL_BLEND);
        }
        else {
            gl.glDisable(GL_BLEND);
        }

        gl.glBlendFunc(originalFactorS, originalFactorD);

        return true;
    }

    private boolean blend;
    private boolean originalBlend;
    private int factorS, factorD;
    private int originalFactorS, originalFactorD;
}
