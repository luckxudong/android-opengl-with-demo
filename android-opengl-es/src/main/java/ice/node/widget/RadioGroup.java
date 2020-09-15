package ice.node.widget;

import java.util.ArrayList;

/**
 * User: jason
 * Date: 12-1-13
 * Time: 上午9:33
 */
public class RadioGroup extends ArrayList<RadioButtonOverlay> {

    public RadioGroup(RadioButtonOverlay... radioButtons) {

        for (RadioButtonOverlay radioButton : radioButtons) {
            radioButton.setParent(this);
            add(radioButton);
        }
    }

    public void setToggled(RadioButtonOverlay toggled) {
        this.toggled = toggled;
    }

    public RadioButtonOverlay getToggled() {
        return toggled;
    }

    public void setOnToggledListener(RadioButtonOverlay.OnToggledListener onToggledListener) {
        for (RadioButtonOverlay radioButton : this) {
            radioButton.setOnToggledListener(onToggledListener);
        }
    }

    private RadioButtonOverlay toggled;
}
