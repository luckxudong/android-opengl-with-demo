package ice.model;

/**
 * User: jason
 * Date: 12-2-21
 * Time: 下午3:58
 */
public class Size {

    public Size(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    private float width, height;
}
