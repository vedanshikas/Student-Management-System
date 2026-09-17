@echo off
echo ===================================================
echo   Compiling Smart Student Management System...
echo ===================================================

if not exist bin mkdir bin

javac -d bin src\exception\*.java src\model\*.java src\repository\*.java src\service\*.java src\util\*.java src\Main.java src\SystemTest.java

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b %ERRORLEVEL%
)

echo [SUCCESS] Compilation complete.
echo Launching Application...
echo.
java -cp bin Main
