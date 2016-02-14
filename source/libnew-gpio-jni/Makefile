# These are the only things to change in this Makefile
# >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
# You NEED to and MUST change TOOL_BIN_DIR to the "bin" directory of your OpenWrt uClibc toolchain
TOOL_BIN_DIR=/home/kit/OmegaCross/OpenWrt-Toolchain-ar71xx-generic_gcc-4.8-linaro_uClibc-0.9.33.2.Linux-x86_64/toolchain-mips_34kc_gcc-4.8-linaro_uClibc-0.9.33.2/bin
# You MAY need to change LIBNEW-GPIO_DIR to relative directory of libnew-gpio if you are not using the sources as originally supplied
LIBNEW-GPIO_DIR=../libnew-gpio
# You NEED to and MUST change JAVA_JDK_DIR to the directory of your Java JDK
JAVA_JDK_DIR=/usr/local/jdk1.8.0_65
# >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

SHELL := /bin/bash
# Use buildep=1 on make command line to also build libnew-gpio first
builddep?=0

# C++ compiler
CC=$(TOOL_BIN_DIR)/mips-openwrt-linux-uclibc-g++
CCFLAGS=-c -O2 -fPIC

# Flags for linking .so library
LDFLAGS=-dynamic -shared -fPIC

# Source files
SOURCES=$(shell find ./src/ -name "*.cpp" | sed s:./src/:: | tr "\n" " ")
OBJECTS=$(patsubst %.cpp,build/%.o,$(SOURCES))
GPIO_OBJECTS=$(shell find $(LIBNEW-GPIO_DIR)/build/ -name "*.o" | sed s:$(LIBNEW-GPIO_DIR)/build/:build/: | tr "\n" " ")

# Includes and libraries
INC=-I./hdr -I$(LIBNEW-GPIO_DIR)/hdr -I$(JAVA_JDK_DIR)/include -I$(JAVA_JDK_DIR)/include/linux
LIBS=$(LIBNEW-GPIO_DIR)/dist/libnew-gpio.so
SYS_LIBS=-lpthread -ldl

# Main build targets
TARGET_NOLIB=dist/libnew-gpio-jni.so
TARGET_WITHLIB=dist/libnew-gpio-jni-all.so

# Default target - build everything
all: nolib withlib

#Build nolib .so library
ifeq ($(builddep),1)
nolib: precomp MAKE_LIB $(TARGET_NOLIB)
else
nolib: precomp $(TARGET_NOLIB)
endif

#Build withlib .so library
ifeq ($(builddep),1)
withlib: precomp MAKE_LIB $(TARGET_WITHLIB)
else
withlib: precomp $(TARGET_WITHLIB)
endif

# Ensure library is built
MAKE_LIB:
	pushd $(LIBNEW-GPIO_DIR); make clean; make builddep=$(builddep); popd

# Create nolib .so library
$(TARGET_NOLIB): $(OBJECTS)
	$(CC) $(LDFLAGS) -o $(TARGET_NOLIB) $(OBJECTS) $(LIBS) $(SYS_LIBS)
	
# Create withlib .so library
$(TARGET_WITHLIB): $(OBJECTS)
	cp $(LIBNEW-GPIO_DIR)/build/*.o build/.
	$(CC) $(LDFLAGS) -o $(TARGET_WITHLIB) $(OBJECTS) $(GPIO_OBJECTS) $(SYS_LIBS)
	
# Compile sources for library
build/%.o: src/%.cpp
	$(CC) $(CCFLAGS) $(INC) $< -o $@

# Remove all created files
clean:
	rm -rf build/*
	rm -rf dist/*

# Ensure required sub-directories are created
precomp:
	@mkdir -p build
	@mkdir -p dist
	
