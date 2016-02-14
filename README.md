# **java-gpio**
# ADDED SOURCES AND BINARIES - STILL NO DOCUMENTATION - THIS WILL COME LATER
Java wrapper classes and programs that make use of the **libnew-gpio** C/C++ code to control Onion Omega GPIO pins

Provides the following components:

+ **java-gpio-lib** - a Java library package for access to the GPIO pins
+ **libnew-gpio-jni** - a C++ dynamic link library that provides a JNI bridge between **java-gpio-lib** and the separate C++ library **libnew-gpio**
+ **java-gpio** - a Java program that can be used to access GPIO pins similar to the Omega supplied **fast-gpio** program and to the separate **new-gpio** program
+ **java-expled** - a Java program that uses GPIO pin access to control expansion dock led similar to the Omega supplied **expled** script and to the separate **new-expled** program

***NOTE***
The **libnew-gpio** library and the **new-gpio** and **new-expled** programs referred to above may be found at https://github.com/KitBishop/new-gpio
