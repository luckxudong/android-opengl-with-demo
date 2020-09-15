package ice.node;


import android.view.MotionEvent;
import ice.animation.Animation;
import ice.graphic.Camera;
import ice.graphic.gl_status.ColorController;
import ice.graphic.gl_status.GlStatusController;
import ice.graphic.gl_status.MatrixController;
import ice.model.Point3F;

import javax.microedition.khronos.opengles.GL11;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * User: ice
 * Date: 11-11-14
 * Time: 上午10:40
 */
public abstract class Overlay {

    public interface OnTouchListener {
        boolean onTouch(Overlay overlay, MotionEvent event);
    }

    private static long maxId;

    public synchronized static long requestId() {
        maxId++;

        if (maxId == Long.MAX_VALUE) {
            throw new IllegalStateException("ID reuse !");
        }

        return maxId;
    }

    public synchronized static void resetId() {
        maxId = 0;
    }

    public Overlay() {
        this(0, 0, 0);
    }

    public Overlay(float posX, float posY, float posZ) {
        visible = true;
        id = requestId();

        statusControllers = new ArrayList<GlStatusController>(3);
        statusControllers.add(new MatrixController());

        setPos(posX, posY, posZ);
    }

    protected void onOutdated(GL11 gl) {

    }

    public void draw(GL11 gl) {

        ensureStatusControllers();

        if (!visible) return;

        for (GlStatusController controller : statusControllers)
            controller.attach(gl);

        onDraw(gl);

        for (Iterator<GlStatusController> iterator = statusControllers.iterator(); iterator.hasNext(); ) {
            GlStatusController controller = iterator.next();

            boolean effectMoreFrame = controller.detach(gl, this);

            if (!effectMoreFrame) iterator.remove();
        }

    }

    private void ensureStatusControllers() {
        if (removeBuffer != null && removeBuffer.size() > 0) {
            statusControllers.removeAll(removeBuffer);
            removeBuffer.clear();
        }

        if (addBuffer != null && addBuffer.size() > 0) {
            statusControllers.addAll(addBuffer);
            addBuffer.clear();
        }
    }

    protected abstract void onDraw(GL11 gl);

    public void startAnimation(Animation animation) {

        if (this.animation == null) {
            this.animation = animation;
        }
        else {
            if (statusControllers.contains(this.animation))
                throw new IllegalStateException("Another animation not finished yet !");

            this.animation = animation;
        }

        addGlStatusController(animation);

        if (!visible)
            setVisible(true);
    }

    public void cancelAnimation() {
        animation.cancel();
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setRemovable(boolean removable) {
        this.removable = removable;
    }

    public boolean isRemovable() {
        return removable;
    }

    public boolean hitTest(float x, float y) {
        return false;
    }

    public boolean onTouchEvent(MotionEvent event) {

        return onTouchListener == null
                ? false
                : onTouchListener.onTouch(this, event);
    }

    public void setPos(float posX, float posY) {
        getMatrixController().setPos(posX, posY);
    }

    public void setPos(float posX, float posY, float posZ) {
        getMatrixController().setPos(posX, posY, posZ);
    }

    public float getPosX() {
        return getMatrixController().getPosX();
    }

    public float getPosY() {
        return getMatrixController().getPosY();
    }

    public float getPosZ() {
        return getMatrixController().getPosZ();
    }

    public void setPosZ(float posZ) {
        getMatrixController().setPosZ(posZ);
    }

    public void setColors(float[] color) {
        addGlStatusController(new ColorController(color));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Overlay overlay = (Overlay) o;

        if (id != overlay.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    public Point3F getAbsolutePos() {
        MatrixController matrixController = getMatrixController();

        Point3F absolute = new Point3F(
                matrixController.getPosX(),
                matrixController.getPosY(),
                matrixController.getPosZ()
        );

        if (parent != null) {
            Point3F parentAbsolutePos = parent.getAbsolutePos();
            absolute.x += parentAbsolutePos.x;
            absolute.y += parentAbsolutePos.y;
            absolute.z += parentAbsolutePos.z;
        }

        return absolute;
    }

    protected void setParent(OverlayParent parent) {
        this.parent = parent;
    }

    public void setScale(float scaleX, float scaleY, float scaleZ) {
        getMatrixController().setScale(scaleX, scaleY, scaleZ);
    }

    public void setRotate(float rotate, float axleX, float axleY, float axleZ) {
        getMatrixController().setRotate(rotate, axleX, axleY, axleZ);
    }

    public Animation getAnimation() {
        return animation;
    }

    public void addGlStatusController(GlStatusController controller) {
        if (addBuffer == null)
            addBuffer = new CopyOnWriteArrayList<GlStatusController>();

        addBuffer.add(controller);
    }

    public void removeGlStatusController(GlStatusController controller) {
        if (removeBuffer == null)
            removeBuffer = new CopyOnWriteArrayList<GlStatusController>();

        removeBuffer.add(controller);
    }

    public MatrixController getMatrixController() {
        return (MatrixController) statusControllers.get(0);
    }

    private Animation animation;

    private List<GlStatusController> addBuffer;

    private List<GlStatusController> removeBuffer;

    protected List<GlStatusController> statusControllers;

    private OnTouchListener onTouchListener;

    public OverlayParent getParent() {
        return parent;
    }

    private Camera camera;   //TODO

    private boolean visible;
    private boolean removable;

    /**
     * 用于获取绝对位置
     */
    private OverlayParent parent;
    private long id;
}