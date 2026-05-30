@echo off
setlocal

set "PROJECT_DIR=%~dp0StudentManagementSystem\StudentManagementSystem"
set "INTELLIJ_HOME=C:\Program Files\JetBrains\IntelliJ IDEA 2026.1.1"
set "BUNDLED_MAVEN=%INTELLIJ_HOME%\plugins\maven\lib\maven3\bin\mvn.cmd"

if not exist "%PROJECT_DIR%\pom.xml" (
    echo Could not find pom.xml at:
    echo %PROJECT_DIR%
    pause
    exit /b 1
)

cd /d "%PROJECT_DIR%"

if exist "%INTELLIJ_HOME%\jbr\bin\java.exe" (
    set "JAVA_HOME=%INTELLIJ_HOME%\jbr"
    set "PATH=%INTELLIJ_HOME%\jbr\bin;%PATH%"
)

if exist "%BUNDLED_MAVEN%" (
    call "%BUNDLED_MAVEN%" "-Dmaven.repo.local=%PROJECT_DIR%\.m2\repository" org.openjfx:javafx-maven-plugin:0.0.8:run
) else (
    call mvn "-Dmaven.repo.local=%PROJECT_DIR%\.m2\repository" org.openjfx:javafx-maven-plugin:0.0.8:run
)

pause
