package nz.net.bishop.omega.gpio;

/**
 *
 * @author kit
 */
public class RGBLED {
    private static int defRedPin = 17;
    private static int defGreenPin = 16;
    private static int defBluePin = 15;

    private int redVal;
    private int greenVal;
    private int blueVal;
    
    private GPIOPin redPin;
    private GPIOPin greenPin;
    private GPIOPin bluePin;
    
    private boolean activeLow;

    public RGBLED() {
        this(defRedPin, defGreenPin, defBluePin);
    }

    public RGBLED(int redPinN, int greenPinN, int bluePinN) {
        redPin = new GPIOPin(redPinN);
        greenPin = new GPIOPin(greenPinN);
        bluePin = new GPIOPin(bluePinN);

        redPin.setDirection(GPIODirection.GPIO_OUTPUT);
        greenPin.setDirection(GPIODirection.GPIO_OUTPUT);
        bluePin.setDirection(GPIODirection.GPIO_OUTPUT);

        activeLow = true;

        setColor(0, 0, 0);
    }

    public void setColor(int rval, int gval, int bval) {
        setRed(rval);
        setGreen(gval);
        setBlue(bval);
    }

    private void setPinVal(GPIOPin pin, int value) {
        int v = value;
        if (activeLow) {
            v = 100 - v;
        }
        pin.setPWM(200, v);
    }

    void setRed(int rval) {
        redVal = rval;
        setPinVal(redPin, rval);
    }

    void setGreen(int gval) {
        greenVal = gval;
        setPinVal(greenPin, gval);
    }

    void setBlue(int bval) {
        blueVal = bval;
        setPinVal(bluePin, bval);
    }

    int getRed() {
        return redVal;
    }

    int getGreen() {
        return greenVal;
    }

    int getBlue() {
        return blueVal;
    }

    GPIOPin getRedPin() {
        return redPin;
    }

    GPIOPin getGreenPin() {
        return greenPin;
    }

    GPIOPin getBluePin() {
        return bluePin;
    }

    void setActiveLow(boolean actLow) {
        boolean changed = activeLow != actLow;
        activeLow = actLow;
        if (changed) {
            setColor(redVal, greenVal, blueVal);
        }
    }

    boolean isActiveLow() {
        return activeLow;
    }
}
