package ice.practical;

import android.graphics.Color;
import ice.node.widget.TextOverlay;

import javax.microedition.khronos.opengles.GL11;

/**
 * User: jason
 * Date: 12-2-10
 * Time: 下午5:25
 */
public class Fps extends TextOverlay {

    public Fps() {
        super(100, 30);
        setPos(getWidth() / 2, getHeight() / 2);

        setColor(Color.BLUE);
    }

    @Override
    public void draw(GL11 gl) {

        fps++;

        if (System.currentTimeMillis() - lastUpdate > 1000) {
            setText("fps : " + fps, (int) getHeight());
            fps = 0;
            lastUpdate = System.currentTimeMillis();
        }

        super.draw(gl);
    }


    private int fps;
    private long lastUpdate;

}
