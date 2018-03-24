@echo off

rd /s /q .\build
call gradlew.bat build
pause
