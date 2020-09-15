package ice.engine;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLU;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import ice.graphic.GlRenderer;
import ice.graphic.projection.PerspectiveProjection;
import ice.node.Overlay;
import ice.res.Res;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * User: ice
 * Date: 12-1-6
 * Time: 下午3:09
 */
public abstract class Game extends Activity implements App {
    private static final String TAG = Game.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");

        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();

        EngineContext.build(this);
        Res.built(this);
        Overlay.resetId();

        providerStack = new Stack<Class<? extends SceneProvider>>();
        providerCache = new HashMap<Class<? extends SceneProvider>, SoftReference<SceneProvider>>();

        setContentView(gameView = buildGameView());

        Class<? extends SceneProvider> entryProvider = getEntry();//启动时，保证是主界面的入口
        providerStack.push(entryProvider);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

        new Thread() {
            @Override
            public void run() {

                gameView.getRenderer().waitUntilInited();

                Class<? extends SceneProvider> topProviderClass = providerStack.peek();

                topProvider = findFromCache(topProviderClass);

                if (topProvider == null) {
                    topProvider = buildInstance(topProviderClass);
                    topProvider.onCreate();
                }

                topProvider.onResume();
                gameView.showScene(topProvider.getScene());

            }

        }.start();

    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i(TAG, "onPause");
        topProvider.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.i(TAG, "onStop");
        topProvider.onStop();
    }

    protected abstract Class<? extends SceneProvider> getEntry();

    @Override
    public AppView getRender() {
        return gameView;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            handBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private synchronized void handBack() {
        Class<? extends SceneProvider> topProviderClass = providerStack.peek();

        SceneProvider topInstance = findFromCache(topProviderClass);

        if (!topInstance.onBackPressed())
            back();

    }

    @Override
    public synchronized void intent(Class<? extends SceneProvider> to) {
        intent(to, null);
    }

    @Override
    public void intent(Class<? extends SceneProvider> to, Object msg) {
        topProvider.onPause();

        SceneProvider toProvider = findFromCache(to);
        if (toProvider == null) {
            toProvider = buildInstance(to);
            toProvider.setIntentMsg(msg);
            toProvider.onCreate();
        }

        toProvider.onResume();
        topProvider = toProvider;

        if (toProvider.isEntry())
            providerStack.clear();

        providerStack.push(to);

        switchToScene(toProvider);
    }

    private void switchToScene(SceneProvider sceneProvider) {
        gameView.switchScene(sceneProvider.getScene());
    }

    @Override
    public void exit() {
        finish();
    }

    protected GameView buildGameView() {

        return new GameView(this) {

            @Override
            protected GlRenderer onCreateGlRenderer() {

                PerspectiveProjection projection = new PerspectiveProjection(new GLU(), 60);

                return new GlRenderer(projection);
            }
        };
    }

    private SceneProvider buildInstance(Class<? extends SceneProvider> providerClass) {

        SceneProvider providerInstance = null;
        try {
            providerInstance = providerClass.newInstance();
            providerCache.put(providerClass, new SoftReference<SceneProvider>(providerInstance));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return providerInstance;
    }

    private SceneProvider findFromCache(Class<? extends SceneProvider> providerClass) {

        SoftReference<SceneProvider> cache = providerCache.get(providerClass);

        if (cache != null) {
            SceneProvider instance = cache.get();

            if (instance != null)
                return instance;

        }

        return null;
    }

    private synchronized void back() {
        Log.i(TAG, "back");

        if (providerStack.size() <= 1) {
            exit();
            return;
        }

        Class<? extends SceneProvider> currentProviderClass = providerStack.pop();

        topProvider = findFromCache(currentProviderClass);

        if (topProvider.isEntry()) {
            providerStack.clear();
            exit();
            return;
        }

        Class<? extends SceneProvider> topProviderClass = providerStack.peek();
        SceneProvider nextProvider = findFromCache(topProviderClass);
        if (nextProvider == null) {
            nextProvider = buildInstance(topProviderClass);
            nextProvider.onCreate();
        }

        topProvider.onPause();
        nextProvider.onResume();
        topProvider = nextProvider;
        switchToScene(nextProvider);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    private int width, height;

    private GameView gameView;

    private SceneProvider topProvider;
    private Stack<Class<? extends SceneProvider>> providerStack;
    private Map<Class<? extends SceneProvider>, SoftReference<SceneProvider>> providerCache;
}
