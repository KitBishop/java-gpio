package nz.net.bishop.omega.gpio.support;

import nz.net.bishop.omega.gpio.GPIOJniLoad;

/**
 *
 * @author kit
 */
public class GPIOSupport extends GPIOJniLoad {

    static native private int wrapperForkSet(int pinNum, String forkFileBaseName, String forkInfo);

    static native private void wrapperForkStop(int pinNum, String forkFileBaseName);

    static native private String wrapperForkInfo(int pinNum, String forkFileBaseName);
    
    static native private void wrapperExecCommand(String command, String debugInf);

    static public int forkSet(int pinNum, String forkFileBaseName, String forkInfo) {
        return wrapperForkSet(pinNum, forkFileBaseName, forkInfo);
    }

    static public void forkStop(int pinNum, String forkFileBaseName) {
        wrapperForkStop(pinNum, forkFileBaseName);
    }

    static public String getForkInfo(int pinNum, String forkFileBaseName) {
        return wrapperForkInfo(pinNum, forkFileBaseName);
    }
    
    static public void execCommand(String command, String debugInf) {
        String dbgInf;
        if ((debugInf == null) || (debugInf.length() == 0)) {
            if (command.startsWith("[debug]")) {
                command = command.substring(7);
            }
            dbgInf = "";
        } else {
            dbgInf = debugInf;
        }
        wrapperExecCommand(command, dbgInf);
    }
    
    static public void execCommand(String command) {
        execCommand(command, null);
    }
}
