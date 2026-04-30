@echo off
cd /d "%~dp0"
call gradlew installDist
if %errorlevel% neq 0 exit /b %errorlevel%
"%JAVA_HOME%\bin\java" -cp "build\install\the-arcade\lib\*" -Dstdout.encoding=UTF-8 core.tui.MenuDemo