# 📋 UPDATED SETUP GUIDE (with New Features)

Complete setup guide untuk menjalankan project dengan semua features baru.

## Prerequisites

- **Java**: 17 atau lebih tinggi
- **Maven**: 3.6 atau lebih tinggi  
- **MySQL**: 5.7 atau lebih tinggi
- **Git**: Untuk version control (optional)

---

## 1️⃣ SETUP DATABASE

### Database Setup

#### Option A: Using Command Line
```bash
cd "c:\Users\angga\Desktop\tugas back end"
mysql -u root -p < database.sql
```
(Press Enter jika password kosong)

#### Option B: Using Batch Script
```bash
Double-click: setup_db.bat
```

#### Option C: Manual
```sql
CREATE DATABASE IF NOT EXISTS auth_jwt_db;
USE auth_jwt_db;

-- Copy and paste queries dari database.sql
```

### Verify Database
```bash
mysql -u root
SHOW DATABASES;
USE auth_jwt_db;
SHOW TABLES;
EXIT;
```

---

## 2️⃣ CONFIGURE APPLICATION

### Configure Database Connection

Edit file: `src/main/resources/application.properties`

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/auth_jwt_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD_HERE
```

### (NEW) Environment Variables Setup

Untuk production, gunakan environment variables instead of hardcoding:

#### Windows PowerShell
```powershell
$env:JWT_SECRET = "mySecureKey123456789"
$env:JWT_EXPIRATION = "86400000"
$env:DATABASE_PASSWORD = "myDbPassword"

# Run application
java -jar target/auth-jwt-spring-security-1.0.0.jar --spring.profiles.active=prod
```

#### Windows CMD
```cmd
set JWT_SECRET=mySecureKey123456789
set JWT_EXPIRATION=86400000
set DATABASE_PASSWORD=myDbPassword

java -jar target/auth-jwt-spring-security-1.0.0.jar --spring.profiles.active=prod
```

#### Linux/Mac Bash
```bash
export JWT_SECRET="mySecureKey123456789"
export JWT_EXPIRATION="86400000"
export DATABASE_PASSWORD="myDbPassword"

java -jar target/auth-jwt-spring-security-1.0.0.jar --spring.profiles.active=prod
```

---

## 3️⃣ BUILD PROJECT

### Option A: Using Maven Commands

#### Clean Build
```bash
cd "c:\Users\angga\Desktop\tugas back end"
mvn clean package
```

#### Build without Tests (faster)
```bash
mvn clean package -DskipTests
```

#### Build with Test Coverage
```bash
mvn clean package jacoco:report
```

### Option B: Using IDE
- Right-click project → Run As → Maven build
- Goals: `clean package`

### Verify Build
```bash
# Check if JAR created successfully
dir target\auth-jwt-spring-security-1.0.0.jar
```

---

## 4️⃣ RUN TESTS

### Run All Tests
```bash
mvn clean test
```

### Run Tests with Coverage Report
```bash
mvn clean test jacoco:report

# Open coverage report
# File: target/site/jacoco/index.html
```

### Run Specific Test Class
```bash
mvn test -Dtest=AuthControllerTest
mvn test -Dtest=UserControllerTest
mvn test -Dtest=AuthServiceTest
```

### Test Results
```
BUILD SUCCESS - semua tests passed
BUILD FAILURE - ada test yang gagal

# Check logs untuk error details
```

---

## 5️⃣ RUN APPLICATION

### Option A: Development Mode (Maven)
```bash
mvn spring-boot:run

# Application starts at: http://localhost:8080
```

### Option B: Using JAR File
```bash
# First, build the project
mvn clean package

# Then run
java -jar target/auth-jwt-spring-security-1.0.0.jar
```

### Option C: Using Batch Script
```bash
Double-click: start_app.bat
```

### Option D: Production Mode
```bash
# Set environment variables first
set JWT_SECRET=your-secret-key-here

# Then run with prod profile
java -jar target/auth-jwt-spring-security-1.0.0.jar --spring.profiles.active=prod
```

---

## 6️⃣ ACCESS FEATURES

### 🌐 Swagger API Documentation
**URL**: `http://localhost:8080/swagger-ui.html`

**Features**:
- Interactive API exploration
- Try-it-out untuk test endpoints
- View all endpoints dengan parameter details
- Request/response examples

### 📝 API Docs JSON
**URL**: `http://localhost:8080/v3/api-docs`

### 🧪 Test Registration (dengan Password Validation)

**Valid Password Example**: `MyPassword123!`
- Minimal 8 karakter ✅
- Minimal 1 uppercase (MyPassword) ✅
- Minimal 1 lowercase (yPassword) ✅
- Minimal 1 digit (3) ✅
- Minimal 1 special char (!) ✅

**Using Swagger UI**:
1. Open Swagger UI: `http://localhost:8080/swagger-ui.html`
2. Find `POST /api/auth/register`
3. Click "Try it out"
4. Fill in request body:
```json
{
  "username": "testuser",
  "email": "test@example.com",
  "password": "MyPassword123!",
  "confirmPassword": "MyPassword123!"
}
```
5. Click "Execute"

**Using curl**:
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d "{
    \"username\": \"testuser\",
    \"email\": \"test@example.com\",
    \"password\": \"MyPassword123!\",
    \"confirmPassword\": \"MyPassword123!\"
  }"
```

### 🔐 Test Login

**Using Swagger UI**:
1. Open Swagger UI
2. Find `POST /api/auth/login`
3. Click "Try it out"
4. Fill in:
```json
{
  "username": "testuser",
  "password": "MyPassword123!"
}
```
5. Copy the `token` dari response

### 🛡️ Test Protected Endpoint with Token

**Using Swagger UI**:
1. Find `GET /api/users/profile`
2. Click "Authorize" button (top right)
3. Paste token: `Bearer YOUR_TOKEN_HERE`
4. Click "Execute"

**Using curl**:
```bash
curl -X GET http://localhost:8080/api/users/profile \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## 7️⃣ (NEW) MONITORING & LOGGING

### View Logs

Logs akan ditampilkan di console/terminal dengan format:

```
2024-04-08 10:30:45 - ==> REQUEST [ID: abc123] POST /api/auth/login
2024-04-08 10:30:45 - Request Headers: Authorization: ***MASKED*** ...
2024-04-08 10:30:46 - <== RESPONSE [ID: abc123] Status: 200 Duration: 123 ms
```

### Change Log Level
Edit `application.properties`:
```properties
# More verbose
logging.level.com.tugas=DEBUG

# Less verbose
logging.level.com.tugas=WARN
```

### (NEW) Rate Limiting

Rate limiting otomatis apply ke API endpoints:
- **Default limit**: 100 requests per 1 menit per IP
- **Excluded**: Register, login, public endpoints

**Testing Rate Limit** (simulate 101 requests):
```bash
for i in {1..101}; do
  curl http://localhost:8080/api/users/profile \
    -H "Authorization: Bearer TOKEN" \
    -w "\nRequest %d\n"
done

# Request ke-101 akan error:
# HTTP 429 Too Many Requests
```

---

## 8️⃣ (NEW) CI/CD PIPELINE

### GitHub Actions Setup

1. Push code to GitHub
2. CI/CD pipeline automatically runs on:
   - Every push to `main` oder `develop`
   - Every pull request

3. Pipeline stages:
   - ✅ Build (Java 17 + 21)
   - ✅ Test (unit + integration)
   - ✅ Security Scan (OWASP, Trivy)
   - ✅ Code Quality (SonarQube optional)
   - ✅ Deploy (auto-deploy to production)

### Configure Deployment

1. Go to GitHub repository settings
2. Secrets and variables → Actions
3. Add secrets:
   - `JWT_SECRET` - Your JWT secret
   - `DATABASE_URL` - Production database URL
   - `DATABASE_USERNAME` - DB username
   - `DATABASE_PASSWORD` - DB password
   - `DEPLOY_KEY` - SSH key untuk deployment
   - `DEPLOY_SERVER` - Server address

### View Pipeline Results
1. Go to GitHub → Actions tab
2. Click workflow run
3. View detailed logs

---

## 9️⃣ PROJECT STRUCTURE

```
tugas back end/
├── .github/
│   └── workflows/
│       └── ci-cd.yml                          [GitHub Actions CI/CD]
├── src/
│   ├── main/
│   │   ├── java/com/tugas/
│   │   │   ├── AuthJwtApplication.java
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── SwaggerConfig.java         [NEW - Swagger UI]
│   │   │   │   ├── RateLimiterConfig.java     [NEW - Rate Limiting]
│   │   │   │   └── WebConfig.java             [NEW - Interceptors]
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   └── UserController.java
│   │   │   ├── dto/
│   │   │   │   ├── AvailabilityResponse.java  [NEW]
│   │   │   │   ├── PublicUserInfoResponse.java [NEW]
│   │   │   │   └── ...other DTOs
│   │   │   ├── entity/, exception/, repository/
│   │   │   ├── interceptor/
│   │   │   │   ├── RateLimitingInterceptor.java [NEW]
│   │   │   │   └── RequestResponseLoggingInterceptor.java [NEW]
│   │   │   ├── security/
│   │   │   │   ├── JwtTokenProvider.java      [UPDATED]
│   │   │   │   └── ...other security classes
│   │   │   └── service/
│   │   │       ├── AuthService.java           [UPDATED]
│   │   │       ├── CustomUserDetailsService.java
│   │   │       └── PasswordValidator.java     [NEW]
│   │   └── resources/
│   │       ├── application.properties         [UPDATED]
│   │       ├── application-dev.properties     [NEW]
│   │       └── application-prod.properties    [NEW]
│   └── test/
│       ├── java/com/tugas/
│       │   ├── controller/
│       │   │   ├── AuthControllerTest.java    [NEW - 10 tests]
│       │   │   └── UserControllerTest.java    [NEW - 7 tests]
│       │   ├── service/
│       │   │   └── AuthServiceTest.java       [NEW - 10 tests]
│       │   ├── dto/, entity/, security/       [Existing tests]
│       └── resources/
│           └── application-test.properties    [NEW]
├── pom.xml                                     [UPDATED - 4 new deps]
├── FEATURES_DOCUMENTATION.md                  [NEW]
├── NEW_FEATURES_SUMMARY.md                    [NEW]
├── SETUP_GUIDE_UPDATED.md                     [NEW - this file]
├── README.md, QUICK_START.md, PROJECT_SUMMARY.md
└── database.sql, Postman_Collection.json
```

---

## 🔟 TROUBLESHOOTING

### Build Fails
```bash
# Clean dan rebuild
mvn clean install

# Check Java version
java -version

# Check Maven version
mvn -version
```

### Tests Failing
```bash
# Run tests dengan verbose output
mvn test -e -X

# Run single test
mvn test -Dtest=AuthControllerTest#testRegisterSuccess

# Check H2 database config in application-test.properties
```

### Application Won't Start
```bash
# Check if port 8080 is in use
netstat -ano | findstr :8080

# Use different port
java -jar app.jar --server.port=8081
```

### Swagger UI Not Loading
```bash
# Check if dependency installed
mvn dependency:tree | grep springdoc

# Verify URL: http://localhost:8080/swagger-ui.html
# Not: http://localhost:8080/swagger-ui (missing .html)
```

### Password Validation Too Strict
Example valid passwords:
- `MyPassword123!`
- `SecurePass@2024`
- `Admin123$`

Requirements:
- 8+ characters
- At least 1 uppercase
- At least 1 lowercase
- At least 1 digit
- At least 1 special char (@$!%*?&)

### Rate Limit Too Strict
Edit `application.properties`:
```properties
# Increase limit for development
app.rate-limit.requests-per-minute=200
```

---

## ✅ VERIFICATION CHECKLIST

After setup, verify:

- [ ] Database created successfully
- [ ] Application starts without errors
- [ ] Swagger UI accessible at http://localhost:8080/swagger-ui.html
- [ ] Can register new user with strong password
- [ ] Can login successfully
- [ ] Can access protected endpoint with JWT token
- [ ] Tests pass: `mvn clean test`
- [ ] Logs visible in console with request/response
- [ ] Rate limiting works (101+ requests error)

---

## 📚 DOCUMENTATION

For more details, refer to:

- **[NEW_FEATURES_SUMMARY.md](NEW_FEATURES_SUMMARY.md)** - Overview of new features
- **[FEATURES_DOCUMENTATION.md](FEATURES_DOCUMENTATION.md)** - Detailed feature documentation
- **[README.md](README.md)** - Original project README
- **[QUICK_START.md](QUICK_START.md)** - Quick start guide
- **[pom.xml](pom.xml)** - Dependencies and build config

---

## 🆘 NEED HELP?

1. **Check logs** - Console akan menampilkan detailed error messages
2. **Read documentation** - FEATURES_DOCUMENTATION.md has troubleshooting section
3. **Review code** - Check test files untuk example usage
4. **Run tests** - `mvn clean test` untuk verify setup

---

**Last Updated**: April 8, 2026  
**Project**: Auth JWT Spring Security  
**Version**: 1.0.0
