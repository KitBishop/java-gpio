package nz.net.bishop.omega.gpio;

/**
 *
 * @author kit
 */
public enum GPIOResult {
    GPIO_OK (0),
    GPIO_BAD_ACCESS(1),
    GPIO_INVALID_PIN(2),
    GPIO_INVALID_OP(3);

    private int val;

    private GPIOResult(int v) {
        val = v;
    }

    public int getValue() {
        return val;
    }
    
    public static GPIOResult getValueFor(int i) {
        for (GPIOResult r : GPIOResult.values()) {
            if (r.getValue() == i) {
                return r;
            }
        }
        return GPIO_BAD_ACCESS;
    }
}
