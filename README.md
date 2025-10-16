# User Management Application

A Spring Boot application for user management with JWT authentication and token revocation capabilities.

## Features

- User registration and login
- JWT-based authentication
- Role-based access control (Customer, Mitra, Admin)
- Token storage in database for revocation
- Scheduled cleanup of expired tokens
- User profile management
- Admin user management

## Technologies Used

- Java 17
- Spring Boot 3.x
- Spring Security
- Spring Data JPA
- JWT (JSON Web Tokens)
- H2 Database (configurable)
- Maven

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+

### Installation

1. Clone the repository:

   ```bash
   git clone <repository-url>
   cd user-management
   ```

2. Build the project:

   ```bash
   mvn clean install
   ```

3. Configure application.yml
   ```
   spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/user_management_db
    username: postgres
    password: yourpassword
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect
    show-sql: true
  profiles:
    active: default

server:
  port: 8080

jwt:
  secret: mySuperSecureJwtSecretKeyThatIsAtLeast256BitsLong123456789

springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
    enabled: true

---
spring:
  config:
    activate:
      on-profile: docker
  springdoc:
    api-docs:
      path: /api-docs
    swagger-ui:
      path: /swagger-ui.html
      enabled: true
  datasource:
    url: jdbc:postgresql://db:5432/user_management
    username: user
    password: password
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect
    show-sql: true

server:
  port: 8080

jwt:
  secret: mySuperSecureJwtSecretKeyThatIsAtLeast256BitsLong123456789

springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html

   ```
4. Create Database in PostgreSql
```
CREATE DATABASE user_management_db;
```

5. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`.

## API Endpoints

### Authentication

- `POST /auth/register` - Register a new user
- `POST /auth/login` - Login and receive JWT token
- `POST /auth/logout` - Logout (revoke tokens)
- `POST /auth/refresh` - Refresh JWT token

### User Profile

- `GET /profiles/me` - Get current user profile
- `PUT /profiles/me` - Update user profile
- `DELETE /profiles/me` - Delete user profile

### Admin (Admin role required)

- `GET /admin/users` - Get all users with pagination
- `GET /admin/users/{id}` - Get user information based on id
- `PUT /admin/users/{id}` - Update data user
- `PUT /admin/users/{id}/status` - Update user status (ACTIVE OR INACTIVE)
- `DELETE /admin/users/{id}`  - Remove user (with profile user)

## Database Schema

### Users Table

- id (Primary Key)
- email (Unique)
- password (Encrypted)
- role (CUSTOMER, MITRA, ADMIN)
- status (ACTIVE, INACTIVE)

### User Profiles Table

- id (Primary Key)
- user_id (Foreign Key)
- full_name
- address
- phone

### Tokens Table

- id (Primary Key)
- token (Unique)
- user_id (Foreign Key)
- revoked (Boolean)
- expiry_date (DateTime)

## Security

- JWT tokens are stored in the database for revocation capability
- Tokens are automatically revoked on logout or new login
- Expired tokens are cleaned up periodically
- Role-based access control implemented

## Configuration

Application properties can be configured in `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password:

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

jwt:
  secret: your-jwt-secret-key-here
```

## Testing

Run the tests:

```bash
mvn test
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.
