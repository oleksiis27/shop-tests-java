# Shop Test Framework

API and UI test automation framework for [shop-app](https://github.com/oleksiis27/shop-app) e-commerce service.

## Tech Stack

- **Java 21**
- **Gradle** (Kotlin DSL)
- **JUnit 5** — test runner
- **REST Assured** — API testing
- **Selenide** — UI testing (Selenium wrapper)
- **Allure** — reporting (with Selenide integration for screenshots)
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
│   ├── pages/           # Selenide Page Objects
│   │   ├── components/  # Reusable UI components (NavBar)
│   │   └── *.java       # LoginPage, RegisterPage, HomePage, etc.
│   └── helpers/         # Auth & test data helpers
└── test/java/com/shop/tests/
    ├── api/             # API tests (37 tests)
    │   ├── AuthTests        # Authentication (7 tests)
    │   ├── ProductTests     # Products CRUD & filtering (13 tests)
    │   ├── CartTests        # Cart operations (7 tests)
    │   └── OrderTests       # Order flow & admin (10 tests)
    └── ui/              # UI tests (26 tests)
        ├── BaseUiTest       # Selenide config, Allure listener
        ├── LoginUiTest      # Login flow (4 tests)
        ├── RegisterUiTest   # Registration flow (3 tests)
        ├── HomePageUiTest   # Home page (5 tests)
        ├── ProductUiTest    # Product page (3 tests)
        ├── CartUiTest       # Cart operations (5 tests)
        ├── OrderUiTest      # Orders (2 tests)
        └── AdminUiTest      # Admin panel (4 tests)
```

## Test Coverage

| Module | Tests | Type | Coverage |
|--------|-------|------|----------|
| Auth | 7 | API | Registration, login, token validation, error cases |
| Products | 13 | API | CRUD, filtering, search, sorting, access control |
| Cart | 7 | API | Add/update/delete items, clear cart, auth checks |
| Orders | 10 | API | Create order, status flow, admin operations, isolation |
| Login UI | 4 | UI | Valid login, invalid login, navigation, logout |
| Register UI | 3 | UI | Registration, duplicate email, navigation |
| Home Page UI | 5 | UI | Page load, search, category filter, sort, pagination |
| Product UI | 3 | UI | Product details, add to cart, unauthorized access |
| Cart UI | 5 | UI | View cart, quantity update, remove, clear, checkout |
| Orders UI | 2 | UI | Order list, order details |
| Admin UI | 4 | UI | View orders, update status, add/delete product |
| **Total** | **63** | | |

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

Wait until the backend is available at `http://localhost:8000` and the frontend at `http://localhost:3000`.

### 2. Run all tests

```bash
./gradlew test
```

### 3. Run only API tests

```bash
./gradlew test --tests "com.shop.tests.api.*"
```

### 4. Run only UI tests

```bash
./gradlew test --tests "com.shop.tests.ui.*"
```

### 5. Generate Allure report

```bash
./gradlew allureServe
```

## Configuration

Test configuration is managed via `src/main/resources/app.properties`:

```properties
base.url=http://localhost:8000
ui.url=http://localhost:3000
admin.email=admin@shop.com
admin.password=admin123
user.email=user@shop.com
user.password=user123
```

Properties can be overridden via system properties:

```bash
./gradlew test -Dbase.url=http://staging:8000 -Dui.url=http://staging:3000
```

## CI/CD

GitHub Actions workflow (`.github/workflows/tests.yml`):

1. Checks out both test framework and shop-app
2. Starts shop-app via Docker Compose
3. Waits for backend and frontend health checks
4. Sets up Chrome for UI tests (headless mode)
5. Runs all tests (API + UI) with JDK 21
6. Generates and uploads Allure report as artifact

Triggers: push/PR to `main`, manual dispatch.
