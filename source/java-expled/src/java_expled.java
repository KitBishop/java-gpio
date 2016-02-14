
import nz.net.bishop.omega.gpio.RGBLED;
import nz.net.bishop.omega.gpio.support.GPIOSupport;


/**
 *
 * @author kit
 */
public class java_expled {

    static private String PID_FILE_EXPLED = "/tmp/expled_pid";

    private enum optype {
        undef,
        expled,
        expledstop;
    }

    private String[] args;

    private optype operation;
    private int ledRedValue;
    private int ledGreenValue;
    private int ledBlueValue;

    public java_expled(String[] args) {
        this.args = args;
    }

    public int exec() {
        if (!processArgs()) {
            usage("jamvm -jar java-expled.jar");
            return -1;
        }

        if (operation == optype.expled) {
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

        System.out.printf("\t%s <ledhex>\n",nm);
        System.out.printf("\t\tStarts output to expansion led\n");
        System.out.printf("\t%s rgb <r> <g> <b>\n",nm);
        System.out.printf("\t\tStarts output to expansion led using decimal rgb values\n");
        System.out.printf("\t%s stop\n",nm);
        System.out.printf("\t\tTerminates output to expansion led\n");
        System.out.printf("\t%s help\n",nm);
        System.out.printf("\t\tDisplays this usage information\n");
        System.out.printf("Where:\n");
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

        if (argc > 0) {
            if (args[0].equals("stop")) {
                operation = optype.expledstop;
            } else if (args[0].equals("help")) {
                return false;
            } else {
                operation = optype.expled;
            }

            if (operation == optype.expled) {
                if (argc > 0) {
                    if (args[0].equals("rgb")) {
                        if (argc >= 4) {
                            try {
                                ledRedValue = new Integer(args[1]);
                            } catch (Exception e) {
                                System.out.printf("**ERROR** Invalid <r> for expled rgb: %s\n", args[1]);
                                return false;
                            }
                            if ((ledRedValue < 0) || (ledRedValue > 100)) {
                                System.out.printf("**ERROR** Invalid <r> for expled rgb: %s\n", args[1]);
                                return false;
                            }
                            
                            try {
                                ledGreenValue = new Integer(args[2]);
                            } catch (Exception e) {
                                System.out.printf("**ERROR** Invalid <g> for expled rgb: %s\n", args[2]);
                                return false;
                            }
                            if ((ledGreenValue < 0) || (ledGreenValue > 100)) {
                                System.out.printf("**ERROR** Invalid <g> for expled rgb: %s\n", args[2]);
                                return false;
                            }
                            
                            try {
                                ledBlueValue = new Integer(args[3]);
                            } catch (Exception e) {
                                System.out.printf("**ERROR** Invalid <b> for expled rgb: %s\n", args[3]);
                                return false;
                            }
                            if ((ledBlueValue < 0) || (ledBlueValue > 100)) {
                                System.out.printf("**ERROR** Invalid <b> for expled rgb: %s\n", args[3]);
                                return false;
                            }
                        } else {
                            System.out.printf("**ERROR** Invalid data specified for: expled rgb\n");
                            return false;
                        }
                    } else {
                        String expled = new String(args[0]);
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
            }
        } else {
            System.out.printf("**ERROR** No parameter supplied\n");
            return false;
        }

        return true;
    }

    void stopExpLed() {
        GPIOSupport.forkStop(0, PID_FILE_EXPLED);
    }

    void startExpLed(int ledRedValue, int ledGreenValue, int ledBlueValue) {
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
        java_expled javaExpLed = new java_expled(args);
        System.exit(javaExpLed.exec());
    }
}
