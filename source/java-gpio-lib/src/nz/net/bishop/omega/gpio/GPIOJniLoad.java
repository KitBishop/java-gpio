package nz.net.bishop.omega.gpio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author kit
 */
public class GPIOJniLoad {

    static {
        boolean ok = false;
        
        // First try to load libnew-gpio-jni-alla.so from Jar file
        if (!loadLibraryFromJar("libnew-gpio-jni-all.so")) {
//            System.out.println("FAILED:loadLibraryFromJar(\"libnew-gpio-jni-all.so\")");
        } else {
//            System.out.println("OK:loadLibraryFromJar(\"libnew-gpio-jni-all.so\")");
            ok = true;
        }
        
        if (!ok) {
            // Try to load libnew-gpio-jni-all.so from system
            if (!loadSystemLibrary("libnew-gpio-jni-all.so")) {
//                System.out.println("FAILED:loadSystemLibrary(\"libnew-gpio-jni-all.so\")");
            } else {
//                System.out.println("OK:loadSystemLibrary(\"libnew-gpio-jni-all.so\")");
                ok = true;
            }
        }
        
        if (!ok) {
            // Try to load libnew-gpio-jni.so from system
            if (!loadSystemLibrary("libnew-gpio-jni.so")) {
//                System.out.println("FAILED:loadSystemLibrary(\"libnew-gpio-jni.so\")");
            } else {
//                System.out.println("OK:loadSystemLibrary(\"libnew-gpio-jni.so\")");
                ok = true;
            }
        }

        if (!ok) {
            System.out.println("Load of required GPIO library failed");
            throw new RuntimeException("Load of library failed");
        }
    }

    public static boolean loadSystemLibrary(String libname) {
        boolean ok;
        try {
            System.load(libname);
            ok = true;
        } catch (UnsatisfiedLinkError e) {
            ok = false;
        }
        
        return ok;
    }

    public static boolean loadLibraryFromJar(String libname) { // throws IOException {
        boolean res = true;

        try {
            String path = libname;

            if (!path.startsWith("/")) {
                path = "/" + path;
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
                suffix = (parts.length > 1) ? "." + parts[parts.length - 1] : null; // Thanks, davs! :-)
            }

            // Check if the filename is okay
            if (filename == null || prefix.length() < 3) {
//                System.out.println("The filename has to be at least 3 characters long.");
                return false;
            }

            // Prepare temporary file
            File temp = File.createTempFile(prefix, suffix);
            temp.deleteOnExit();

            if (!temp.exists()) {
//                System.out.println("File " + temp.getAbsolutePath() + " does not exist.");
                return false;
            }

            // Prepare buffer for data copying
            byte[] buffer = new byte[1024];
            int readBytes;

            // Open and check input stream
            InputStream is = GPIOJniLoad.class.getResourceAsStream(path);
            if (is == null) {
//                System.out.println("File " + path + " was not found inside JAR.");
                return false;
            }

            // Open output stream and copy data between source file in JAR and the temporary file
            OutputStream os = new FileOutputStream(temp);

            try {
                while ((readBytes = is.read(buffer)) != -1) {
                    os.write(buffer, 0, readBytes);
                }
            } catch (IOException ioe) {
  //              System.out.println("Error copying file:" + ioe.getMessage());
                res = false;
            } finally {
                // If read/write fails, close streams safely before throwing an exception
                os.close();
                is.close();
            }

            // Finally, load the library
            System.load(temp.getAbsolutePath());
        } catch (IOException ioe) {
            System.out.println("IOException:" + ioe.getMessage());
            res = false;
        }
        
        return res;
    }
}
