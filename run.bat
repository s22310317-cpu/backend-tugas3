@echo off
REM Script untuk setup dan run Spring Boot Login & Registrasi Application

echo ===============================================
echo Login Registrasi - Spring Security JWT
echo ===============================================
echo.

REM Check Java
echo [1] Checking Java Installation...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java tidak ditemukan!
    echo Silahkan install Java 17 atau lebih tinggi
    pause
    exit /b 1
)
echo Java: OK

REM Check Maven
echo [2] Checking Maven Installation...
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Maven tidak ditemukan!
    echo Silahkan install Maven 3.6 atau lebih tinggi
    pause
    exit /b 1
)
echo Maven: OK

REM Check MySQL
echo [3] Checking MySQL Installation...
mysql --version >nul 2>&1
if %errorlevel% neq 0 (
    echo WARNING: MySQL command line client tidak ditemukan
    echo Pastikan MySQL Server sudah berjalan dan database sudah di-create
    echo.
) else (
    echo MySQL: OK
    echo.
    echo [4] Creating Database...
    echo Masukkan MySQL password (tekan Enter jika kosong):
    set /p mysqlpass=
    
    mysql -u root -p%mysqlpass% < database.sql >nul 2>&1
    if %errorlevel% equ 0 (
        echo Database created successfully
    ) else (
        echo WARNING: Database mungkin sudah ada atau password salah
    )
)

echo.
echo [5] Building Project...
mvn clean install -DskipTests
if %errorlevel% neq 0 (
    echo ERROR: Maven build failed!
    pause
    exit /b 1
)

echo.
echo [6] Running Application...
echo Server akan berjalan di http://localhost:8080
echo.
mvn spring-boot:run

pause
