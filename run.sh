#!/bin/bash
echo "==================================================="
echo "  Compiling Smart Student Management System..."
echo "==================================================="

mkdir -p bin

javac -d bin src/exception/*.java src/model/*.java src/repository/*.java src/service/*.java src/util/*.java src/Main.java src/SystemTest.java

if [ $? -ne 0 ]; then
    echo "[ERROR] Compilation failed!"
    exit 1
fi

echo "[SUCCESS] Compilation complete."
echo "Launching Application..."
echo ""
java -cp bin Main
