import nz.net.bishop.omega.gpio.GPIOPin;
import nz.net.bishop.omega.gpio.GPIOResult;
import nz.net.bishop.omega.gpio.GPIOAccess;
import nz.net.bishop.omega.gpio.GPIODirection;
import nz.net.bishop.omega.gpio.GPIOIrqType;
import nz.net.bishop.omega.gpio.IGPIOIrqHandler;
import nz.net.bishop.omega.gpio.RGBLED;

/**
 *
 * @author kit
 */
public class my_main_class {
    private String[] args;
    
    public my_main_class(String[] args) {
        this.args = args;
    }

    public int exec() {
        // Code to execute your Java GPIO application
        return 0;
    }
    public static void main(String[] args) {
        my_main_class my_main = new my_main_class(args);
        System.exit(my_main.exec());
    }
}
