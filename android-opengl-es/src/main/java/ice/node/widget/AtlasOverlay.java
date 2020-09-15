package ice.node.widget;

import ice.graphic.texture.Texture;

/**
 * User: jason
 * Date: 12-2-23
 * Time: 下午4:49
 */
public class AtlasOverlay extends BitmapOverlay {

    public AtlasOverlay(float width, float height, int splitU) {
        this(width, height, splitU, 1);
    }

    public AtlasOverlay(float width, float height, int splitU, int splitV) {
        super(width, height);

        this.splitU = splitU;
        this.splitV = splitV;
    }

    public void setAtlasIndex(int index, Texture texture) {
        setTexture(texture);

        float maxU = texture.getMaxU();
        float maxV = texture.getMaxV();

        float uLeft = (index % splitU) * maxU / splitU;
        float uRight = ((index % splitU) + 1) * maxU / splitU;

        float vTop = (index / splitU) * maxV / splitV;
        float vBottom = ((index / splitU) + 1) * maxV / splitV;

        setTextureCoord(uLeft, uRight, vTop, vBottom);
    }

    private int splitU, splitV;
}
