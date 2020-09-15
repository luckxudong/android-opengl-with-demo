package ice.engine;

import android.content.Context;

public interface App {

    Context getContext();

    AppView getRender();

    int getWidth();

    int getHeight();

    void exit();

    void intent(Class<? extends SceneProvider> to);

    void intent(Class<? extends SceneProvider> toClass, Object msg);
}
