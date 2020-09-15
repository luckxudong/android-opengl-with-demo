package ice.node.widget;

import ice.graphic.texture.Texture;
import ice.model.Point3F;
import ice.model.vertex.Rectangle;

/**
 * User: jason
 * Date: 12-2-23
 * Time: 下午4:16
 */
public class Grid extends Mesh {

    public Grid(float width, float height) {
        this(width, height, true);
    }

    public Grid(float width, float height, boolean ccw) {
        this.width = width;
        this.height = height;

        setVertexData(new Rectangle(width, height, ccw));
    }

    public void setBounds(float width, float height) {
        Rectangle rectangle = (Rectangle) getVertexData();
        rectangle.setVertexCoord(width, height);
    }

    public void setTextureCoord(float uRight, float vBottom) {
        setTextureCoord(0, uRight, 0, vBottom);
    }

    public void setTextureCoord(float uLeft, float uRight, float vTop, float vBottom) {
        Rectangle rectangle = (Rectangle) getVertexData();
        rectangle.setTextureCoord(uLeft, uRight, vTop, vBottom);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    @Override
    public void setTexture(Texture texture) {
        super.setTexture(texture);
        setTextureCoord(texture.getMaxU(), texture.getMaxV());
    }

    @Override
    public boolean hitTest(float x, float y) {
        Point3F absolutePos = getAbsolutePos();
        float offsetX = x - absolutePos.x;
        float offsetY = y - absolutePos.y;

        float halfWidth = width / 2;
        float halfHeight = height / 2;

        return offsetX >= -halfWidth
                && offsetX <= halfWidth
                && offsetY >= -halfHeight
                && offsetY <= halfHeight;
    }

    private float width, height;
}
