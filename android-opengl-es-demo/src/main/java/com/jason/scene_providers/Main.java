package com.jason.scene_providers;

import com.jason.scenes.MainScene;
import ice.engine.Scene;
import ice.engine.SceneProvider;

/**
 * User: ice
 * Date: 12-1-6
 * Time: 下午5:04
 */
public class Main extends SceneProvider {

    public Main() {
        scene = new MainScene();
    }

    @Override
    protected boolean isEntry() {
        return true;
    }

    @Override
    protected Scene getScene() {
        return scene;
    }

    private Scene scene;
}
