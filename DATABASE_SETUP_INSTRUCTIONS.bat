@echo off
REM Database Setup Script for Auth JWT Application

echo.
echo ========================================
echo AUTH JWT - DATABASE SETUP GUIDE
echo ========================================
echo.

echo Langkah 1: Pastikan MySQL sudah diinstall dan running
echo.
echo Untuk Windows:
echo   - Open Services (services.msc)
echo   - Cari "MySQL80" atau "MySQL"
echo   - Pastikan status = "Running"
echo.

echo Langkah 2: Buka MySQL Command Prompt
echo   - Buka Command Prompt atau PowerShell
echo   - Ketik: mysql -u root -p
echo   - Masukkan password MySQL Anda
echo.

echo Langkah 3: Copy-paste commands ini di MySQL:
echo.
echo CREATE DATABASE IF NOT EXISTS auth_jwt_db;
echo USE auth_jwt_db;
echo.

echo Langkah 4: Import schema dari file database.sql:
echo mysql -u root -p auth_jwt_db ^< database.sql
echo.

echo ========================================
echo SETELAH DATABASE SIAP
echo ========================================
echo.
echo Kemudian jalankan aplikasi dengan:
echo   mvn spring-boot:run
echo.
echo Atau gunakan batch file ini:
echo   start_app.bat
echo.
echo Aplikasi akan berjalan di:
echo   http://localhost:8080
echo.
echo Swagger UI akan tersedia di:
echo   http://localhost:8080/swagger-ui.html
echo.

pause
