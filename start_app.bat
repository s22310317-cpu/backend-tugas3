@echo off
title Auth JWT Spring Security - Application
color 0A

echo.
echo ===============================================
echo    LOGIN & REGISTRASI - SPRING SECURITY JWT
echo ===============================================
echo.
echo Starting application on http://localhost:8080
echo.
echo Tekan CTRL+C untuk stop server
echo.
echo ===============================================
echo.

cd "C:\Users\angga\Desktop\tugas back end"
java -jar target\auth-jwt-spring-security-1.0.0.jar

pause
