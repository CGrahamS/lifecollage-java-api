@echo off
setlocal
cd ..
.\gradlew clean build test -Penvironment=integration-test