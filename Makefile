JAVAC = javac

BIN = ./bin/

SRC = ./src/

# Java compiler flags
JAVAFLAGS = -g -d $(BIN) -cp $(SRC)

# Creating a .class file
COMPILE = $(JAVAC) $(JAVAFLAGS)

EMPTY =

JAVA_FILES = $(subst $(SRC), $(EMPTY), $(wildcard $(SRC)*.java))

ALL_FILES = $(JAVA_FILES)

CLASS_FILES = $(ALL_FILES:.java=.class)
all : exec

compile : $(addprefix $(BIN), $(CLASS_FILES))
$(BIN)%.class : $(SRC)%.java
	$(COMPILE) $<

exec: compile
	echo "#!/bin/bash" > dijikstra
	echo "java -cp ./bin Main $$""*" >> dijikstra
	chmod a+x dijikstra

clean : 
	rm -rf $(BIN)/*
	rm dijikstra
