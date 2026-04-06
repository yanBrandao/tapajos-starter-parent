# Tech Stack

**Analyzed:** 2026-04-06

## Core

- Framework: Spring Boot 2.3.3.RELEASE
- Language: Kotlin 1.4.10
- Runtime: Java 11
- Package manager: Maven (multi-module POM)
- Spring Cloud: Hoxton.RELEASE

## Backend

- API Style: REST (Spring MVC via spring-boot-starter-web)
- Service Discovery/Config: Consul (spring-cloud-consul-config)
- API Documentation: Springfox Swagger 2.9.2
- Metrics: Micrometer 1.5.4 + Prometheus registry
- Tracing: Jaeger client 1.3.2

## Logging

- Logback with JSON output (logstash-logback-encoder 6.4)
- logback-contrib (jackson + json-classic) 0.1.5

## Data (managed versions only, no auto-config)

- PostgreSQL driver: 42.2.16
- Liquibase: 4.0.0

## AWS (managed versions only)

- AWS Java SDK 1.11.848 (core, DynamoDB, SNS, SQS, S3)

## JSON

- Jackson (version managed by Spring Boot BOM)
- jackson-module-kotlin 2.11.2 (hardcoded)
- jackson-datatype-jsr310 (Spring Boot BOM)

## Testing

- Unit: kotlin-test (Kotlin 1.4.10)
- Mocking: mockito-kotlin 1.6.0
- Integration: spring-boot-starter-test (JUnit, Hamcrest, Mockito)
- Coverage: JaCoCo 0.8.5 (configured but commented out in starters-parent)

## Development Tools

- Kotlin Maven Plugin with spring, jpa, no-arg compiler plugins
- maven-source-plugin 3.0.1
- nexus-staging-maven-plugin 1.6.7
- maven-scm-plugin 1.11.2
