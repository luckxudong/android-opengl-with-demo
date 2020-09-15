package ice.node.widget;

import android.graphics.Color;
import ice.graphic.gl_status.ColorController;

/**
 * User: Jason
 * Date: 12-2-4
 * Time: 下午6:21
 */
public class ColorOverlay extends Grid {

    public ColorOverlay(int color, float width, float height) {
        super(width, height);

        controller = new ColorController(
                Color.red(color) / (float) 255,
                Color.green(color) / (float) 255,
                Color.blue(color) / (float) 255,
                Color.alpha(color) / (float) 255
        );

        addGlStatusController(controller);
    }

    private ColorController controller;
}
