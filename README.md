# Login & Registrasi dengan Spring Security dan JWT Token

Ini adalah project API RESTful untuk sistem Login & Registrasi menggunakan Spring Boot, Spring Security, dan JWT Token.

## Fitur Utama

✅ Registrasi user baru dengan validasi  
✅ Login dengan username/password  
✅ JWT Token generation dan validation  
✅ Role-based access control (RBAC)  
✅ Password encryption menggunakan BCrypt  
✅ Global exception handling  
✅ Request validation menggunakan Jakarta Validation  

## Prerequisites

- Java 17 atau lebih tinggi
- Maven 3.6 atau lebih tinggi
- MySQL 5.7 atau lebih tinggi

## Setup Instructions

### 1. Clone atau Download Project

```bash
cd c:\Users\angga\Desktop\tugas back end
```

### 2. Create Database

Buat database MySQL dengan nama `auth_jwt_db`:

```sql
CREATE DATABASE auth_jwt_db;
```

### 3. Configure Database Connection

Edit file `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/auth_jwt_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### 4. Ubah JWT Secret Key (PENTING untuk Production)

Edit file `src/main/resources/application.properties`:

```properties
app.jwt.secret=YOUR_SECRET_KEY_YANG_PANJANG_DAN_AMAN
app.jwt.expiration=86400000
```

### 5. Run Application

```bash
mvn clean install
mvn spring-boot:run
```

atau jika menggunakan IDE, langsung run `AuthJwtApplication.java`

Server akan berjalan di `http://localhost:8080`

## API Endpoints

### 1. Register User

**Endpoint:** `POST /api/auth/register`

**Request Body:**
```json
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "password123",
  "confirmPassword": "password123"
}
```

**Success Response (201 Created):**
```json
{
  "success": true,
  "message": "Registrasi berhasil",
  "data": {
    "success": true,
    "message": "User berhasil didaftarkan",
    "userId": 1,
    "username": "johndoe",
    "email": "john@example.com"
  }
}
```

### 2. Login User

**Endpoint:** `POST /api/auth/login`

**Request Body:**
```json
{
  "username": "johndoe",
  "password": "password123"
}
```

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Login berhasil",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJhdXRob3JpdGllcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9VU0VSIn1dLCJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNjQwMDEyMzQ1LCJleHAiOjE2NDAwOTg3NDV9.xxx",
    "tokenType": "Bearer",
    "id": 1,
    "username": "johndoe",
    "email": "john@example.com",
    "roles": ["USER"]
  }
}
```

### 3. Check Username Available

**Endpoint:** `GET /api/auth/check-username/{username}`

**Response:**
```json
{
  "success": true,
  "message": "Username tersedia"
}
```

### 4. Check Email Available

**Endpoint:** `GET /api/auth/check-email/{email}`

**Response:**
```json
{
  "success": true,
  "message": "Email tersedia"
}
```

### 5. Get Current User Profile (Protected)

**Endpoint:** `GET /api/users/profile`

**Headers Required:**
```
Authorization: Bearer <JWT_TOKEN>
```

**Response:**
```json
{
  "success": true,
  "message": "User profile berhasil diambil",
  "data": {
    "id": 1,
    "username": "johndoe",
    "email": "john@example.com",
    "roles": [
      {
        "id": 1,
        "name": "USER"
      }
    ],
    "createdAt": "2024-01-01T10:30:00",
    "updatedAt": "2024-01-01T10:30:00"
  }
}
```

### 6. Test Protected Endpoint

**Endpoint:** `GET /api/users/test`

**Headers Required:**
```
Authorization: Bearer <JWT_TOKEN>
```

**Response:**
```json
{
  "success": true,
  "message": "Endpoint ini hanya bisa diakses oleh authenticated user"
}
```

### 7. Public Info Endpoint

**Endpoint:** `GET /api/users/public/info`

**Response:**
```json
{
  "success": true,
  "message": "Ini adalah public endpoint"
}
```

## Error Handling

### Validation Error (400)
```json
{
  "username": "Username tidak boleh kosong",
  "email": "Email harus valid"
}
```

### Bad Request (400)
```json
{
  "success": false,
  "message": "Username sudah digunakan"
}
```

### Resource Not Found (404)
```json
{
  "success": false,
  "message": "User tidak ditemukan dengan username: 'xxx'"
}
```

### Unauthorized (401)
```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Authentication required",
  "path": "/api/users/profile"
}
```

## Testing dengan Postman

### Step 1: Register User
- Method: POST
- URL: http://localhost:8080/api/auth/register
- Body (raw JSON):
```json
{
  "username": "testuser",
  "email": "test@example.com",
  "password": "Test@123",
  "confirmPassword": "Test@123"
}
```

### Step 2: Login User
- Method: POST
- URL: http://localhost:8080/api/auth/login
- Body (raw JSON):
```json
{
  "username": "testuser",
  "password": "Test@123"
}
```
- Copy token dari response

### Step 3: Access Protected Endpoint
- Method: GET
- URL: http://localhost:8080/api/users/profile
- Headers:
  - Key: Authorization
  - Value: Bearer <paste_token_here>

## Project Structure

```
src/
├── main/
│   ├── java/com/tugas/
│   │   ├── AuthJwtApplication.java       # Main application
│   │   ├── controller/
│   │   │   ├── AuthController.java       # Login/Register endpoints
│   │   │   └── UserController.java       # Protected endpoints
│   │   ├── service/
│   │   │   ├── AuthService.java          # Auth business logic
│   │   │   └── CustomUserDetailsService.java
│   │   ├── security/
│   │   │   ├── JwtTokenProvider.java     # JWT utilities
│   │   │   ├── JwtAuthenticationFilter.java
│   │   │   └── JwtAuthenticationEntryPoint.java
│   │   ├── entity/
│   │   │   ├── User.java
│   │   │   └── Role.java
│   │   ├── repository/
│   │   │   ├── UserRepository.java
│   │   │   └── RoleRepository.java
│   │   ├── dto/
│   │   │   ├── LoginRequest.java
│   │   │   ├── LoginResponse.java
│   │   │   ├── RegisterRequest.java
│   │   │   ├── RegisterResponse.java
│   │   │   └── ApiResponse.java
│   │   ├── exception/
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   ├── ResourceNotFoundException.java
│   │   │   └── BadApiRequest.java
│   │   ├── config/
│   │   │   └── SecurityConfig.java       # Spring Security configuration
│   │   └── init/
│   │       └── DataInitializer.java      # Initialize roles
│   └── resources/
│       └── application.properties        # Configuration
└── pom.xml                               # Maven dependencies
```

## Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE user_roles (
    user_id BIGINT,
    role_id BIGINT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);
```

## Security Best Practices

⚠️ **UNTUK PRODUCTION:**

1. **Ubah JWT Secret Key** ke string yang panjang dan aman
2. **Ubah database credentials** di environment variable
3. **Enable HTTPS** pada production
4. **Implementasi rate limiting** untuk endpoint login
5. **Implementasi token refresh mechanism**
6. **Audit logging** untuk aktivitas login/logout
7. **Email verification** untuk proses registrasi

## Dependencies

- Spring Boot 3.1.5
- Spring Security
- Spring Data JPA
- MySQL Connector
- JJWT 0.12.3 (JWT library)
- Lombok
- Jakarta Validation

## Troubleshooting

### Error: "Access denied to user 'root'@'localhost'"
Pastikan MySQL sudah running dan credentials di `application.properties` sudah benar.

### Error: "Syntax error in SQL statement"
Update `spring.jpa.hibernate.ddl-auto=update` di properties file.

### Token Expired Error
Ubah `app.jwt.expiration` di `application.properties` dengan nilai yang lebih besar (dalam milliseconds).

### Port Already in Use
Ubah `server.port` di `application.properties` ke port yang tersedia.

## License

MIT License

## Author

Tugas Backend - 2024
