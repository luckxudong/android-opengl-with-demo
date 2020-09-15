package ice.practical;

import android.view.MotionEvent;
import ice.node.Overlay;

/**
 * 点击跟随
 * User: jason
 * Date: 12-2-3
 * Time: 下午3:49
 */
public class GoAfterTouchListener implements Overlay.OnTouchListener {

    @Override
    public boolean onTouch(Overlay overlay, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        if (!overlay.hitTest(x, y))
            return false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                overlay.setPos(overlay.getPosX() + x - lastX, overlay.getPosY() + y - lastY);
                lastX = x;
                lastY = y;
                return true;
        }
        return false;
    }

    private int lastX;
    private int lastY;
}
