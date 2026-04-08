# QUICK START GUIDE

## ✅ Project Sudah Selesai Dibuild

JAR file sudah dibuat di: `target/auth-jwt-spring-security-1.0.0.jar`

## 📋 Langkah Setup Final

### Langkah 1: Create Database (PENTING!)

Sebelum menjalankan aplikasi, siapkan database MySQL:

**Option A: Menggunakan Command Prompt**
```bash
cd C:\Users\angga\Desktop\tugas back end
mysql -u root -p < database.sql
```
(Tekan Enter jika password kosong)

**Option B: Menggunakan Batch Script**
- Double-click file `setup_db.bat`

**Option C: Manual (MySQL Workbench/Console)**
```sql
CREATE DATABASE IF NOT EXISTS auth_jwt_db;
USE auth_jwt_db;
-- Run semua queries dari file database.sql
```

### Langkah 2: Configure Database Connection

Edit file: `src/main/resources/application.properties`

Sesuaikan dengan credentials MySQL Anda:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/auth_jwt_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=
```

### Langkah 3: Run Application

**Option A: Double-click start_app.bat**
```
start_app.bat
```

**Option B: Manual dari Command Prompt**
```bash
cd C:\Users\angga\Desktop\tugas back end
java -jar target\auth-jwt-spring-security-1.0.0.jar
```

**Option C: Menggunakan Maven**
```bash
cd C:\Users\angga\Desktop\tugas back end
mvn spring-boot:run
```

Server akan berjalan di: **http://localhost:8080**

---

## 📝 Setelah Server Running

### Test Endpoints

**1. Public Endpoint (tidak perlu token):**
```bash
curl -X GET http://localhost:8080/api/users/public/info
```

**2. Register User Baru:**
```bash
curl -X POST http://localhost:8080/api/auth/register ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"admin\",\"email\":\"admin@example.com\",\"password\":\"Admin@123\",\"confirmPassword\":\"Admin@123\"}"
```

**3. Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"admin\",\"password\":\"Admin@123\"}"
```

**4. Akses Protected Endpoint (dengan token):**
```bash
curl -X GET http://localhost:8080/api/users/profile ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## 🔗 Menggunakan Postman

1. Import file: `Postman_Collection.json`
2. Jalankan requests dari collection

---

## 🐛 Troubleshooting

### Database Connection Error
- Pastikan MySQL sudah running
- Check username dan password di `application.properties`

### Port 8080 Sudah Digunakan
Edit `application.properties`:
```properties
server.port=8081
```

### JAR Not Found
Pastikan sudah menjalankan `mvn clean package -DskipTests` dulu

### MySQL Command Not Recognized
- Install MySQL dari: https://dev.mysql.com/downloads/mysql/
- Atau gunakan XAMPP

---

## 📚 Dokumentasi Lengkap

- **README.md** - API documentation lengkap
- **SETUP_GUIDE.md** - Setup detail step-by-step
- **PROJECT_SUMMARY.md** - Project overview

---

## ✅ Checklist Sebelum Run

- [ ] Java 17+ sudah installed (`java -version`)
- [ ] Maven 3.6+ sudah installed (`mvn -version`)
- [ ] MySQL Server sudah running
- [ ] Database sudah di-create (jalankan `setup_db.bat`)
- [ ] File `application.properties` sudah dikonfigurasi
- [ ] JAR file sudah ada di `target/` folder
- [ ] Port 8080 tidak sedang digunakan

---

## 🎉 Selamat!

Project Login & Registrasi dengan Spring Security dan JWT Token sudah siap digunakan!

Jika ada pertanyaan, cek dokumentasi di README.md atau SETUP_GUIDE.md 🚀
