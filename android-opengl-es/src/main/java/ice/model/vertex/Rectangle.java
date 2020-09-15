package ice.model.vertex;

import ice.node.Overlay;

import javax.microedition.khronos.opengles.GL11;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static ice.model.Constants.BYTE_OF_FLOAT;
import static javax.microedition.khronos.opengles.GL10.*;

/**
 * User: jason
 * Date: 12-2-23
 * Time: 下午12:05
 */
public class Rectangle implements VertexData {

    private static final ByteBuffer CCW_INDICES;
    private static final ByteBuffer CW_INDICES;

    static {
        CCW_INDICES = ByteBuffer.allocateDirect(6);
        CW_INDICES = ByteBuffer.allocateDirect(6);

        CCW_INDICES.put(
                new byte[]{
                        0, 1, 2,
                        2, 3, 0
                }
        );

        CW_INDICES.put(
                new byte[]{
                        0, 3, 2,
                        2, 1, 0
                }
        );
    }

    public Rectangle(float width, float height) {
        this(width, height, true);
    }

    public Rectangle(float width, float height, boolean ccw) {
        setVertexCoord(width, height);
        this.ccw = ccw;
    }

    @Override
    public void attach(GL11 gl) {
        vertexCoord.position(0);
        gl.glEnableClientState(GL_VERTEX_ARRAY);
        gl.glVertexPointer(2, GL_FLOAT, 0, vertexCoord);

        if (textureCoord != null) {
            textureCoord.position(0);
            gl.glEnableClientState(GL_TEXTURE_COORD_ARRAY);
            gl.glTexCoordPointer(2, GL_FLOAT, 0, textureCoord);
        }

    }

    @Override
    public void onDrawVertex(GL11 gl) {
        ByteBuffer indices = ccw ? CCW_INDICES : CW_INDICES;

        indices.position(0);
        gl.glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_BYTE, indices);
    }

    @Override
    public boolean detach(GL11 gl, Overlay overlay) {
        gl.glDisableClientState(GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        return true;
    }

    @Override
    public void release(GL11 gl) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setVertexCoord(float width, float height) {
        ByteBuffer vbb = ByteBuffer.allocateDirect(BYTE_OF_FLOAT * 2 * 4);
        vbb.order(ByteOrder.nativeOrder());
        FloatBuffer coord = vbb.asFloatBuffer();

        float halfWidth = width / 2;
        float halfHeight = height / 2;

        float[] fourPoints = new float[]{
                -halfWidth, +halfHeight,
                -halfWidth, -halfHeight,
                +halfWidth, -halfHeight,
                +halfWidth, +halfHeight
        };

        coord.put(fourPoints);

        vertexCoord = coord;
    }

    public void setTextureCoord(float uRight, float vBottom) {
        setTextureCoord(0, uRight, 0, vBottom);
    }

    public void setTextureCoord(float uLeft, float uRight, float vTop, float vBottom) {
        ByteBuffer vbb = ByteBuffer.allocateDirect(BYTE_OF_FLOAT * 2 * 4);
        vbb.order(ByteOrder.nativeOrder());
        FloatBuffer coord = vbb.asFloatBuffer();

        coord.put(
                new float[]{
                        uLeft, vTop,
                        uLeft, vBottom,
                        uRight, vBottom,
                        uRight, vTop
                }
        );

        textureCoord = coord;
    }

    public float getWidth() {
        return Math.abs(vertexCoord.get(0)) * 2;
    }

    public float getHeight() {
        return Math.abs(vertexCoord.get(1)) * 2;
    }

    private boolean ccw;
    private FloatBuffer vertexCoord;
    private FloatBuffer textureCoord;
}
