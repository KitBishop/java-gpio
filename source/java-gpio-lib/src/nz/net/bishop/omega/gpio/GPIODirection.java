package nz.net.bishop.omega.gpio;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kit
 */
public enum GPIODirection {
    GPIO_INPUT(false),
    GPIO_OUTPUT(true);

    private boolean val;

    private GPIODirection(boolean v) {
        val = v;
    }

    public boolean getValue() {
        return val;
    }

    public static GPIODirection getValueFor(boolean b) {
        for (GPIODirection r : values()) {
            if (r.getValue() == b) {
                return r;
            }
        }
        return GPIO_INPUT;
    }
}
