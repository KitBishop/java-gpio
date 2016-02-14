package nz.net.bishop.omega.gpio;

/**
 *
 * @author kit
 */
public enum GPIOIrqType {
    GPIO_IRQ_NONE (0),
    GPIO_IRQ_RISING (1),
    GPIO_IRQ_FALLING (2),
    GPIO_IRQ_BOTH (3);
    
    private int val;

    private GPIOIrqType(int v) {
        val = v;
    }

    public int getValue() {
        return val;
    }
    
    public static GPIOIrqType getValueFor(int i) {
        for (GPIOIrqType r : GPIOIrqType.values()) {
            if (r.getValue() == i) {
                return r;
            }
        }
        return GPIO_IRQ_NONE;
    }
}
