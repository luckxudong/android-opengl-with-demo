package ice.graphic.texture;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.opengl.GLUtils;
import android.util.Log;
import ice.graphic.GlRes;
import ice.graphic.gl_status.GlStatusController;
import ice.node.Overlay;
import ice.res.Res;

import javax.microedition.khronos.opengles.GL11;
import java.util.HashMap;
import java.util.Map;

import static javax.microedition.khronos.opengles.GL11.*;

/**
 * 在GL2.0以下版本如果硬件支持GL_APPLE_texture_2D_limited_npot，就无需考虑纹理宽高 POT的问题.
 */
public class Texture implements GlStatusController, GlRes {

    public static final int MAX_TEXTURE_SIZE = 1024;

    private static boolean p_o_tSupported;

    private static final String TAG = Texture.class.getSimpleName();

    public static void init(boolean p_o_tSupported) {
        Texture.p_o_tSupported = p_o_tSupported;
        Log.w(TAG, "init p_o_tSupported ? " + p_o_tSupported);
    }

    public static boolean isP_o_tSupported() {
        return p_o_tSupported;
    }

    public static class Params {
        public static final Params LINEAR_REPEAT;

        public static final Params LINEAR_CLAMP_TO_EDGE;

        static {
            LINEAR_REPEAT = new Params();
            LINEAR_REPEAT.add(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            LINEAR_REPEAT.add(GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            LINEAR_REPEAT.add(GL_TEXTURE_WRAP_S, GL_REPEAT);
            LINEAR_REPEAT.add(GL_TEXTURE_WRAP_T, GL_REPEAT);

            LINEAR_CLAMP_TO_EDGE = new Params();
            LINEAR_CLAMP_TO_EDGE.add(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            LINEAR_CLAMP_TO_EDGE.add(GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            LINEAR_CLAMP_TO_EDGE.add(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            LINEAR_CLAMP_TO_EDGE.add(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        }

        public Params() {
            paramMap = new HashMap<Integer, Integer>();
        }

        public void add(int pName, int value) {
            paramMap.put(pName, value);
        }

        public Map<Integer, Integer> getParamMap() {
            return paramMap;
        }

        private Map<Integer, Integer> paramMap;
    }

    private static boolean attachStatues;


    public Texture(int bitmapId) {
        this(Res.getBitmap(bitmapId));
    }

    public Texture(Bitmap bitmap) {
        this(bitmap, Params.LINEAR_CLAMP_TO_EDGE);
    }

    public Texture(Bitmap bitmap, Params params) {
        maxU = 1;
        maxV = 1;
        setBitmap(bitmap);
        this.params = params;
    }

    @Override
    public void attach(GL11 gl) {

        if (attachStatues)
            throw new IllegalStateException("Texture resource should be unattached before !");

        gl.glEnable(GL_TEXTURE_2D);

        if (!coordSupliedBySystem) {
            gl.glEnableClientState(GL_TEXTURE_COORD_ARRAY);
            //An advantage of representing particles with point sprites is that texture coordinate generation can be handled by the system
            //gl.glTexEnvi(GL_POINT_SPRITE_OES, GL_COORD_REPLACE_OES, GL_TRUE);
        }

        if (buffer != null) {        //gl.glIsBuffer(buffer[0])

            if (reload) {
                reload = false;
                rebind(gl);
            }
            else {
                gl.glBindTexture(GL_TEXTURE_2D, buffer[0]);
            }

        }
        else {
            buffer = new int[1];
            gl.glGenTextures(buffer.length, buffer, 0);
            rebind(gl);
        }

        if (modifier != null && !modifier.isFinished()) {

            boolean changed = modifier.step(bitmap);

            if (changed) {
                postSubData(modifier.getXOffset(), modifier.getYOffset(), modifier.getResult());
            }

        }

        if (subProvider != null) {
            GLUtils.texSubImage2D(GL_TEXTURE_2D, 0, xOffset, yOffset, subProvider);
            subProvider = null;
        }

        attachStatues = true;
    }

    @Override
    public boolean detach(GL11 gl, Overlay overlay) {
        if (!attachStatues)
            throw new IllegalStateException("Texture resource not attached before !");

        gl.glDisable(GL_TEXTURE_2D);

        if (!coordSupliedBySystem)
            gl.glDisableClientState(GL_TEXTURE_COORD_ARRAY);

        attachStatues = false;

        return true;
    }

    private void rebind(GL11 gl) {
        gl.glBindTexture(GL_TEXTURE_2D, buffer[0]);
        bindTextureParams(gl, params);
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, GLUtils.getInternalFormat(bitmap), bitmap, 0);
        //GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
    }

    @Override
    public void release(GL11 gl) {
        if (buffer != null && gl.glIsBuffer(buffer[0])) {
            gl.glDeleteTextures(buffer.length, buffer, 0);
            buffer = null;
        }
    }

    public void postSubData(int xoffset, int yoffset, Bitmap subPixel) {
        while (this.subProvider != null) {
            try {
                Thread.sleep(1);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.xOffset = xoffset;
        this.yOffset = yoffset;
        this.subProvider = subPixel;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    private void bindTextureParams(GL11 gl, Params params) {
        Map<Integer, Integer> paramMap = params.getParamMap();
        for (int pName : paramMap.keySet()) {
            gl.glTexParameterf(GL_TEXTURE_2D, pName, paramMap.get(pName));
        }
    }

    public Bitmap setBitmap(Bitmap bitmap) {
        if (bitmap == null)
            throw new IllegalArgumentException("bitmap null !");

        Bitmap newBitmap = tryAdjust(bitmap);

        if (this.bitmap != null) {

            if (this.bitmap.getWidth() != newBitmap.getWidth() || this.bitmap.getHeight() != newBitmap.getHeight()) {
                this.bitmap = newBitmap;
                reload = true;
            }
            else {
                postSubData(0, 0, newBitmap);
            }

        }

        this.bitmap = newBitmap;

        return this.bitmap;
    }

    public void setCoordSupliedBySystem(boolean coordSupliedBySystem) {
        this.coordSupliedBySystem = coordSupliedBySystem;
    }

    public Bitmap tryAdjust(Bitmap bitmap) {
        if (p_o_tSupported) return bitmap;

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();

        int widthToFit = originalWidth;
        int heightToFit = originalHeight;

        if ((widthToFit != 1) && (widthToFit & (widthToFit - 1)) != 0) {
            int i = 1;
            while (i < widthToFit)
                i *= 2;
            widthToFit = i;
        }

        if ((heightToFit != 1) && (heightToFit & (heightToFit - 1)) != 0) {
            int i = 1;
            while (i < heightToFit)
                i *= 2;
            heightToFit = i;
        }

        float scale = 1;
        while (widthToFit > MAX_TEXTURE_SIZE || heightToFit > MAX_TEXTURE_SIZE) {
            widthToFit /= 2;
            heightToFit /= 2;
            scale /= 2.0f;
        }

        if (widthToFit != originalWidth || heightToFit != originalHeight) {
            Bitmap adjustedBitmap = Bitmap.createBitmap(widthToFit, heightToFit, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(adjustedBitmap);
            if (scale != 1)
                canvas.scale(scale, scale);

            canvas.drawBitmap(bitmap, 0, 0, new Paint());

            maxU = (originalWidth * scale) / widthToFit;
            maxV = (originalHeight * scale) / heightToFit;

            return adjustedBitmap;
        }

        return bitmap;
    }

    public void setParams(Params params) {
        if (this.params != params) {
            this.params = params;
            reload = true;
        }
    }

    public float getMaxU() {
        return maxU;
    }

    public float getMaxV() {
        return maxV;
    }

    public void setModifier(TextureModifier modifier) {
        this.modifier = modifier;
    }

    private boolean reload;

    private TextureModifier modifier;

    private float maxU, maxV;

    private boolean coordSupliedBySystem;
    private int xOffset, yOffset;
    private int[] buffer;
    private Bitmap bitmap;
    private Bitmap subProvider;
    private Params params;
}
