package nz.net.bishop.omega.gpio;

/**
 *
 * @author kit
 */
public class GPIOPin {
    private int pinNumber;
    private GPIOResult lastResult;
    
    public GPIOPin(int pinNum)
    {
        pinNumber = pinNum;
        lastResult = GPIOResult.GPIO_OK;
    }

    public void stop()
    {
        stopPWM();
        lastResult = GPIOAccess.getLastResult();
    }

    public void setDirection(GPIODirection dir)
    {
        GPIOAccess.setDirection(pinNumber, dir);
        lastResult = GPIOAccess.getLastResult();
    }

    public GPIODirection getDirection()
    {
        GPIODirection dir = GPIOAccess.getDirection(pinNumber);
        lastResult = GPIOAccess.getLastResult();
        return dir;
    }

    public void set(int value)
    {
        GPIOAccess.set(pinNumber, value);
        lastResult = GPIOAccess.getLastResult();
    }

    public int get()
    {
        int val = GPIOAccess.get(pinNumber);
        lastResult = GPIOAccess.getLastResult();
        return val;
    }

    public void setPWM(int freq, int duty) {
        GPIOAccess.setPWM(pinNumber, freq, duty);
        lastResult = GPIOAccess.getLastResult();
    }

    public void startPWM() {
        GPIOAccess.startPWM(pinNumber);
        lastResult = GPIOAccess.getLastResult();
    }

    public void stopPWM() {
        GPIOAccess.stopPWM(pinNumber);
        lastResult = GPIOAccess.getLastResult();
    }

    public int getPWMFreq() {
        return GPIOAccess.getPWMFreq(pinNumber);
    }

    public int getPWMDuty() {
        return GPIOAccess.getPWMDuty(pinNumber);
    }

    public boolean isPWMRunning() {
        return GPIOAccess.isPWMRunning(pinNumber);
    }

    public void setIrq(GPIOIrqType type, IGPIOIrqHandler handler) {
        setIrq(type, handler, 0);
    }
    
    public void setIrq(GPIOIrqType type, IGPIOIrqHandler handler, int debounceMs) {
        GPIOAccess.setIrq(pinNumber, type, handler, debounceMs);
        lastResult = GPIOAccess.getLastResult();
    }
    
    public void resetIrq() {
        GPIOAccess.resetIrq(pinNumber);
        lastResult = GPIOAccess.getLastResult();
    }
    
    void enableIrq() {
        GPIOAccess.enableIrq(pinNumber);
        lastResult = GPIOAccess.getLastResult();
    }
    
    void disableIrq() {
        GPIOAccess.disableIrq(pinNumber);
        lastResult = GPIOAccess.getLastResult();
    }
    
    void enableIrq(boolean enable) {
        GPIOAccess.enableIrq(pinNumber, enable);
        lastResult = GPIOAccess.getLastResult();
    }
    
    boolean irqEnabled() {
        boolean res = GPIOAccess.irqEnabled(pinNumber);
        lastResult = GPIOAccess.getLastResult();
        return res;
    }
    
    GPIOIrqType getIrqType() {
        GPIOIrqType type = GPIOAccess.getIrqType(pinNumber);
        lastResult = GPIOAccess.getLastResult();
        return type;
    }
    
    IGPIOIrqHandler getIrqHandler() {
        IGPIOIrqHandler handler = GPIOAccess.getIrqHandler(pinNumber);
        lastResult = GPIOAccess.getLastResult();
        return handler;
    }
    
    public int getPinNumber() {
        return pinNumber;
    }
    
    public GPIOResult getLastResult() {
        return lastResult;
    }
}
