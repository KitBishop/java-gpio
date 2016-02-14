#include "new-gpio-jni.h"
#include "GPIOAccess.h"

int lastResult;

JNIEXPORT void JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperSetDirection
(JNIEnv *, jclass, jint pin, jboolean dir) {
    GPIOAccess::setDirection((int)pin, (GPIO_Direction) dir);
    lastResult = GPIOAccess::getLastResult();
}

JNIEXPORT jboolean JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperGetDirection
(JNIEnv *, jclass, jint pin) {
    jboolean dir = GPIOAccess::getDirection((int)pin);
    lastResult = GPIOAccess::getLastResult();
    return dir;
}

JNIEXPORT void JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperSet
(JNIEnv *, jclass, jint pin, jint val) {
    GPIOAccess::set((int)pin, (int)val);
    lastResult = GPIOAccess::getLastResult();
}

JNIEXPORT jint JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperGet
(JNIEnv *, jclass, jint pin) {
    jint val = (jint)GPIOAccess::get((int)pin);
    lastResult = GPIOAccess::getLastResult();
    return val;
}

JNIEXPORT void JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperSetPWM
(JNIEnv *, jclass, jint pin, jint freq, jint duty) {
    GPIOAccess::setPWM((int)pin, (int)freq, (int)duty);
    lastResult = GPIOAccess::getLastResult();
}

JNIEXPORT void JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperStartPWM
(JNIEnv *, jclass, jint pin) {
    GPIOAccess::startPWM((int)pin);
    lastResult = GPIOAccess::getLastResult();
}

JNIEXPORT void JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperStopPWM
(JNIEnv *, jclass, jint pin) {
    GPIOAccess::stopPWM((int)pin);
    lastResult = GPIOAccess::getLastResult();
}

JNIEXPORT jint JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperGetPWMFreq
(JNIEnv *, jclass, jint pin) {
    return (jint)GPIOAccess::getPWMFreq((int)pin);
}

JNIEXPORT jint JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperGetPWMDuty
(JNIEnv *, jclass, jint pin) {
    return (jint)GPIOAccess::getPWMDuty((int)pin);
}

static JNIEnv * gpioAccessJniEnv;
static jclass gpioAccessClass;

void gpioAccessIrqHandlerFunc(int pin, GPIO_Irq_Type type) {
    jmethodID gpioIrqHandlerMethodId = gpioAccessJniEnv->GetStaticMethodID(gpioAccessClass, "irqHandlerMethod", "(II)V");

    if (gpioIrqHandlerMethodId == 0) {
        return;
    }

    gpioAccessJniEnv->CallStaticVoidMethod(gpioAccessClass, gpioIrqHandlerMethodId, (int)pin, (int)type);
}

JNIEXPORT void JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperSetIRQ
(JNIEnv * env, jclass cls, jint pin, jint type, jint debounce) {
    gpioAccessJniEnv = env;
    gpioAccessClass = cls;
    GPIOAccess::setIrq((int)pin, (GPIO_Irq_Type)type, gpioAccessIrqHandlerFunc, (long int)debounce);
    lastResult = GPIOAccess::getLastResult();
}

/*
 * Class:     javanewgpio_GPIOAccess
 * Method:    wrappetResetIRQ
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrappetResetIRQ
(JNIEnv *, jclass, jint pin) {
    GPIOAccess::resetIrq((int)pin);
    lastResult = GPIOAccess::getLastResult();
}

/*
 * Class:     javanewgpio_GPIOAccess
 * Method:    wrapperEnableIRQ
 * Signature: (IZ)V
 */
JNIEXPORT void JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperEnableIRQ
(JNIEnv *, jclass, jint pin, jboolean enable) {
    GPIOAccess::enableIrq(pin, enable);
    lastResult = GPIOAccess::getLastResult();
}

/*
 * Class:     javanewgpio_GPIOAccess
 * Method:    wrapperIRQEnabled
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperIRQEnabled
(JNIEnv *, jclass, jint pin) {
    return (jboolean)GPIOAccess::irqEnabled((int)pin);
}

/*
 * Class:     javanewgpio_GPIOAccess
 * Method:    wrapperGetIRQType
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperGetIRQType
(JNIEnv *, jclass, jint pin) {
    int type = GPIOAccess::getIrqType((int)pin);
    lastResult = GPIOAccess::getLastResult();
    return (jint)type;
}

JNIEXPORT jboolean JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperIsPWMRunning
(JNIEnv *, jclass, jint pin) {
    return (jboolean)GPIOAccess::isPWMRunning((int)pin);
}

JNIEXPORT jboolean JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperIsPinUsable
(JNIEnv *, jclass, jint pin) {
    return (jboolean)GPIOAccess::isPinUsable((int)pin);
}

JNIEXPORT jboolean JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperIsAccessOk
(JNIEnv *, jclass) {
    return (jboolean)GPIOAccess::isAccessOk();
}

JNIEXPORT jint JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperGetLastResult
(JNIEnv *, jclass) {
    return (jint)lastResult;
}
