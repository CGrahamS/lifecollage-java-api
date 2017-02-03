@echo off
setlocal
cd ..
.\gradlew clean build -x test -x checkStyleMain -x pmdMain -x findBugsMain jettyRun -Penvironment=dev
