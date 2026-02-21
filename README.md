# Shop API Test Framework

API test automation framework for [shop-app](https://github.com/oleksiis27/shop-app) e-commerce service.

## Tech Stack

- **Java 21**
- **Gradle** (Kotlin DSL)
- **JUnit 5** — test runner
- **REST Assured** — API testing
- **Allure** — reporting
- **AssertJ** — readable assertions
- **Jackson** — JSON serialization
- **DataFaker** — test data generation
- **Owner** — configuration management

## Project Structure

```
src/
├── main/java/com/shop/
│   ├── config/          # Owner config interface
│   ├── models/          # POJOs (User, Product, Cart, Order, etc.)
│   ├── api/             # REST Assured API clients
│   └── helpers/         # Auth & test data helpers
└── test/java/com/shop/tests/
    ├── auth/            # Authentication tests (7 tests)
    ├── products/        # Product CRUD & filtering tests (13 tests)
    ├── cart/            # Cart operations tests (7 tests)
    └── orders/          # Order flow & admin tests (10 tests)
```

## Test Coverage

| Module | Tests | Coverage |
|--------|-------|----------|
| Auth | 7 | Registration, login, token validation, error cases |
| Products | 13 | CRUD, filtering, search, sorting, access control |
| Cart | 7 | Add/update/delete items, clear cart, auth checks |
| Orders | 10 | Create order, status flow, admin operations, isolation |
| **Total** | **37** | |

## Prerequisites

- Java 21
- Docker & Docker Compose
- [shop-app](https://github.com/oleksiis27/shop-app) running locally

## Setup & Run

### 1. Start the shop-app

```bash
cd /path/to/shop-app
docker-compose up --build -d
```

Wait until the backend is available at `http://localhost:8000`.

### 2. Run tests

```bash
./gradlew test
```

### 3. Generate Allure report

```bash
./gradlew allureServe
```

## Configuration

Test configuration is managed via `src/main/resources/app.properties`:

```properties
base.url=http://localhost:8000
admin.email=admin@shop.com
admin.password=admin123
user.email=user@shop.com
user.password=user123
```

Properties can be overridden via system properties:

```bash
./gradlew test -Dbase.url=http://staging:8000
```

## CI/CD

GitHub Actions workflow (`.github/workflows/tests.yml`):

1. Checks out both test framework and shop-app
2. Starts shop-app via Docker Compose
3. Waits for backend health check
4. Runs all tests with JDK 21
5. Generates and uploads Allure report as artifact

Triggers: push/PR to `main`, manual dispatch.
