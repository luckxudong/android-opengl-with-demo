package ice.graphic.gl_status;

import android.graphics.Color;
import ice.node.Overlay;

import javax.microedition.khronos.opengles.GL11;

/**
 * User: jason
 * Date: 12-2-21
 * Time: 上午11:21
 */
public class ColorController implements GlStatusController {

    public ColorController() {
        this(1, 1, 1, 1);
    }

    public ColorController(float r, float g, float b, float a) {
        this.color = new float[]{r, g, b, a};
    }

    public ColorController(float[] color) {
        this.color = color;
    }

    @Override
    public void attach(GL11 gl) {
        if (color != null)
            gl.glColor4f(color[0], color[1], color[2], color[3]);
    }

    @Override
    public boolean detach(GL11 gl, Overlay overlay) {
        if (color != null)   //TODO 先这样吧
            gl.glColor4f(1, 1, 1, 1);

        return true;
    }

    public void setColor(float[] color) {
        this.color = color;
    }

    public void setColor(int color) {
        this.color = new float[]{
                Color.red(color),
                Color.green(color),
                Color.blue(color),
                Color.alpha(color)
        };
    }

    private float[] color;
}
