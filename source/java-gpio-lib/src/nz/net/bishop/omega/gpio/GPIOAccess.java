package nz.net.bishop.omega.gpio;

/**
 *
 * @author kit
 */
public class GPIOAccess extends GPIOJniLoad {

/*
    
///////////////
static {   
        try {    
            NativeUtils.loadLibraryFromJar("/resources/libHelloJNI.so");   
        } catch (IOException e) {
            // This is probably not the best way to handle exception :-)    
            e.printStackTrace();
        }    
    }  
 public static void loadLibraryFromJar(String path) throws IOException {
 
        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("The path has to be absolute (start with '/').");
        }
 
        // Obtain filename from path
        String[] parts = path.split("/");
        String filename = (parts.length > 1) ? parts[parts.length - 1] : null;
 
        // Split filename to prexif and suffix (extension)
        String prefix = "";
        String suffix = null;
        if (filename != null) {
            parts = filename.split("\\.", 2);
            prefix = parts[0];
            suffix = (parts.length > 1) ? "."+parts[parts.length - 1] : null; // Thanks, davs! :-)
        }
 
        // Check if the filename is okay
        if (filename == null || prefix.length() < 3) {
            throw new IllegalArgumentException("The filename has to be at least 3 characters long.");
        }
 
        // Prepare temporary file
        File temp = File.createTempFile(prefix, suffix);
        temp.deleteOnExit();
 
        if (!temp.exists()) {
            throw new FileNotFoundException("File " + temp.getAbsolutePath() + " does not exist.");
        }
 
        // Prepare buffer for data copying
        byte[] buffer = new byte[1024];
        int readBytes;
 
        // Open and check input stream
        InputStream is = NativeUtils.class.getResourceAsStream(path);
        if (is == null) {
            throw new FileNotFoundException("File " + path + " was not found inside JAR.");
        }
 
        // Open output stream and copy data between source file in JAR and the temporary file
        OutputStream os = new FileOutputStream(temp);
        try {
            while ((readBytes = is.read(buffer)) != -1) {
                os.write(buffer, 0, readBytes);
            }
        } finally {
            // If read/write fails, close streams safely before throwing an exception
            os.close();
            is.close();
        }
 
        // Finally, load the library
        System.load(temp.getAbsolutePath());
    }
}///////////////
*/    
    static IGPIOIrqHandler[] irqHandlers = new IGPIOIrqHandler[30];

    static {
        for (int i = 0; i < 30; i++) {
            irqHandlers[i] = null;
        }
    }

    static native private void wrapperSetDirection(int pinNum, boolean dir);

    static native private boolean wrapperGetDirection(int pinNum);

    static native private void wrapperSet(int pinNum, int value);

    static native private int wrapperGet(int pinNum);

    static native private void wrapperSetPWM(int pinNum, int freq, int duty);

    static native private void wrapperStartPWM(int pinNum);

    static native private void wrapperStopPWM(int pinNum);

    static native private int wrapperGetPWMFreq(int pinNum);

    static native private int wrapperGetPWMDuty(int pinNum);
    
    static native private void wrapperSetIRQ(int pinNum, int type, /*Class cls, */ int debounceMs);
    
    static native private void wrappetResetIRQ(int pinNum);

    static native private void wrapperEnableIRQ(int pinNum, boolean enable);
    
    static native private boolean wrapperIRQEnabled(int pinNum);
    
    static native private int wrapperGetIRQType(int pinNum);
    
    static native private boolean wrapperIsPWMRunning(int pinNum);

    static native private boolean wrapperIsPinUsable(int pinNum);

    static native private boolean wrapperIsAccessOk();

    static native private int wrapperGetLastResult();

    static public void setDirection(int pinNum, GPIODirection dir) {
        wrapperSetDirection(pinNum, dir.getValue());
    }

    static public GPIODirection getDirection(int pinNum) {
        return GPIODirection.getValueFor(wrapperGetDirection(pinNum));
    }

    static public void set(int pinNum, int value) {
        wrapperSet(pinNum, value);
    }

    static public int get(int pinNum) {
        return wrapperGet(pinNum);
    }

    static public void setPWM(int pinNum, int freq, int duty) {
        wrapperSetPWM(pinNum, freq, duty);
    }

    static public void startPWM(int pinNum) {
        wrapperStartPWM(pinNum);
    }

    static public void stopPWM(int pinNum) {
        wrapperStopPWM(pinNum);
    }

    static public void setIrq(int pinNum, GPIOIrqType type, IGPIOIrqHandler handler) {
        setIrq(pinNum, type, handler, 0);
    }

    static public void irqHandlerMethod(int pinNum, int itype) {
        GPIOIrqType type = GPIOIrqType.getValueFor(itype);
        if (irqHandlers[pinNum] != null) {
            irqHandlers[pinNum].handleInterrupt(pinNum, type);
        }
    }
    
    static public void setIrq(int pinNum, GPIOIrqType type, IGPIOIrqHandler handler, int debounceMs) {
        if (irqHandlers[pinNum] != null) {
            resetIrq(pinNum);
        }
        irqHandlers[pinNum] = handler;
        wrapperSetIRQ(pinNum, type.getValue(), /*GPIOAccess,*/ debounceMs);
    }

    static public void resetIrq(int pinNum) {
        irqHandlers[pinNum] = null;
        wrappetResetIRQ(pinNum);
    }

    static public void enableIrq(int pinNum) {
        wrapperEnableIRQ(pinNum, true);
    }

    static public void disableIrq(int pinNum) {
        wrapperEnableIRQ(pinNum, false);
    }

    static public void enableIrq(int pinNum, boolean enable) {
        wrapperEnableIRQ(pinNum, enable);
    }

    static public boolean irqEnabled(int pinNum) {
        return wrapperIRQEnabled(pinNum);
    }

    static public GPIOIrqType getIrqType(int pinNum) {
        return GPIOIrqType.getValueFor(wrapperGetIRQType(pinNum));
    }

    static IGPIOIrqHandler getIrqHandler(int pinNum) {
        return irqHandlers[pinNum];
    }

    static public int getPWMFreq(int pinNum) {
        return wrapperGetPWMFreq(pinNum);
    }

    static public int getPWMDuty(int pinNum) {
        return wrapperGetPWMDuty(pinNum);
    }

    static public boolean isPWMRunning(int pinNum) {
        return wrapperIsPWMRunning(pinNum);
    }

    static public boolean isPinUsable(int pinNum) {
        return wrapperIsPinUsable(pinNum);
    }

    static public boolean isAccessOk() {
        return wrapperIsAccessOk();
    }

    static public GPIOResult getLastResult() {
        return GPIOResult.getValueFor(wrapperGetLastResult());
    }
}
