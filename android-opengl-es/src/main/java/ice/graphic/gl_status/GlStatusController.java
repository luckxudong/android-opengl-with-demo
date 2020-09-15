package ice.graphic.gl_status;

import ice.node.Overlay;

import javax.microedition.khronos.opengles.GL11;

/**
 * User: jason
 * Date: 12-2-21
 * Time: 上午10:25
 */
public interface GlStatusController {

    void attach(GL11 gl);

    /**
     * 恢复之前的状态.
     *
     * @param gl
     * @param overlay
     * @return effect More Frame  true else false
     */
    boolean detach(GL11 gl, Overlay overlay);

}
