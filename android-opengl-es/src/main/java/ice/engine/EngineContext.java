package ice.engine;

/**
 * User: ice
 * Date: 11-11-14
 * Time: 下午2:38
 */
public class EngineContext {

    private static EngineContext instance;

    public static void build(App app) {
        instance = new EngineContext(app);
    }

    public static EngineContext getInstance() {
        return instance;
    }

    public static int getAppWidth() {
        return instance.app.getWidth();
    }

    public static int getAppHeight() {
        return instance.app.getHeight();
    }

    private EngineContext(App app) {
        this.app = app;
    }

    public App getApp() {
        return app;
    }

    private App app;
}
