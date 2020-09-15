package ice.engine;


import ice.node.OverlayParent;

/**
 * User: ice
 * Date: 11-11-14
 * Time: 上午10:41
 */
public class Scene extends OverlayParent {

    public Scene() {
        this(EngineContext.getAppWidth(), EngineContext.getAppHeight());
    }

    public Scene(int width, int height) {
        this.width = width;
        this.height = height;
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private int width, height;


}
