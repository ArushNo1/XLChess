# Compiler and flags
JAVAC = javac
JFLAGS = -d bin -classpath bin

# Directories and files
SRC_DIR = src
BIN_DIR = bin
MANIFEST = META-INF/MANIFEST.MF
JAR_FILE = XLChess.jar

# Find all .java files in the src directory
SOURCES = $(shell find $(SRC_DIR) -name "*.java")

# Default target: build the JAR
all: $(JAR_FILE)

# Target to create the JAR file
$(JAR_FILE): $(MANIFEST) $(BIN_DIR)
	jar cvfm XLChess.jar META-INF/MANIFEST.MF -C bin . -C chess . -C chess/fonts .

# Target to compile Java sources
$(BIN_DIR): $(SOURCES)
	mkdir -p $(BIN_DIR)
	$(JAVAC) $(JFLAGS) $(SOURCES)

# Target to create the manifest file
$(MANIFEST):
	mkdir -p META-INF
	echo "Manifest-Version: 1.0" > $(MANIFEST)
	echo "Main-Class: Chess" >> $(MANIFEST)

# Clean up compiled files and generated JAR
clean:
	rm -rf $(BIN_DIR) $(JAR_FILE) META-INF
