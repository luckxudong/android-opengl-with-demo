package ice.animation;

import ice.node.Overlay;

import javax.microedition.khronos.opengles.GL11;

/**
 * 依赖 源因子GL_SRC_ALPHA实现，注意Drawable的混合因子设置
 */
public class ColorAnimation extends Animation {

    public ColorAnimation(long duration, float[] fromColor, float[] toColor) {
        super(duration);

        this.fromColor = fromColor;
        this.toColor = toColor;
    }

    @Override
    protected void applyFillAfter(Overlay overlay) {
        overlay.setColors(toColor);
    }

    @Override
    protected void onAttach(GL11 gl, float interpolatedTime) {
//        gl.glEnable(GL_BLEND);
//        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//        //gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

        float r = fromColor[0] + ((toColor[0] - fromColor[0]) * interpolatedTime);
        float g = fromColor[1] + ((toColor[1] - fromColor[1]) * interpolatedTime);
        float b = fromColor[2] + ((toColor[2] - fromColor[2]) * interpolatedTime);
        float a = fromColor[3] + ((toColor[3] - fromColor[3]) * interpolatedTime);

        gl.glColor4f(r, g, b, a);
    }

    @Override
    protected void onDetach(Overlay overlay, GL11 gl) {
        // gl.glDisable(GL_BLEND);

        gl.glColor4f(1, 1, 1, 1);
    }

    private float[] fromColor, toColor;
}
