# This is the only thing to change in this Makefile
# >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
# You MAY need to change JAVA-GPIO-JNI_DIR to relative directory of java-gpio if you are not using the sources as originally supplied
LIBNEW-GPIO-JNI_DIR=../libnew-gpio-jni
LIBNEW-GPIO_DIR=../libnew-gpio
# >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

SHELL := /bin/bash
# Use buildep=1 on make command line to also build libnew-gpio-jni first
builddep?=0

# Define directories used
# Where the source java files are located
SRC_DIR=./src
# Used for intermediate build products
BUILD_DIR=./build
# Used for intermeditae class file products
CLASS_BUILD_DIR=$(BUILD_DIR)/classes
LIB_LIB_BUILD_DIR=$(BUILD_DIR)/lib
# Final distribution directory
DIST_DIR=./dist

# Find the java sources to build and derive class file names
JAVA_FILES=$(shell find $(SRC_DIR)/ -name "*.java" | sed s:$(SRC_DIR)/:: | tr "\n" " ")
CLASS_FILES=$(patsubst %.java,$(CLASS_BUILD_DIR)/%.class,${JAVA_FILES})

# Define the name of the manifest file to be created
MANIFEST_FILE=$(BUILD_DIR)/manifest.mf

# The name of the final jar file
JAR_FILE_NOLIB=$(DIST_DIR)/nolib/java-gpio-lib.jar
JAR_FILE_WITHLIB=$(DIST_DIR)/withlib/java-gpio-lib.jar

# Any javac options to apply in compiling the java sources
JAVA_OPT=-g
# Compression option to use when creating jar file
JAR_COMPRESS=0

# The main default build target
all: nolib withlib


ifeq ($(builddep),1)
nolib: precomp MAKE_LIB $(JAR_FILE_NOLIB)
else
nolib: precomp $(JAR_FILE_NOLIB)
endif

ifeq ($(builddep),1)
withlib: precomp MAKE_LIB $(JAR_FILE_WITHLIB)
else
withlib: precomp $(JAR_FILE_WITHLIB)
endif

# Ensure library is built
MAKE_LIB:
	pushd $(LIBNEW-GPIO-JNI_DIR); make clean; make builddep=$(builddep); popd

# Build jar with no lib
$(JAR_FILE_NOLIB): $(CLASS_FILES) $(MANIFEST_FILE)
	jar cmf$(JAR_COMPRESS) $(MANIFEST_FILE) $(JAR_FILE_NOLIB) -C $(CLASS_BUILD_DIR) .

# Build jar including lib
$(JAR_FILE_WITHLIB): $(CLASS_FILES) $(MANIFEST_FILE)
	rm -rf $(LIB_LIB_BUILD_DIR)/*
	cp $(LIBNEW-GPIO-JNI_DIR)/dist/*-all.so $(LIB_LIB_BUILD_DIR)/.
	jar cmf$(JAR_COMPRESS) $(MANIFEST_FILE) $(JAR_FILE_WITHLIB) -C $(CLASS_BUILD_DIR) . -C $(LIB_LIB_BUILD_DIR) .

# Compile the java files	
$(CLASS_BUILD_DIR)/%.class: $(SRC_DIR)/%.java
	javac $(JAVA_OPT) -d $(CLASS_BUILD_DIR) -cp $(CLASS_BUILD_DIR) -sourcepath $(SRC_DIR) $<

# Create the (minimal) manifest file to be used
$(MANIFEST_FILE):
	echo "Manifest-Version: 1.0" > $(MANIFEST_FILE)

# Ensure required directories exist and are created
precomp:
	@mkdir -p $(BUILD_DIR)
	@mkdir -p $(CLASS_BUILD_DIR)
	@mkdir -p $(DIST_DIR)
	@mkdir -p $(DIST_DIR)/nolib
	@mkdir -p $(DIST_DIR)/withlib
	@mkdir -p $(LIB_LIB_BUILD_DIR)

# Clean by removing all build and distribution products
clean: 
	rm -rf $(BUILD_DIR)/*
	rm -rf $(DIST_DIR)/*
