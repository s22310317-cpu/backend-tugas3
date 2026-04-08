# Setup & Running Guide

Panduan lengkap untuk setup dan menjalankan project Login & Registrasi dengan Spring Security dan JWT Token.

## Prerequisites

Pastikan sudah install:

1. **Java 17 atau lebih tinggi**
   ```bash
   java -version
   ```

2. **Maven 3.6 atau lebih tinggi**
   ```bash
   mvn -version
   ```

3. **MySQL 5.7 atau lebih tinggi**
   - Download: https://dev.mysql.com/downloads/mysql/
   - Atau gunakan XAMPP: https://www.apachefriends.org/

## Step 1: Setup Database

### Opsi A: Menggunakan MySQL Command Line

1. Buka MySQL Command Prompt atau Terminal
2. Login ke MySQL:
   ```bash
   mysql -u root -p
   ```
   (Enter untuk password kosong, atau masukkan password jika ada)

3. Jalankan SQL script:
   ```sql
   SOURCE C:/Users/angga/Desktop/tugas back end/database.sql;
   ```

### Opsi B: Menggunakan MySQL Workbench

1. Buka MySQL Workbench
2. Klik "File" → "Run SQL Script"
3. Pilih file `database.sql`
4. Klik "Run"

### Opsi C: Automatic (via Hibernate)

Jika menggunakan Hibernate auto DDL, database akan otomatis created saat aplikasi run.

## Step 2: Configure Database Connection

Edit file: `src/main/resources/application.properties`

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/auth_jwt_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

**Untuk Windows XAMPP:**
- Username: `root`
- Password: `` (KOSONG)
- URL: `jdbc:mysql://localhost:3306/auth_jwt_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true`

## Step 3: Build Project

Buka Command Prompt/Terminal di folder project:

```bash
cd C:\Users\angga\Desktop\tugas back end

mvn clean install
```

Tunggu sampai selesai. Output terakhir seharusnya:
```
[INFO] BUILD SUCCESS
```

## Step 4: Run Application

### Opsi A: Dari Command Line

```bash
mvn spring-boot:run
```

### Opsi B: Dari IDE (IntelliJ IDEA / Eclipse)

1. Right-click pada `AuthJwtApplication.java`
2. Klik "Run 'AuthJwtApplication.main()'"

### Opsi C: Dari JAR File

```bash
mvn package
java -jar target/auth-jwt-spring-security-1.0.0.jar
```

Jika berhasil, akan melihat output seperti:

```
...
2024-01-15 10:30:45 - Starting AuthJwtApplication ...
2024-01-15 10:30:46 - Started AuthJwtApplication in 2.5 seconds (JVM running for 3.1 seconds)
2024-01-15 10:30:46 - Creating default roles...
2024-01-15 10:30:46 - Default roles created successfully
```

## Step 5: Test API

### Menggunakan Postman

1. Import Postman Collection: `Postman_Collection.json`
2. Jalankan requests

### Menggunakan cURL

#### Register User:
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d "{
    \"username\": \"testuser\",
    \"email\": \"test@example.com\",
    \"password\": \"Test@123\",
    \"confirmPassword\": \"Test@123\"
  }"
```

#### Login:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{
    \"username\": \"testuser\",
    \"password\": \"Test@123\"
  }"
```

#### Access Protected Endpoint:
```bash
curl -X GET http://localhost:8080/api/users/profile \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

### Menggunakan Browser

1. Open browser → http://localhost:8080/api/users/public/info
2. Seharusnya muncul response JSON

## Troubleshooting

### Error: "Connection refused"
- Pastikan MySQL sudah running
- Check `application.properties` - URL database sudah benar?

### Error: "Access denied for user 'root'@'localhost'"
- Pastikan MySQL credentials benar di `application.properties`
- Untuk XAMPP: username `root`, password kosong

### Error: "Build Failure"
```bash
# Clean cache dan rebuild
mvn clean
mvn install
```

### Port 8080 Already in Use
Edit `application.properties`:
```properties
server.port=8081
```

### Module Not Found Exception
```bash
# Update Maven repository
mvn clean install -U
```

## Environment Variables (Optional)

Untuk production, gunakan environment variables instead of hardcoding credentials.

Buat file `.env`:
```
DB_URL=jdbc:mysql://localhost:3306/auth_jwt_db
DB_USERNAME=root
DB_PASSWORD=
JWT_SECRET=your_very_secret_key_here
JWT_EXPIRATION=86400000
```

Edit `application.properties`:
```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
app.jwt.secret=${JWT_SECRET}
app.jwt.expiration=${JWT_EXPIRATION}
```

## Verify Installation

Jika setup berhasil, test endpoints berikut:

1. **Public Endpoint** (tidak perlu token):
   ```
   GET http://localhost:8080/api/users/public/info
   ```

2. **Register New User**:
   ```
   POST http://localhost:8080/api/auth/register
   
   Body:
   {
     "username": "john",
     "email": "john@example.com",
     "password": "SecurePass123",
     "confirmPassword": "SecurePass123"
   }
   ```

3. **Login**:
   ```
   POST http://localhost:8080/api/auth/login
   
   Body:
   {
     "username": "john",
     "password": "SecurePass123"
   }
   ```
   
   Akan dapat token. Copy token tersebut.

4. **Access Protected Endpoint** (dengan token):
   ```
   GET http://localhost:8080/api/users/profile
   
   Header:
   Authorization: Bearer <TOKEN_DARI_LOGIN>
   ```

## File Structure

```
tugas back end/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/tugas/
│   │   │       ├── AuthJwtApplication.java
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── security/
│   │   │       ├── entity/
│   │   │       ├── repository/
│   │   │       ├── dto/
│   │   │       ├── exception/
│   │   │       ├── config/
│   │   │       └── init/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
├── README.md
├── SETUP_GUIDE.md (file ini)
├── database.sql
├── Postman_Collection.json
├── .gitignore
└── target/
    └── auth-jwt-spring-security-1.0.0.jar
```

## Next Steps

1. ✅ Setup database dan credentials
2. ✅ Run application
3. ✅ Test endpoints dengan Postman
4. 📝 Implement frontend
5. 🔒 Configure production security settings
6. 📊 Add logging dan monitoring
7. 🧪 Add unit tests

## Support

Jika ada pertanyaan atau issue:

1. Check logs di console
2. Baca error message dengan seksama
3. Verify bahwa semua prerequisites sudah installed
4. Cek database connection
5. Pastikan port 8080 tidak sedang digunakan

---

**Happy coding!** 🚀
