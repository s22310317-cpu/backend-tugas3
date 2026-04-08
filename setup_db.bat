@echo off
REM Quick setup untuk database MySQL
echo ===============================================
echo DATABASE SETUP - MySQL
echo ===============================================
set /p rootpass="Masukkan MySQL root password (tekan Enter jika kosong): "

echo.
echo Membuat database dan tables...
mysql -u root -p%rootpass% < "C:\Users\angga\Desktop\tugas back end\database.sql"

if %errorlevel% equ 0 (
    echo.
    echo ✓ Database berhasil dibuat!
    echo.
    echo Default roles yang sudah dibuat: USER, ADMIN, MODERATOR
    echo.
) else (
    echo.
    echo ✗ Error saat membuat database
    echo   Pastikan MySQL sudah running dan password benar
    echo.
)

pause
