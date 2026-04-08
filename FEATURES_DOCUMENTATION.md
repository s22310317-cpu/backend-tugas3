# 📚 FEATURES DOCUMENTATION

Dokumentasi lengkap untuk semua fitur yang tersedia di project Authentication JWT Spring Security.

## 📑 Table of Contents

1. [Integration Tests](#integration-tests)
2. [Password Validation](#password-validation)
3. [Swagger/OpenAPI Documentation](#swaggeropenapi-documentation)
4. [Rate Limiting](#rate-limiting)
5. [Request/Response Logging](#requestresponse-logging)
6. [Environment Configuration](#environment-configuration)
7. [GitHub Actions CI/CD](#github-actions-cicd)

---

## Integration Tests

### Overview
Project ini sudah dilengkapi dengan comprehensive integration tests untuk memastikan API berfungsi dengan baik.

### Test Files

#### 1. AuthControllerTest (`src/test/java/com/tugas/controller/AuthControllerTest.java`)
Tests untuk endpoint authentication:
- ✅ Register user (success, duplicate username, invalid email)
- ✅ Login user (success, invalid password, user not found)
- ✅ Check username availability
- ✅ Check email availability

#### 2. UserControllerTest (`src/test/java/com/tugas/controller/UserControllerTest.java`)
Tests untuk user endpoints:
- ✅ Get user profile (success, unauthorized, invalid token)
- ✅ Test protected endpoint
- ✅ Public info endpoint
- ✅ Missing authorization header
- ✅ Expired token handling

#### 3. AuthServiceTest (`src/test/java/com/tugas/service/AuthServiceTest.java`)
Tests untuk business logic:
- ✅ Register user (success, duplicate username/email, validation)
- ✅ Get user by username
- ✅ Check if username/email exists

### Running Tests

```bash
# Run semua tests
mvn clean test

# Run tests dengan code coverage
mvn clean test jacoco:report

# Run specific test class
mvn test -Dtest=AuthControllerTest

# Run specific test method
mvn test -Dtest=AuthControllerTest#testRegisterSuccess
```

### Test Database
Tests menggunakan H2 in-memory database yang dikonfigurasi di `application-test.properties` untuk:
- Kecepatan: Database in-memory lebih cepat
- Isolation: Setiap test run menggunakan database yang fresh
- No cleanup: Database otomatis dihapus setelah test selesai

---

## Password Validation

### Overview
Fitur validasi password strength untuk memastikan password user aman.

### Password Requirements
Password harus memenuhi kriteria berikut:
- ✅ Minimal 8 karakter
- ✅ Minimal 1 huruf besar (A-Z)
- ✅ Minimal 1 huruf kecil (a-z)
- ✅ Minimal 1 angka (0-9)
- ✅ Minimal 1 karakter spesial (@$!%*?&)

### Implementation
File: `src/main/java/com/tugas/service/PasswordValidator.java`

### Usage Example
```java
@Autowired
private PasswordValidator passwordValidator;

// Check if password is valid
if (passwordValidator.isPasswordValid("MyPassword123!")) {
    // Password is strong enough
}

// Get detailed error message
String error = passwordValidator.getPasswordValidationError("weak");
// Returns: "Password harus mengandung minimal 1 huruf besar (A-Z)"
```

### Testing Password Validation
```bash
# Valid password
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "SecurePass123!",
    "confirmPassword": "SecurePass123!"
  }'

# Invalid password (error)
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "weak",
    "confirmPassword": "weak"
  }'
```

---

## Swagger/OpenAPI Documentation

### Overview
API documentation yang interaktif dan mudah digunakan dengan Swagger UI.

### Accessing Swagger UI
1. **Development**: `http://localhost:8080/swagger-ui.html`
2. **API Docs JSON**: `http://localhost:8080/v3/api-docs`

### Features
- ✅ Interactive API exploration
- ✅ Try-it-out feature untuk test endpoints
- ✅ Parameter validation
- ✅ Request/response examples
- ✅ Schema documentation

### Configuration
Files:
- `src/main/java/com/tugas/config/SwaggerConfig.java` - Main configuration
- `src/main/resources/application.properties` - Enable/disable settings

### Swagger UI Endpoints

#### Authentication Endpoints
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user
- `GET /api/auth/check-username/{username}` - Check username availability
- `GET /api/auth/check-email/{email}` - Check email availability

#### User Endpoints
- `GET /api/users/profile` - Get user profile (protected)
- `GET /api/users/test` - Test protected endpoint (protected)
- `GET /api/users/public/info` - Get public info (public)

### Disabling Swagger in Production
Edit `application-prod.properties`:
```properties
springdoc.swagger-ui.enabled=false
```

---

## Rate Limiting

### Overview
Rate limiting untuk melindungi API dari abuse dan brute force attacks.

### Configuration
- **Default limit**: 100 requests per 1 menit per IP address
- **Algorithm**: Token bucket algorithm (Bucket4j)
- **Per IP**: Setiap IP address punya bucket terpisah

### Files
- `src/main/java/com/tugas/config/RateLimiterConfig.java` - Rate limiter config
- `src/main/java/com/tugas/interceptor/RateLimitingInterceptor.java` - Interceptor

### Response Headers
Ketika rate limit exceeded:
```
HTTP/1.1 429 Too Many Requests
X-Rate-Limit-Retry-After-Seconds: 45
```

### Excluded Endpoints
Endpoints yang tidak terkena rate limiting:
- `/api/auth/register` - Free registration
- `/api/auth/login` - Free login attempt
- `/api/users/public/**` - Public endpoints
- `/swagger-ui/**` - Swagger documentation
- `/v3/api-docs/**` - API docs
- `/health` - Health check
- `/actuator/**` - Actuator endpoints

### Customization
Edit `application.properties`:
```properties
# Change requests per minute
app.rate-limit.requests-per-minute=50

# Disable rate limiting
app.rate-limit.enabled=false
```

### Testing Rate Limiting
```bash
# Run 101 requests in loop (akan error pada request ke-101)
for i in {1..101}; do
  curl -i http://localhost:8080/api/users/profile \
    -H "Authorization: Bearer YOUR_TOKEN"
done
```

---

## Request/Response Logging

### Overview
Automatic logging untuk semua HTTP requests dan responses untuk debugging dan monitoring.

### Features
- ✅ Unique request ID tracing
- ✅ Request method dan URI
- ✅ Response status code dan duration
- ✅ Sensitive header masking (Authorization, Password, Token, Secret, API-Key)
- ✅ Exception logging

### Files
`src/main/java/com/tugas/interceptor/RequestResponseLoggingInterceptor.java`

### Log Output Example
```
2024-04-08 10:30:45 - ==> REQUEST [ID: 550e8400-e29b-41d4-a716-446655440000] POST /api/auth/login
2024-04-08 10:30:45 - Request Headers: Authorization: ***MASKED*** content-type: application/json
2024-04-08 10:30:46 - <== RESPONSE [ID: 550e8400-e29b-41d4-a716-446655440000] Status: 200 Duration: 123 ms
```

### Configuration
Edit `application.properties`:
```properties
# Change logging level
logging.level.com.tugas=DEBUG

# Console pattern
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
```

---

## Environment Configuration

### Overview
Multi-environment configuration untuk development, staging, dan production.

### Profile Files
1. **application.properties** (default/development)
2. **application-dev.properties** (development specific)
3. **application-prod.properties** (production specific)

### Switching Profiles
```bash
# Development (default)
java -jar app.jar

# Development explicit
java -jar app.jar --spring.profiles.active=dev

# Production
java -jar app.jar --spring.profiles.active=prod
```

### Environment Variables

#### Critical Variables (JWT Secret)
```bash
# Set JWT secret (REQUIRED for production)
export JWT_SECRET="your-secret-key-here-min-32-chars"

# Set JWT expiration (optional, default: 86400000 = 24 hours)
export JWT_EXPIRATION="86400000"
```

#### Database Variables (Production)
```bash
export DATABASE_URL="jdbc:mysql://db-host:3306/auth_jwt_db?useSSL=true"
export DATABASE_USERNAME="db_user"
export DATABASE_PASSWORD="db_password"
```

### Development Environment
```properties
# application-dev.properties
spring.jpa.hibernate.ddl-auto=update
logging.level.com.tugas=DEBUG
springdoc.swagger-ui.enabled=true
app.rate-limit.requests-per-minute=100
```

### Production Environment
```properties
# application-prod.properties
spring.jpa.hibernate.ddl-auto=validate
logging.level.com.tugas=INFO
springdoc.swagger-ui.enabled=false
app.rate-limit.requests-per-minute=50
```

### Running with Environment Variables
```bash
# Linux/Mac
export JWT_SECRET="mySecretKey12345"
export DATABASE_PASSWORD="myDbPassword"
java -jar auth-jwt-spring-security-1.0.0.jar --spring.profiles.active=prod

# Windows PowerShell
$env:JWT_SECRET = "mySecretKey12345"
$env:DATABASE_PASSWORD = "myDbPassword"
java -jar auth-jwt-spring-security-1.0.0.jar --spring.profiles.active=prod

# Windows CMD
set JWT_SECRET=mySecretKey12345
set DATABASE_PASSWORD=myDbPassword
java -jar auth-jwt-spring-security-1.0.0.jar --spring.profiles.active=prod
```

---

## GitHub Actions CI/CD

### Overview
Automated CI/CD pipeline untuk automatic build, test, dan deploy.

### Pipeline Stages

#### 1. Build Job
- Compile code dengan Java 17 dan 21
- Run semua unit & integration tests
- Generate code coverage reports
- Build Docker image

#### 2. Security Check Job
- Run OWASP Dependency Check
- Scan Docker image dengan Trivy
- Upload security results

#### 3. SonarQube Job (optional)
- Code quality analysis
- Security vulnerability scanning

#### 4. Deploy Job (on main branch only)
- Deploy ke production environment
- Requires secrets configuration

### Workflow File
Location: `.github/workflows/ci-cd.yml`

### Setting Up GitHub Secrets
Untuk enable deployment, configure GitHub secrets:
1. Go to Settings → Secrets and variables → Actions
2. Add these secrets:
   - `DEPLOY_KEY` - SSH private key untuk deployment
   - `DEPLOY_SERVER` - Server address
   - `JWT_SECRET` - JWT secret key
   - `DATABASE_URL` - Production database URL
   - `DATABASE_USERNAME` - Database username
   - `DATABASE_PASSWORD` - Database password
   - `SONAR_TOKEN` - SonarQube token (optional)

### Workflow Triggers
- Push ke `main` atau `develop` branch
- Pull requests ke `main` atau `develop`

### Viewing Pipeline Results
1. Go to GitHub repository
2. Click "Actions" tab
3. View workflow runs dan detailed logs

### Customization
Edit `.github/workflows/ci-cd.yml` untuk:
- Change Java versions
- Add more test stages
- Configure deployment script
- Add notifications (Slack, Email, etc.)

---

## Summary

Dengan semua fitur ini, project sudah production-ready dengan:
- ✅ Comprehensive testing coverage
- ✅ Strong password validation
- ✅ Interactive API documentation
- ✅ Rate limiting protection
- ✅ Detailed logging
- ✅ Environment-specific configuration
- ✅ Automated CI/CD pipeline

Untuk questions atau issues, silakan check logs dan error messages yang detailed untuk debugging.
