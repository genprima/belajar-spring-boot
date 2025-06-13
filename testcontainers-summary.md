# TestContainers Learning Summary

## What is TestContainers?
TestContainers is a Java library that provides lightweight, throwaway instances of common databases, Selenium web browsers, or anything else that can run in a Docker container.

## Key Components

### 1. Dependencies
```xml
<!-- TestContainers Core -->
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>testcontainers</artifactId>
    <version>1.19.7</version>
    <scope>test</scope>
</dependency>

<!-- PostgreSQL Module -->
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>postgresql</artifactId>
    <version>1.19.7</version>
    <scope>test</scope>
</dependency>

<!-- JUnit Integration -->
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>1.19.7</version>
    <scope>test</scope>
</dependency>
```

### 2. Test Layers
1. **Repository Tests**
   - Tests database operations
   - Uses real PostgreSQL container
   - Tests JPA/Hibernate mappings

2. **Service Tests**
   - Tests business logic
   - Uses real database through repository
   - Tests service methods

3. **Resource/Controller Tests**
   - Tests HTTP endpoints
   - Uses REST Assured
   - Tests API contracts

## How It Works

### 1. Container Lifecycle
```java
@Container
static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");
```
- Container starts before tests
- Container stops after tests
- Each test class gets its own container

### 2. Database Connection
```java
@DynamicPropertySource
static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
}
```

## Testing Approaches

### 1. With TestContainers (Integration Testing)
```java
@SpringBootTest
@Testcontainers
class AuthorResourceTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");
    
    @Autowired
    private AuthorService authorService;  // Real service with real database
}
```

### 2. Without TestContainers (Unit Testing)
```java
@SpringBootTest
class AuthorResourceTest {
    @MockBean
    private AuthorService authorService;  // Mocked service
}
```

## Docker Environment

### 1. Test Environment (docker-compose.test.yml)
- Temporary containers
- Clean database for each test
- Gets cleaned up after tests

### 2. Production Environment (docker-compose.yml)
- Persistent containers
- Keeps data between restarts
- Used for development/production

## Docker Compose Configuration

### 1. Test Environment (docker-compose.test.yml)
```yaml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=test
    # No database service needed - TestContainers handles it!

networks:
  backend_class-network:
    driver: bridge
```

> **Important Note**: The test Docker Compose file does NOT include a database service because TestContainers manages the database container automatically. This is different from the production environment where we need a persistent database.

### 2. Production Environment (docker-compose.yml)
```yaml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/proddb
      - SPRING_DATASOURCE_USERNAME=prod
      - SPRING_DATASOURCE_PASSWORD=prod
    depends_on:
      db:
        condition: service_healthy
    volumes:
      - app-data:/app/data

  db:
    image: postgres:16-alpine
    ports:
      - "5432:5432"
    environment:
      - POSTGRES_DB=proddb
      - POSTGRES_USER=prod
      - POSTGRES_PASSWORD=prod
    volumes:
      - postgres-data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U prod"]
      interval: 5s
      timeout: 5s
      retries: 5

volumes:
  app-data:
  postgres-data:
```

### 3. Key Differences Between Test and Production Compose

1. **Database Management**
   - Test: No database service in compose file - TestContainers handles it
   - Production: Dedicated database service with persistence

2. **Container Lifecycle**
   - Test: Database container created/destroyed per test class
   - Production: Database container runs continuously

3. **Data Persistence**
   - Test: No volumes needed - fresh database for each test
   - Production: Uses named volumes for data persistence

4. **Environment Variables**
   - Test: Minimal configuration - TestContainers sets database properties
   - Production: Full configuration for all services

### 4. How TestContainers Works with Docker Compose

1. **Test Execution Flow**:
   ```
   Test Class Starts
   ↓
   TestContainers creates PostgreSQL container
   ↓
   Spring Boot connects to TestContainers database
   ↓
   Tests run
   ↓
   TestContainers destroys container
   ```

2. **Database Configuration**:
   - TestContainers automatically sets:
     - Database URL
     - Username
     - Password
     - Port
   - Spring Boot test profile uses these settings

3. **Benefits**:
   - No port conflicts
   - Isolated test databases
   - Automatic cleanup
   - No need to manage database state

## Best Practices

1. **Isolation**
   - Each test class has its own container
   - Tests don't affect each other
   - Clean database for each test

2. **Performance**
   - Containers are reused when possible
   - Cleanup happens automatically
   - Tests run in parallel when possible

3. **Maintenance**
   - Keep test containers lightweight
   - Use specific versions
   - Clean up resources properly

## Common Use Cases

1. **Database Testing**
   - Test database operations
   - Test migrations
   - Test data integrity

2. **API Testing**
   - Test REST endpoints
   - Test with real database
   - Test full application flow

3. **Integration Testing**
   - Test multiple components
   - Test with real dependencies
   - Test complete workflows

## Benefits

1. **Realistic Testing**
   - Tests with real database
   - Tests complete application flow
   - Tests production-like environment

2. **Isolation**
   - Tests don't affect each other
   - Clean environment for each test
   - No external dependencies

3. **Automation**
   - Automatic container management
   - Automatic cleanup
   - Easy to run in CI/CD

## Limitations

1. **Performance**
   - Slower than unit tests
   - Needs Docker
   - More resource intensive

2. **Complexity**
   - More setup required
   - More dependencies
   - More maintenance

## Spring Boot Test Configuration

### 1. Auto-Configuration Report
When running tests, Spring Boot shows which configurations are active. Here's what happens:

```
Positive matches:
-----------------
   DataSourceAutoConfiguration matched:
      - Found required classes for database support

   DataSourceConfiguration.Hikari matched:
      - Using HikariCP as connection pool
      - Default database configuration

Negative matches:
-----------------
   FlywayAutoConfiguration:
      - No Flyway dependency found
   
   LiquibaseAutoConfiguration:
      - No Liquibase dependency found
```

### 2. Test Database Configuration
1. **Default Behavior**:
   - Spring Boot automatically configures HikariCP
   - Uses in-memory database if no configuration provided
   - TestContainers overrides these settings

2. **TestContainers Integration**:
   ```java
   @DynamicPropertySource
   static void configureProperties(DynamicPropertyRegistry registry) {
       registry.add("spring.datasource.url", postgres::getJdbcUrl);
       registry.add("spring.datasource.username", postgres::getUsername);
       registry.add("spring.datasource.password", postgres::getPassword);
   }
   ```
   - Overrides default database settings
   - Uses TestContainers PostgreSQL instance
   - Configures connection pool automatically

3. **Connection Pool**:
   - HikariCP is used by default
   - Optimized for testing
   - Automatic cleanup after tests

### 3. Test Profile Activation
```yaml
spring:
  config:
    activate:
      on-profile: test
  datasource:
    # These will be overridden by TestContainers
    url: jdbc:postgresql://localhost:5432/testdb
    username: test
    password: test
  jpa:
    hibernate:
      ddl-auto: create-drop  # Clean database for each test
```

1. **Profile Loading**:
   - Test profile is activated automatically
   - Database settings are overridden by TestContainers
   - JPA is configured for testing

2. **Database Cleanup**:
   - `ddl-auto: create-drop` ensures clean state
   - Tables are recreated for each test
   - No data persists between tests

### 4. Common Test Configuration Issues

1. **Missing Dependencies**:
   - Flyway/Liquibase not found (if not needed)
   - Can be ignored if not using migrations
   - Add dependencies if needed

2. **Connection Pool**:
   - HikariCP is default and sufficient
   - No need to change for testing
   - Automatically configured

3. **Database Cleanup**:
   - TestContainers handles container cleanup
   - JPA handles table cleanup
   - No manual cleanup needed

## Conclusion
TestContainers provides a powerful way to test applications with real dependencies in an isolated environment. It's particularly useful for integration testing and ensuring your application works with real databases and services.

## Running Tests

### 1. Running All Tests
```bash
# Run all tests
./mvnw test

# Run with test profile
./mvnw test -Dspring.profiles.active=test

# Run with detailed output
./mvnw test -Dtest=* -Dsurefire.useFile=false
```

### 2. Running Specific Tests
```bash
# Run specific test class
./mvnw test -Dtest=AuthorRepositoryTest

# Run specific test method
./mvnw test -Dtest=AuthorRepositoryTest#shouldSaveAuthor

# Run tests in a package
./mvnw test -Dtest="com.nostratech.spring.repository.*"
```

### 3. Test Categories
```bash
# Run only unit tests
./mvnw test -Dtest=*UnitTest

# Run only integration tests
./mvnw test -Dtest=*IT

# Run only repository tests
./mvnw test -Dtest=*RepositoryTest
```

### 4. Test Selection in IDE
1. **IntelliJ IDEA**:
   - Right-click on test class/method
   - Select "Run" or "Debug"
   - Use keyboard shortcuts:
     - `Ctrl+Shift+F10` (Run)
     - `Ctrl+Shift+D` (Debug)

2. **VS Code**:
   - Click "Run Test" above test class/method
   - Use Test Explorer view
   - Use keyboard shortcuts:
     - `F5` (Run)
     - `Shift+F5` (Debug)

### 5. Test Configuration Options
```bash
# Run with specific JVM arguments
./mvnw test -DargLine="-Xmx512m"

# Run with specific Spring profile
./mvnw test -Dspring.profiles.active=test

# Run with debug logging
./mvnw test -Dlogging.level.org.hibernate.SQL=DEBUG
```

### 6. Common Test Commands
```bash
# Clean and test
./mvnw clean test

# Skip tests during build
./mvnw package -DskipTests

# Run tests with coverage
./mvnw test jacoco:report

# Run tests in parallel
./mvnw test -Dparallel-tests
```

### 7. Test Output Options
```bash
# Show test output in console
./mvnw test -Dsurefire.useFile=false

# Generate test report
./mvnw test surefire-report:report

# Show test execution time
./mvnw test -Dsurefire.reportFormat=plain
```

### 8. Test Debugging
```bash
# Run with debug port
./mvnw test -Dmaven.surefire.debug

# Run with specific test timeout
./mvnw test -Dsurefire.timeout=30

# Run with test retry
./mvnw test -Dsurefire.rerunFailingTestsCount=2
```

### 9. Test Environment Variables
```bash
# Set test database properties
./mvnw test -Dspring.datasource.url=jdbc:postgresql://localhost:5432/testdb

# Set test logging level
./mvnw test -Dlogging.level.root=INFO

# Set test timeout
./mvnw test -Dspring.datasource.hikari.connection-timeout=20000
```

### 10. Best Practices for Running Tests
1. **Before Running**:
   - Ensure Docker is running
   - Check database connection
   - Verify test profile is active

2. **During Test Run**:
   - Watch for container startup
   - Monitor test execution
   - Check test output

3. **After Test Run**:
   - Verify test results
   - Check container cleanup
   - Review test reports