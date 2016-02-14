# This is the only thing to change in this Makefile
# >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
# You MAY need to change JAVA-GPIO-LIB_DIR to relative directory of java-gpio if you are not using the sources as originally supplied
JAVA-GPIO-LIB_DIR=../java-gpio-lib
# You NEED to change MAIN_CLASS to the name of the class that contains your main method
MAIN_CLASS=my_main_class
# You NEED to change JAR_NAME to the name of the .jar file that will contain your Java application
JAR_NAME=my_jar.jar
# >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

SHELL := /bin/bash
# Use buildep=1 on make command line to also build java-gpio-lib first
builddep?=0
# Use inclib=1 on make command line to include java-gpio-lib class in jar file
inclib?=0

# Define directories used
# Where the source java files are located
SRC_DIR=./src
# Used for intermediate build products
BUILD_DIR=./build
# Used for intermeditae class file products
CLASS_BUILD_DIR=$(BUILD_DIR)/classes
LIB_CLASS_BUILD_DIR=$(BUILD_DIR)/libclasses
# Final distribution directory
DIST_DIR=./dist

# Find the java sources to build and derive class file names
JAVA_FILES=$(shell find $(SRC_DIR)/ -name "*.java" | sed s:$(SRC_DIR)/:: | tr "\n" " ")
CLASS_FILES=$(patsubst %.java,$(CLASS_BUILD_DIR)/%.class,${JAVA_FILES})

# Define the name of the manifest file to be created
MANIFEST_FILE_NOLIB=$(BUILD_DIR)/manifest_nolib.mf
MANIFEST_FILE_WITHLIB=$(BUILD_DIR)/manifest_withlib.mf

# Java gpio lib
GPIO-LIB-JAR=java-gpio-lib.jar
GPIO-LIB-JAR-CPDIR=$(JAVA-GPIO-LIB_DIR)/dist/withlib
GPIO-LIB-JAR-CP=$(GPIO-LIB-JAR-CPDIR)/java-gpio-lib.jar

# The name of the final jar file
JAR_FILE_NOLIB=$(DIST_DIR)/nolib/$(JAR_NAME)
JAR_FILE_WITHLIB=$(DIST_DIR)/withlib/$(JAR_NAME)

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

# Create the jar file
$(JAR_FILE_NOLIB): $(CLASS_FILES) $(MANIFEST_FILE_NOLIB)
	jar cmf$(JAR_COMPRESS) $(MANIFEST_FILE_NOLIB) $(JAR_FILE_NOLIB) -C $(CLASS_BUILD_DIR) .

$(JAR_FILE_WITHLIB): $(CLASS_FILES) $(MANIFEST_FILE_WITHLIB)
	rm -rf $(LIB_CLASS_BUILD_DIR)/*
	unzip $(GPIO-LIB-JAR-CP) *.class -d $(LIB_CLASS_BUILD_DIR)
	unzip $(GPIO-LIB-JAR-CP) *-all.so -d $(LIB_CLASS_BUILD_DIR)
	jar cmf$(JAR_COMPRESS) $(MANIFEST_FILE_WITHLIB) $(JAR_FILE_WITHLIB) -C $(CLASS_BUILD_DIR) . -C $(LIB_CLASS_BUILD_DIR) .

# Ensure library is built
MAKE_LIB:
	pushd $(JAVA-GPIO-LIB_DIR); make clean; make builddep=$(builddep); popd

# Compile the java files	
$(CLASS_BUILD_DIR)/%.class: $(SRC_DIR)/%.java
	javac $(JAVA_OPT) -d $(CLASS_BUILD_DIR) -cp $(CLASS_BUILD_DIR):$(GPIO-LIB-JAR-CP) -sourcepath $(SRC_DIR) $<

# Create the (minimal) manifest file to be used
$(MANIFEST_FILE_NOLIB):
	echo "Manifest-Version: 1.0" > $(MANIFEST_FILE_NOLIB)
	echo "Main-Class: $(MAIN_CLASS)" >> $(MANIFEST_FILE_NOLIB)
	echo "Class-Path: java-gpio-lib.jar" >> $(MANIFEST_FILE_NOLIB)
	
$(MANIFEST_FILE_WITHLIB):
	echo "Manifest-Version: 1.0" > $(MANIFEST_FILE_WITHLIB)
	echo "Main-Class: $(MAIN_CLASS)" >> $(MANIFEST_FILE_WITHLIB)
	
#	echo "Class-Path: ./$(GPIO-LIB-JAR)" >> $(MANIFEST_FILE)

# Ensure required directories exist and are created
precomp:
	@mkdir -p $(BUILD_DIR)
	@mkdir -p $(CLASS_BUILD_DIR)
	@mkdir -p $(LIB_CLASS_BUILD_DIR)
	@mkdir -p $(DIST_DIR)
	@mkdir -p $(DIST_DIR)/nolib
	@mkdir -p $(DIST_DIR)/withlib

# Clean by removing all build and distribution products
clean-nolib:
	rm -rf $(JAR_FILE_NOLIB)

clean-withlib:
	rm -rf $(JAR_FILE_WITHLIB)

clean: clean-nolib clean-withlib
	rm -rf build/*
	rm -rf dist/*
