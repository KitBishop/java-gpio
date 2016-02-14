package nz.net.bishop.omega.gpio;

/**
 *
 * @author kit
 */
public interface IGPIOIrqHandler {
    void handleInterrupt(int pinNum, GPIOIrqType type);
}
