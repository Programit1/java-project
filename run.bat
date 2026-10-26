@echo off
cls
echo ==============================================================================
echo    AI-POWERED INDUSTRIAL VISUAL INSPECTION AND QUALITY MANAGEMENT SYSTEM
echo ==============================================================================
echo.
echo [1/2] Compiling consolidated Java application (IndustrialInspectionSystem.java)...
javac IndustrialInspectionSystem.java
if errorlevel 1 (
    echo [ERROR] Compilation failed!
    pause
    exit /b 1
)
echo [2/2] Launching application...
echo.
if "%1"=="--cli" (
    java IndustrialInspectionSystem --cli
) else if "%1"=="--test" (
    java IndustrialInspectionSystem --test
) else (
    java IndustrialInspectionSystem
)
pause
