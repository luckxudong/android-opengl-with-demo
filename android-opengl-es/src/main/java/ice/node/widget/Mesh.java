package ice.node.widget;

import ice.graphic.texture.Texture;
import ice.model.vertex.VertexData;
import ice.node.Overlay;

import javax.microedition.khronos.opengles.GL11;

/**
 * User: ice
 * Date: 11-11-14
 * Time: 下午2:35
 */
public class Mesh extends Overlay {

    public Mesh() {
        this(null);
    }

    public Mesh(VertexData vertexData) {
        this(vertexData, null);
    }

    public Mesh(VertexData vertexData, Texture texture) {
        this.vertexData = vertexData;
        this.texture = texture;
    }

    @Override
    protected void onDraw(GL11 gl) {
        Texture theTexture = texture;
        boolean useTexture = (theTexture != null);

        if (useTexture)
            theTexture.attach(gl);

        vertexData.attach(gl);

        vertexData.onDrawVertex(gl);

        vertexData.detach(gl, this);

        if (useTexture) theTexture.detach(gl, this);
    }

    public VertexData getVertexData() {
        return vertexData;
    }

    public void setVertexData(VertexData vertexData) {
        this.vertexData = vertexData;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }

    @Override
    protected void onOutdated(GL11 gl) {
        if (texture != null)
            texture.release(gl);

        if (vertexData != null)
            vertexData.release(gl);
    }

    private Texture texture;
    private VertexData vertexData;
}
