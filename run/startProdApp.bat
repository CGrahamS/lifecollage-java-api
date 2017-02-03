@echo off
setlocal
cd ..
.\gradlew clean build jettyRun -Penvironment="prod"