troubleshooting

# ⚠️ DATABASE CONNECTION ERROR

## Masalah
Aplikasi tidak bisa connect ke MySQL database. Error:
```
Communications link failure - Connection refused
```

## Solusi - Setup Database Step by Step

### Step 1: Pastikan MySQL Installed & Running

**Check di Windows:**
1. Buka Services (Win + R → `services.msc`)
2. Cari `MySQL80` atau `MySQL` 
3. Pastikan Status = "Running"

**Jika belum running:**
- Klik kanan pada MySQL → Start

**Alternatif - Command Line:**
```bash
# Cek apakah MySQL berjalan
netstat -ano | findstr :3306

# Jika tidak ada hasil, MySQL tidak running
```

### Step 2: Setup Database MySQL

**Option A: Menggunakan Batch File**
```bash
Double-click file ini di terminal:
setup_db.bat
```

**Option B: Manual Setup**

Buka MySQL Command Line:
```bash
mysql -u root -p
```

Lalu jalankan queries:
```sql
CREATE DATABASE IF NOT EXISTS auth_jwt_db;
USE auth_jwt_db;

-- Biarkan Hibernate membuat tables otomatis
-- Atau import dari database.sql jika ada
```

**Option C: Command Line Direct**
```bash
mysql -u root -p < database.sql
```

### Step 3: Verify Database Connection

Check credentials di file:
```
src/main/resources/application.properties
```

Pastikan ini sesuai dengan MySQL Anda:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/auth_jwt_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=
```

### Step 4: Jalankan Aplikasi

```bash
mvn spring-boot:run
```

Atau:
```bash
Double-click: start_app.bat
```

### Step 5: Akses Aplikasi

- **API Base**: `http://localhost:8080`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **API Docs**: `http://localhost:8080/v3/api-docs`

---

## Common Issues & Solutions

### ❌ "Access denied for user 'root'@'localhost'"
**Cause**: Password salah atau MySQL user berbeda
**Solution**: 
- Update password di `application.properties`
- Atau gunakan user yang sudah ada di MySQL

### ❌ "Unknown database 'auth_jwt_db'"
**Cause**: Database belum dibuat
**Solution**:
```sql
CREATE DATABASE auth_jwt_db;
```

### ❌ "Can't connect to MySQL server on 'localhost' (10061)"
**Cause**: MySQL tidak running
**Solution**:
- Start MySQL service di Services (services.msc)
- Atau restart MySQL

### ❌ "Port 8080 already in use"
**Cause**: Aplikasi lain menggunakan port 8080
**Solution**:
- Gunakan port berbeda: `--server.port=8081`
- Atau stop aplikasi yang menggunakan port 8080

---

## Testing Tanpa Database Built

Jika Anda ingin test tanpa database, jalankan:
```bash
mvn clean test
```

Tests menggunakan H2 in-memory database dan tidak memerlukan MySQL.

---

## Complete Checklist

- [ ] MySQL installed pada komputer
- [ ] MySQL service running (check di Services)
- [ ] Database `auth_jwt_db` created
- [ ] Tables created (auto by Hibernate atau manual)
- [ ] `application.properties` updated dengan credentials
- [ ] Compiled successfully: `mvn clean compile`
- [ ] Tests pass: `mvn clean test`
- [ ] Application runs: `mvn spring-boot:run`
- [ ] Swagger UI loads: `http://localhost:8080/swagger-ui.html`

---

## Next: Test Aplikasi

Setelah aplikasi running, buka Swagger UI:
1. Buka browser: `http://localhost:8080/swagger-ui.html`
2. Click pada endpoint (contoh: POST /api/auth/register)
3. Click "Try it out"
4. Isi request body dan click "Execute"

**Example Request:**
```json
{
  "username": "testuser",
  "email": "test@example.com",
  "password": "MyPassword123!",
  "confirmPassword": "MyPassword123!"
}
```

Password harus:
- Minimal 8 karakter
- Minimal 1 uppercase
- Minimal 1 lowercase
- Minimal 1 digit
- Minimal 1 special char (@$!%*?&)
