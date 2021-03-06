/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class nz_net_bishop_omega_gpio_GPIOAccess */

#ifndef _Included_nz_net_bishop_omega_gpio_GPIOAccess
#define _Included_nz_net_bishop_omega_gpio_GPIOAccess
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     nz_net_bishop_omega_gpio_GPIOAccess
 * Method:    wrapperSetDirection
 * Signature: (IZ)V
 */
JNIEXPORT void JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperSetDirection
  (JNIEnv *, jclass, jint, jboolean);

/*
 * Class:     nz_net_bishop_omega_gpio_GPIOAccess
 * Method:    wrapperGetDirection
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperGetDirection
  (JNIEnv *, jclass, jint);

/*
 * Class:     nz_net_bishop_omega_gpio_GPIOAccess
 * Method:    wrapperSet
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperSet
  (JNIEnv *, jclass, jint, jint);

/*
 * Class:     nz_net_bishop_omega_gpio_GPIOAccess
 * Method:    wrapperGet
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperGet
  (JNIEnv *, jclass, jint);

/*
 * Class:     nz_net_bishop_omega_gpio_GPIOAccess
 * Method:    wrapperSetPWM
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperSetPWM
  (JNIEnv *, jclass, jint, jint, jint);

/*
 * Class:     nz_net_bishop_omega_gpio_GPIOAccess
 * Method:    wrapperStartPWM
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperStartPWM
  (JNIEnv *, jclass, jint);

/*
 * Class:     nz_net_bishop_omega_gpio_GPIOAccess
 * Method:    wrapperStopPWM
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperStopPWM
  (JNIEnv *, jclass, jint);

/*
 * Class:     nz_net_bishop_omega_gpio_GPIOAccess
 * Method:    wrapperGetPWMFreq
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperGetPWMFreq
  (JNIEnv *, jclass, jint);

/*
 * Class:     nz_net_bishop_omega_gpio_GPIOAccess
 * Method:    wrapperGetPWMDuty
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperGetPWMDuty
  (JNIEnv *, jclass, jint);

/*
 * Class:     nz_net_bishop_omega_gpio_GPIOAccess
 * Method:    wrapperSetIRQ
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperSetIRQ
  (JNIEnv *, jclass, jint, jint, jint);

/*
 * Class:     nz_net_bishop_omega_gpio_GPIOAccess
 * Method:    wrappetResetIRQ
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrappetResetIRQ
  (JNIEnv *, jclass, jint);

/*
 * Class:     nz_net_bishop_omega_gpio_GPIOAccess
 * Method:    wrapperEnableIRQ
 * Signature: (IZ)V
 */
JNIEXPORT void JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperEnableIRQ
  (JNIEnv *, jclass, jint, jboolean);

/*
 * Class:     nz_net_bishop_omega_gpio_GPIOAccess
 * Method:    wrapperIRQEnabled
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperIRQEnabled
  (JNIEnv *, jclass, jint);

/*
 * Class:     nz_net_bishop_omega_gpio_GPIOAccess
 * Method:    wrapperGetIRQType
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperGetIRQType
  (JNIEnv *, jclass, jint);

/*
 * Class:     nz_net_bishop_omega_gpio_GPIOAccess
 * Method:    wrapperIsPWMRunning
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperIsPWMRunning
  (JNIEnv *, jclass, jint);

/*
 * Class:     nz_net_bishop_omega_gpio_GPIOAccess
 * Method:    wrapperIsPinUsable
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperIsPinUsable
  (JNIEnv *, jclass, jint);

/*
 * Class:     nz_net_bishop_omega_gpio_GPIOAccess
 * Method:    wrapperIsAccessOk
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperIsAccessOk
  (JNIEnv *, jclass);

/*
 * Class:     nz_net_bishop_omega_gpio_GPIOAccess
 * Method:    wrapperGetLastResult
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_nz_net_bishop_omega_gpio_GPIOAccess_wrapperGetLastResult
  (JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif
#endif
