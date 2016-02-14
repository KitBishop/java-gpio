#include "new-gpio-jni-support.h"

#include <cstdlib>
#include <fstream>
#include <unistd.h>
#include <signal.h>
#include <string.h>

/*
 * Class:     javanewgpiotestsupport_GPIOTestSupport
 * Method:    wrapperForkSet
 * Signature: (ILjava/lang/String;Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_nz_net_bishop_omega_gpio_support_GPIOSupport_wrapperForkSet
(JNIEnv * env, jclass, jint pinnum, jstring nameBase, jstring inf) {
    const char *cNameBase = env->GetStringUTFChars(nameBase, 0);
    const char *cInf = env->GetStringUTFChars(inf, 0);

    pid_t pid = fork();

    if (pid != 0) {
        // parent process
        char pathname[255];
        std::ofstream myfile;

        // determine the file name and open the file
        snprintf(pathname, sizeof (pathname), cNameBase, (int) pinnum);
        myfile.open(pathname);

        // write the pid to the file
        myfile << pid;
        myfile << "\n";

        char forkinf[100];
        sprintf(forkinf, "%s\n", cInf);
        myfile << forkinf;

        myfile.close();
    }

    env->ReleaseStringUTFChars(nameBase, cNameBase);
    env->ReleaseStringUTFChars(inf, cInf);

    return (jint) pid;
}

/*
 * Class:     javanewgpiotestsupport_GPIOTestSupport
 * Method:    wrapperForkStop
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_nz_net_bishop_omega_gpio_support_GPIOSupport_wrapperForkStop
(JNIEnv * env, jclass, jint pinnum, jstring nameBase) {
    const char *cNameBase = env->GetStringUTFChars(nameBase, 0);

    char pathname[255];
    char line[255];
    char cmd[255];

    int pid;
    std::ifstream myfile;

    // determine the file name and open the file
    snprintf(pathname, sizeof (pathname), cNameBase, (int) pinnum);
    myfile.open(pathname);

    // read the file
    if (myfile.is_open()) {
        // file exists, check for pid
        myfile.getline(line, 255);
        pid = atoi(line);

        // kill the process
        if (pid > 0) {
            sprintf(cmd, "kill %d >& /dev/null", pid);
            system(cmd);
        }

        sprintf(cmd, "rm %s >& /dev/null", pathname);
        system(cmd);

        myfile.close();
    }

    env->ReleaseStringUTFChars(nameBase, cNameBase);
}

/*
 * Class:     javanewgpiotestsupport_GPIOTestSupport
 * Method:    wrapperForkInfo
 * Signature: (ILjava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_nz_net_bishop_omega_gpio_support_GPIOSupport_wrapperForkInfo
(JNIEnv * env, jclass, jint pin, jstring nameBase) {
    const char *cNameBase = env->GetStringUTFChars(nameBase, 0);

    char inf[128];
    inf[0] = 0;

    char pathname[255];
    char line[255];
    char cmd[255];

    int pid;
    std::ifstream myfile;

    // determine the file name and open the file
    snprintf(pathname, sizeof (pathname), cNameBase, (int) pin);
    myfile.open(pathname);

    // read the file
    if (myfile.is_open()) {
        // file exists, check for pid
        myfile.getline(line, 255);
        pid = atoi(line);
        if (kill(pid, 0) == 0) {
            // Process is running - get the info
            char infostr[128];
            sprintf(infostr, "\tProcess Id:%d\n\t", pid);
            myfile.getline(line, 255);
            strcat(infostr, line);
            strcpy(inf, infostr);
        }

        myfile.close();
    }

    env->ReleaseStringUTFChars(nameBase, cNameBase);

    jstring jst = env->NewStringUTF(inf);
    return jst;

}


/*
 * Class:     javanewgpiotestsupport_GPIOTestSupport
 * Method:    wrapperExecCommand
 * Signature: (Ljava/lang/String;java/lang/String;)V
 */
JNIEXPORT void JNICALL Java_nz_net_bishop_omega_gpio_support_GPIOSupport_wrapperExecCommand
  (JNIEnv * env, jclass, jstring command, jstring debugInf) {
    const char *cCommand = env->GetStringUTFChars(command, 0);
    const char *cDebugInf = env->GetStringUTFChars(debugInf, 0);

    if (strstr(cCommand, "[debug]") == cCommand) {
        char dbgcmd[300];
        sprintf(dbgcmd, "echo '%s' && ", cDebugInf);
//        sprintf(dbgcmd, "echo 'GPIO Irq Debug: Pin=%d Type=", pinNum);
//        if (type == GPIO_IRQ_RISING) {
//            strcat(dbgcmd, "Rising' && ");
//        } else {
//            strcat(dbgcmd, "Falling' && ");
//        }
        strcat(dbgcmd, cCommand + 7);
        system(dbgcmd);
    } else {
        system(cCommand);
    }
    
    env->ReleaseStringUTFChars(command, cCommand);
    env->ReleaseStringUTFChars(debugInf, cDebugInf);
}
