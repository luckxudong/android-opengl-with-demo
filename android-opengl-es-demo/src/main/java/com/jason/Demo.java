package com.jason;

import com.jason.graphic.DemoView;
import com.jason.scene_providers.Main;

import ice.engine.Game;
import ice.engine.GameView;
import ice.engine.SceneProvider;

public class Demo extends Game {

    @Override
    protected Class<? extends SceneProvider> getEntry() {
        return Main.class;
    }

    @Override
    protected GameView buildGameView() {
        return new DemoView(this);
    }

}
