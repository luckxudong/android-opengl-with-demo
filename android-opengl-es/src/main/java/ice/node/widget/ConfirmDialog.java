package ice.node.widget;

import ice.engine.Scene;


public abstract class ConfirmDialog extends Scene {

    public ConfirmDialog() {
        onSetupComponent();
    }

    protected ConfirmDialog(int width, int height) {
        super(width, height);
        onSetupComponent();
    }

    protected abstract void onSetupComponent();

    public ButtonOverlay getConfirmButton() {
        return confirmButton;
    }

    public ButtonOverlay getCancelButton() {
        return cancelButton;
    }

    protected ButtonOverlay confirmButton;
    protected ButtonOverlay cancelButton;
}
