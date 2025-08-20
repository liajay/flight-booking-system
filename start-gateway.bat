@echo off
echo Starting Flight Booking Gateway...
cd /d "%~dp0gateway\bootstrap"
mvn spring-boot:run
pause
