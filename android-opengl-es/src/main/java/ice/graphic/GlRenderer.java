package ice.graphic;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;
import ice.engine.App;
import ice.engine.EngineContext;
import ice.graphic.projection.PerspectiveProjection;
import ice.graphic.projection.Projection;
import ice.graphic.texture.Texture;
import ice.node.Overlay;
import ice.node.OverlayParent;
import ice.practical.Fps;
import ice.util.GlUtil;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import static javax.microedition.khronos.opengles.GL10.*;

/**
 * User: ice
 * Date: 12-1-6
 * Time: 下午4:24
 */
public class GlRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "GlRenderer";

    public GlRenderer(Projection projection) {
        this.projection = projection;
        drawDispatcher = new OverlayParent();
        fps = new Fps();
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        Log.w(TAG, "onSurfaceCreated");

        GL11 gl = (GL11) gl10;

        /**设置一次就ok了*/
        gl.glClearDepthf(1.0f);
        gl.glClearColor(0, 0, 0, 1.0f);
        /**设置一次就ok了*/

        boolean textureP_O_T = GlUtil.queryExtension(gl, "GL_APPLE_texture_2D_limited_npot");

        Texture.init(textureP_O_T);

        gl.glEnable(GL_CULL_FACE);

        onInit(gl);

        inited = true;

        synchronized (this) {
            notify();
        }

        System.out.println("GL_RENDERER = " + gl.glGetString(GL_RENDERER));
        System.out.println("GL_VENDOR = " + gl.glGetString(GL_VENDOR));
        System.out.println("GL_VERSION = " + gl.glGetString(GL_VERSION));
        System.out.println("GL_EXTENSIONS = " + gl.glGetString(GL_EXTENSIONS));
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        projection.setUp((GL11) gl10, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GL11 gl = (GL11) gl10;

        onFrame(gl);

        drawDispatcher.draw(gl);

        fps.draw(gl);

        log(gl);
    }

    protected void onFrame(GL11 gl) {
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();

        App app = EngineContext.getInstance().getApp();

        float z = 0;

        if (projection instanceof PerspectiveProjection) { //移动z到窗口
            PerspectiveProjection perspectiveProjection = (PerspectiveProjection) projection;
            z = -0.1f - perspectiveProjection.getZFarOfWindow();
        }

        gl.glTranslatef(-(app.getWidth() / 2.0f), -(app.getHeight() / 2.0f), z);
    }

    public OverlayParent getDrawDispatcher() {
        return drawDispatcher;
    }

    public void waitUntilInited() {
        if (!inited) {
            synchronized (this) {
                try {
                    wait();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    protected void onInit(GL11 gl) {

    }

    private void log(GL11 gl) {
        int errorCode = gl.glGetError();

        if (errorCode != GL_NO_ERROR)
            throw new IllegalStateException(GLU.gluErrorString(errorCode));
    }

    //    private Scene newScene;
//    private Scene oldScene;
    private boolean inited;
    private Overlay fps;
    private Projection projection;
    private OverlayParent drawDispatcher;
}
