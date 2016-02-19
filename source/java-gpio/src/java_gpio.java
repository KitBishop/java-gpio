
import nz.net.bishop.omega.gpio.GPIOAccess;
import nz.net.bishop.omega.gpio.GPIODirection;
import nz.net.bishop.omega.gpio.GPIOIrqType;
import nz.net.bishop.omega.gpio.GPIOPin;
import nz.net.bishop.omega.gpio.GPIOResult;
import nz.net.bishop.omega.gpio.IGPIOIrqHandler;
import nz.net.bishop.omega.gpio.RGBLED;
import nz.net.bishop.omega.gpio.support.GPIOSupport;

/**
 *
 * @author kit
 */
public class java_gpio {

    static private String PID_FILE_PWM = "/tmp/pin%d_pwm_pid";
    static private String PID_FILE_IRQ = "/tmp/pin%d_irq_pid";
    static private String PID_FILE_EXPLED = "/tmp/expled_pid";

    private enum optype {
        undef,
        info,
        set,
        get,
        setd,
        getd,
        pwm,
        pwmstop,
        irq,
        irqstop,
        expled,
        expledstop;
    }

    private String[] args;

    private optype operation;
    private int pinnum;
    private int value;
    private int freq;
    private int duty;
    private GPIODirection direction;
    private GPIOIrqType irqtype;
    private String irqtext;
    private int irqdebounce;
    private int ledRedValue;
    private int ledGreenValue;
    private int ledBlueValue;
    private static int ledRedPin = 17;
    private static int ledGreenPin = 16;
    private static int ledBluePin = 15;

    private static int numPins = 27;

    class TestIrqHandler implements IGPIOIrqHandler {

        private String command;

        public TestIrqHandler(String command) {
            this.command = command;
        }

        @Override
        public void handleInterrupt(int pinNum, GPIOIrqType type) {
            String debugInf = "GPIO Irq Debug: Pin=" + pinNum + " Type=" + type + " Command:" + command;
            GPIOSupport.execCommand(command, debugInf);
        }

    }

    public java_gpio(String[] args) {
        this.args = args;
    }

    public int exec() {
        if (!processArgs()) {
            usage("jamvm -jar java-gpio.jar");
            return -1;
        }

        if (operation == optype.info) {
            if (pinnum != -1) {
                reportOn(pinnum);
            } else {
                int i;
                for (i = 0; i < numPins; i++) {
                    if (GPIOAccess.isPinUsable(i)) {
                        reportOn(i);
                    }
                }
            }
            return 0;
        } else if (operation == optype.set) {
            if (pinnum != -1) {
                setPin(pinnum, value);
            } else {
                int i;
                for (i = 0; i < numPins; i++) {
                    if (GPIOAccess.isPinUsable(i)) {
                        setPin(i, value);
                    }
                }
            }
            return 0;
        } else if (operation == optype.get) {
            return getPin(pinnum);
        } else if (operation == optype.setd) {
            if (pinnum != -1) {
                setPinDir(pinnum, direction);
            } else {
                int i;
                for (i = 0; i < numPins; i++) {
                    if (GPIOAccess.isPinUsable(i)) {
                        setPinDir(i, direction);
                    }
                }
            }
            return 0;
        } else if (operation == optype.getd) {
            GPIODirection dir = getPinDir(pinnum);
            return dir.getValue() ? 1 : 0;
        } else if (operation == optype.pwm) {
            startPWM(pinnum, freq, duty);
            return 0;
        } else if (operation == optype.pwmstop) {
            stopPWM(pinnum);
            return 0;
        } else if (operation == optype.irq) {
            startIRQ(pinnum, irqtype, irqtext, irqdebounce);
            return 0;
        } else if (operation == optype.irqstop) {
            stopIRQ(pinnum);
            return 0;
        } else if (operation == optype.expled) {
            startExpLed(ledRedValue, ledGreenValue, ledBlueValue);
            return 0;
        } else if (operation == optype.expledstop) {
            stopExpLed();
            return 0;
        }

        return -1;
    }

    private void usage(String nm) {
        System.out.printf("Usage\n");
        System.out.printf("Commands - one of:\n");

        System.out.printf("\t%s set-input <pin>\n", nm);
        System.out.printf("\t\tSets pin to be an input pin\n");
        System.out.printf("\t%s set-output <pin>\n", nm);
        System.out.printf("\t\tSets pin to be an output pin\n");
        System.out.printf("\t%s get-direction <pin>\n", nm);
        System.out.printf("\t\tGets and returns pin direction\n");
        System.out.printf("\t%s read <pin>\n", nm);
        System.out.printf("\t\tGets and returns input pin value\n");
        System.out.printf("\t%s set <pin> <val>\n", nm);
        System.out.printf("\t\tSets output pin value\n");

        System.out.printf("\t%s pwm <pin> <freq> <duty>\n", nm);
        System.out.printf("\t\tStarts PWM output on pin\n");
        System.out.printf("\t%s pwmstop <pin>\n", nm);
        System.out.printf("\t\tStops PWM output on pin\n");
        System.out.printf("\t%s irq <pin> <irqtype> <irqcmd> <debounce>\n", nm);
        System.out.printf("\t\tEnables IRQ handling on pin\n");
        System.out.printf("\t%s irqstop <pin>\n", nm);
        System.out.printf("\t\tTerminates IRQ handling on pin\n");

        System.out.printf("\t%s expled <ledhex>\n", nm);
        System.out.printf("\t\tStarts output to expansion led\n");
        System.out.printf("\t%s expled rgb <r> <g> <b>\n", nm);
        System.out.printf("\t\tStarts output to expansion led using decimal rgb values\n");
        System.out.printf("\t%s expledstop\n", nm);
        System.out.printf("\t\tTerminates output to expansion led\n");

        System.out.printf("\t%s info <pin>\n", nm);
        System.out.printf("\t\tDisplays information on pin(s)\n");
        System.out.printf("\t%s help\n", nm);
        System.out.printf("\t\tDisplays this help information\n");

        System.out.printf("Where:\n");
        System.out.printf("\t<pin> is one of\n\t\t0, 1, 6, 7, 8, 12, 13, 14, 15, 16, 17, 18, 19, 23, 26, all\n");
        System.out.printf("\t\tA <pin> of all can only be used for:\n");
        System.out.printf("\t\t\tinfo, set-input, set-output, set\n");
        System.out.printf("\t<val> is only required for set:\n");
        System.out.printf("\t\t<val> is 0 or 1\n");
        System.out.printf("\t<freq> is PWM frequency in Hz > 0\n");
        System.out.printf("\t<duty> is PWM duty cycle %% in range 0 to 100\n");
        System.out.printf("\t<irqtype> is the type for IRQ and is one of:\n");
        System.out.printf("\t\tfalling, rising, both\n");
        System.out.printf("\t<irqcmd> is the shell command to be executed when the IRQ occurs\n");
        System.out.printf("\t\tMust be enclosed in \" characters if it contains\n");
        System.out.printf("\t\tspaces or other special characters\n");
        System.out.printf("\t\tIf it starts with the string [debug],\n");
        System.out.printf("\t\tdebug output is displayed first\n");
        System.out.printf("\t<debounce> is optional debounce time for IRQ in milliseconds\n");
        System.out.printf("\t\tDefaults to 0 if not supplied\n");
        System.out.printf("\t<ledhex> specifies the hex value to be output to expansion led\n");
        System.out.printf("\t\tMust be a six digit hex value with or without leading 0x\n");
        System.out.printf("\t\tThe order of the hex digits is: rrggbb\n");
        System.out.printf("\t<r> <g> <b> specify the decimal values for output to expansion led\n");
        System.out.printf("\t\tEach value is in the range 0..100\n");
        System.out.printf("\t\t0 = off, 100 = fully on\n");
    }

    private boolean processArgs() {
        int argc = args.length;
        operation = optype.undef;
        pinnum = -1;
        value = -1;
        if (argc > 0) {
            if (args[0].equals("info")) {
                operation = optype.info;
            } else if (args[0].equals("set")) {
                operation = optype.set;
            } else if (args[0].equals("get")) {
                operation = optype.get;
            } else if (args[0].equals("set-input")) {
                operation = optype.setd;
                direction = GPIODirection.GPIO_INPUT;
            } else if (args[0].equals("set-output")) {
                operation = optype.setd;
                direction = GPIODirection.GPIO_OUTPUT;
            } else if (args[0].equals("get-direction")) {
                operation = optype.getd;
            } else if (args[0].equals("pwm")) {
                operation = optype.pwm;
            } else if (args[0].equals("pwmstop")) {
                operation = optype.pwmstop;
            } else if (args[0].equals("irq")) {
                operation = optype.irq;
            } else if (args[0].equals("irqstop")) {
                operation = optype.irqstop;
            } else if (args[0].equals("expled")) {
                operation = optype.expled;
            } else if (args[0].equals("expledstop")) {
                operation = optype.expledstop;
            } else if (args[0].equals("help")) {
                return false;
            } else {
                System.out.printf("**ERROR** Invalid <op> : %s\n", args[0]);
                return false;
            }

            if (operation == optype.expled) {
                if (argc > 1) {
                    if (args[1].equals("rgb")) {
                        if (argc >= 5) {
                            try {
                                ledRedValue = new Integer(args[2]);
                            } catch (Exception e) {
                                System.out.printf("**ERROR** Invalid <r> for expled rgb: %s\n", args[2]);
                                return false;
                            }
                            if ((ledRedValue < 0) || (ledRedValue > 100)) {
                                System.out.printf("**ERROR** Invalid <r> for expled rgb: %s\n", args[2]);
                                return false;
                            }

                            try {
                                ledGreenValue = new Integer(args[3]);
                            } catch (Exception e) {
                                System.out.printf("**ERROR** Invalid <g> for expled rgb: %s\n", args[3]);
                                return false;
                            }
                            if ((ledGreenValue < 0) || (ledGreenValue > 100)) {
                                System.out.printf("**ERROR** Invalid <g> for expled rgb: %s\n", args[3]);
                                return false;
                            }

                            try {
                                ledBlueValue = new Integer(args[4]);
                            } catch (Exception e) {
                                System.out.printf("**ERROR** Invalid <b> for expled rgb: %s\n", args[4]);
                                return false;
                            }
                            if ((ledBlueValue < 0) || (ledBlueValue > 100)) {
                                System.out.printf("**ERROR** Invalid <b> for expled rgb: %s\n", args[4]);
                                return false;
                            }
                        } else {
                            System.out.printf("**ERROR** Invalid data specified for: expled rgb\n");
                            return false;
                        }
                    } else {
                        String expled = new String(args[1]);
                        if (expled.startsWith("0x") || expled.startsWith("0X")) {
                            expled = expled.substring(2);
                        }

                        if (expled.length() != 6) {
                            System.out.printf("**ERROR** Invalid <ledhex> for expled : %s\n", args[0]);
                            return false;
                        }

                        int allled = 0;
                        try {
                            allled = Integer.parseInt(expled, 16);
                        } catch (Exception e) {
                            System.out.printf("**ERROR** Invalid <ledhex> for expled : %s\n", args[0]);
                            return false;
                        }

                        ledRedValue = (allled >> 16) & 0xff;
                        ledGreenValue = (allled >> 8) & 0xff;
                        ledBlueValue = allled & 0xff;

                        ledRedValue = (ledRedValue * 100) / 255;
                        ledGreenValue = (ledGreenValue * 100) / 255;
                        ledBlueValue = (ledBlueValue * 100) / 255;
                    }
                } else {
                    System.out.printf("**ERROR** No data specified for: expled\n");
                    return false;
                }
            }
            else if (operation == optype.expledstop) {
		return true;
            } else if (argc > 1) {
                if (args[1].equals("all")) {
                    if ((operation != optype.set)
                            && (operation != optype.getd)
                            && (operation != optype.info)) {
                        System.out.printf("**ERROR** Invalid <pin> for operation: %s\n", args[1]);
                        return false;
                    }
                    pinnum = -1;
                } else {
                    try {
                        pinnum = new Integer(args[1]);
                        if (!GPIOAccess.isPinUsable(pinnum)) {
                            System.out.printf("**ERROR** Invalid <pin> : %s\n", args[1]);
                            return false;
                        }
                    } catch (Exception e) {
                        System.out.printf("**ERROR** Invalid <pin> : %s\n", args[1]);
                        return false;
                    }
                }

                if (operation == optype.set) {
                    if (argc > 2) {
                        if (args[2].equals("0")) {
                            value = 0;
                        } else if (args[2].equals("1")) {
                            value = 1;
                        } else {
                            System.out.printf("**ERROR** Invalid <val> for set: %s\n", args[2]);
                            return false;
                        }
                    } else {
                        System.out.printf("**ERROR** No <val> specified for set\n");
                        return false;
                    }
                } else if (operation == optype.pwm) {
                    if (argc > 2) {
                        try {
                            freq = new Integer(args[2]);
                            if (freq > 0) {
                                if (argc > 3) {
                                    try {
                                        duty = new Integer(args[3]);
                                        if ((duty < 0) || (duty > 100)) {
                                            System.out.printf("**ERROR** Invalid <duty> for pwm : %s\n", args[3]);
                                            return false;
                                        }
                                    } catch (Exception e) {
                                        System.out.printf("**ERROR** Invalid <duty> for pwm : %s\n", args[3]);
                                        return false;
                                    }
                                } else {
                                    System.out.printf("**ERROR** No <duty> specified for pwm\n");
                                    return false;
                                }
                            } else {
                                System.out.printf("**ERROR** Invalid <freq> for pwm : %s\n", args[2]);
                                return false;
                            }
                        } catch (Exception e) {
                            System.out.printf("**ERROR** Invalid <freq> for pwm : %s\n", args[2]);
                            return false;
                        }
                    } else {
                        System.out.printf("**ERROR** No <freq> specified for pwm\n");
                        return false;
                    }
                } else if (operation == optype.irq) {
                    if (argc > 2) {
                        if (args[2].equals("rising")) {
                            irqtype = GPIOIrqType.GPIO_IRQ_RISING;
                        } else if (args[2].equals("falling")) {
                            irqtype = GPIOIrqType.GPIO_IRQ_FALLING;
                        } else if (args[2].equals("both")) {
                            irqtype = GPIOIrqType.GPIO_IRQ_BOTH;
                        } else {
                            System.out.printf("**ERROR** Invalid <irqtype> for irq : %s\n", args[2]);
                            return false;
                        }
                        if (argc > 3) {
                            irqtext = args[3];
                            if (argc > 4) {
                                try {
                                    irqdebounce = new Integer(args[4]);
                                    if (irqdebounce < 0) {
                                        System.out.printf("**ERROR** Invalid <debounce> for irq : %s\n", args[4]);
                                        return false;
                                    }
                                } catch (Exception e) {
                                    System.out.printf("**ERROR** Invalid <debounce> for irq : %s\n", args[4]);
                                    return false;
                                }
                            } else {
                                irqdebounce = 0;
                            }
                        } else {
                            System.out.printf("**ERROR** No <irqcmd> specified for irq\n");
                            return false;
                        }
                    } else {
                        System.out.printf("**ERROR** No <irqtype> specified for irq\n");
                        return false;
                    }
                }
            } else {
                System.out.printf("**ERROR** No <pin> specified\n");
                return false;
            }
        } else {
            System.out.printf("**ERROR** No <op> specified\n");
            return false;
        }

        return true;
    }

    void reportOn(int pinnum) {
        System.out.printf("Pin:%d, ", pinnum);
        GPIOPin pin = new GPIOPin(pinnum);
        GPIODirection dir = pin.getDirection();
        System.out.printf("Direction:");
        if (dir.equals(GPIODirection.GPIO_OUTPUT)) {
            System.out.printf("OUTPUT");
            if ((pinnum == ledRedPin) || (pinnum == ledGreenPin) || (pinnum == ledBluePin)) {
                String expledInf = GPIOSupport.getForkInfo(0, PID_FILE_EXPLED);
                if (expledInf.length() > 0) {
                    if (pinnum == ledRedPin) {
                        System.out.printf(", EXPLED(red)\n%s", expledInf);
                    } else if (pinnum == ledGreenPin) {
                        System.out.printf(", EXPLED(green)\n%s", expledInf);
                    } else if (pinnum == ledBluePin) {
                        System.out.printf(", EXPLED(blue)\n%s", expledInf);
                    }
                }
            }
            String pwmInf = GPIOSupport.getForkInfo(pinnum, PID_FILE_PWM);
            if (pwmInf.length() > 0) {
                System.out.printf(" PWM\n:%s", pwmInf);
            }
        } else if (dir.equals(GPIODirection.GPIO_INPUT)) {
            int val = pin.get();
            System.out.printf("INPUT, Value:%d", val);
            String irqInf = GPIOSupport.getForkInfo(pinnum, PID_FILE_IRQ);
            if (irqInf.length() > 0) {
                System.out.printf(" IRQ\n:%s", irqInf);
            }
        }
        System.out.printf("\n");
    }

    void setPin(int pinnum, int val) {
        if ((pinnum == ledRedPin) || (pinnum == ledGreenPin) || (pinnum == ledBluePin)) {
            stopExpLed();
        }
        stopPWM(pinnum);
        stopIRQ(pinnum);
        GPIOPin pin = new GPIOPin(pinnum);
        pin.setDirection(GPIODirection.GPIO_OUTPUT);
        pin.set(val);
        GPIOResult r = pin.getLastResult();
        if (r != GPIOResult.GPIO_OK) {
            System.out.printf("**ERROR setting pin:%d to value:%d, err=%d\n", pinnum, val, r);
        }
    }

    int getPin(int pinnum) {
        if ((pinnum == ledRedPin) || (pinnum == ledGreenPin) || (pinnum == ledBluePin)) {
            stopExpLed();
        }
        stopPWM(pinnum);
        stopIRQ(pinnum);
        GPIOPin pin = new GPIOPin(pinnum);
        pin.setDirection(GPIODirection.GPIO_INPUT);
        int val = pin.get();
        GPIOResult r = pin.getLastResult();
        if (r != GPIOResult.GPIO_OK) {
            System.out.printf("**ERROR getting pin:%d value, err=%d\n", pinnum, r);
            val = 0;
        }
        return val;
    }

    void setPinDir(int pinnum, GPIODirection dir) {
        if ((pinnum == ledRedPin) || (pinnum == ledGreenPin) || (pinnum == ledBluePin)) {
            stopExpLed();
        }
        stopPWM(pinnum);
        stopIRQ(pinnum);
        GPIOPin pin = new GPIOPin(pinnum);
        pin.setDirection(dir);
        GPIOResult r = pin.getLastResult();
        if (r != GPIOResult.GPIO_OK) {
            System.out.printf("**ERROR setting pin direction:%d to direction:%d, err=%d\n", pinnum, dir, r);
        }
    }

    GPIODirection getPinDir(int pinnum) {
        GPIOPin pin = new GPIOPin(pinnum);
        GPIODirection dir = pin.getDirection();
        GPIOResult r = pin.getLastResult();
        if (r != GPIOResult.GPIO_OK) {
            System.out.printf("**ERROR getting pin:%d direction, err=%d\n", pinnum, r);
        }
        return dir;
    }

    void stopPWM(int pinnum) {
        GPIOSupport.forkStop(pinnum, PID_FILE_PWM);
    }

    void startPWM(int pinnum, int freq, int duty) {
        if ((pinnum == ledRedPin) || (pinnum == ledGreenPin) || (pinnum == ledBluePin)) {
            stopExpLed();
        }
        stopPWM(pinnum);
        stopIRQ(pinnum);
        int forkPid = GPIOSupport.forkSet(pinnum, PID_FILE_PWM, "Frequency:" + freq + ", Duty:" + duty + "\n");
        if (forkPid == 0) {
            GPIOPin pin = new GPIOPin(pinnum);
            pin.setDirection(GPIODirection.GPIO_OUTPUT);
            GPIOResult r = pin.getLastResult();
            if (r != GPIOResult.GPIO_OK) {
                System.out.printf("**ERROR setting PWM on pin:%d, err=%d\n", pinnum, r);
                return;
            } else {
                pin.setPWM(freq, duty);
                r = pin.getLastResult();
                if (r != GPIOResult.GPIO_OK) {
                    System.out.printf("**ERROR setting PWM on pin:%d, err=%d\n", pinnum, r);
                    return;
                }
            }
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (Exception ex) {
                    System.out.println("Exception in sleep for PWM:" + ex.getMessage());
                }
            }
        }
    }

    void stopIRQ(int pinnum) {
        GPIOSupport.forkStop(pinnum, PID_FILE_IRQ);
    }

    void startIRQ(int pinnum, GPIOIrqType type, String txt, int debounce) {
        if ((pinnum == ledRedPin) || (pinnum == ledGreenPin) || (pinnum == ledBluePin)) {
            stopExpLed();
        }
        stopPWM(pinnum);
        stopIRQ(pinnum);

        String inf;
        inf = "Type:";
        if (type == GPIOIrqType.GPIO_IRQ_RISING) {
            inf = inf + "Rising";
        } else if (type == GPIOIrqType.GPIO_IRQ_FALLING) {
            inf = inf + "Falling";
        } else {
            inf = inf + "Both";
        }

        inf = inf + " Cmd:'" + txt + "', Debounce:" + debounce + "\n";

        int forkPid = GPIOSupport.forkSet(pinnum, PID_FILE_IRQ, inf);
        if (forkPid == 0) {
            GPIOPin pin = new GPIOPin(pinnum);
            pin.setDirection(GPIODirection.GPIO_INPUT);
            GPIOResult r = pin.getLastResult();
            if (r != GPIOResult.GPIO_OK) {
                System.out.printf("**ERROR setting IRQ on pin:%d, err=%d\n", pinnum, r);
                return;
            } else {
                TestIrqHandler handler = new TestIrqHandler(txt);
                pin.setIrq(type, handler, debounce);
                r = pin.getLastResult();
                if (r != GPIOResult.GPIO_OK) {
                    System.out.printf("**ERROR setting IRQ on pin:%d, err=%d\n", pinnum, r);
                    return;
                }
            }
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (Exception ex) {
                    System.out.println("Exception in sleep for IRQ:" + ex.getMessage());
                }
            }
        }
    }

    void stopExpLed() {
        GPIOSupport.forkStop(0, PID_FILE_EXPLED);
    }

    void startExpLed(int ledRedValue, int ledGreenValue, int ledBlueValue) {
        stopIRQ(ledRedPin);
        stopIRQ(ledBluePin);
        stopIRQ(ledGreenPin);
        stopPWM(ledRedPin);
        stopPWM(ledBluePin);
        stopPWM(ledGreenPin);
        stopExpLed();

        String inf;
        inf = "Red Value:" + ledRedValue + ", Green Value:" + ledGreenValue + ", Blue Value:" + ledBlueValue;

        int forkPid = GPIOSupport.forkSet(0, PID_FILE_EXPLED, inf);

        if (forkPid == 0) {
            RGBLED expled = new RGBLED();

            expled.setColor(ledRedValue, ledGreenValue, ledBlueValue);

            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (Exception ex) {
                    System.out.println("Exception in sleep for EXPLED:" + ex.getMessage());
                }
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java_gpio javaGPIO = new java_gpio(args);
        System.exit(javaGPIO.exec());
    }
}
