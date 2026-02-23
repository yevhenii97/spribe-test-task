# spribe-test-task

## Tech Stack
- Java 17
- Spring Boot 2.7.18
- TestNG 7.9.0
- REST Assured 5.5.1
- Allure 2.27.0
- Gradle 8.x

## Project Structure
spribe-test-task/
├── rest-api/          # HTTP clients, models, services
└── spribe-tests/      # Test classes, data providers

## How to Run

# Run all tests
./gradlew :spribe-tests:test

# Run with custom thread count
./gradlew :spribe-tests:test -Dthread.count=5

# Open Allure report
./gradlew :allureServe

## Configuration
Framework config: spribe-tests/src/test/resources/framework.properties
App config: spribe-tests/src/test/resources/application.yml