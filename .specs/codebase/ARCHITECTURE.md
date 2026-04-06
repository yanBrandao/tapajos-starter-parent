# Architecture

**Pattern:** Multi-module Maven parent POM providing reusable Spring Boot starters

## High-Level Structure

```
tapajos-starter-parent (root POM - inherits spring-boot-starter-parent)
└── tapajos-starters (aggregator POM)
    ├── tapajos-starter-commons   [jar] - Jackson extensions, shared annotations
    ├── tapajos-starter-swagger   [jar] - Swagger 2 auto-configuration
    ├── tapajos-starter-web       [jar] - Spring Boot Web + Actuator + Consul + Swagger
    ├── tapajos-starter-logging   [jar] - Logstash/Logback JSON logging
    └── tapajos-starters-parent   [pom] - (mostly commented out) consumer-facing parent POM
```

## Identified Patterns

### Dependency Chain Pattern

**Location:** Module POMs
**Purpose:** Layered dependency graph so consuming a higher-level starter pulls in lower ones
**Implementation:** `web` -> `swagger` -> `commons`
**Example:** `tapajos-starter-web/pom.xml` depends on `tapajos-starter-swagger`, which depends on `tapajos-starter-commons`

### Auto-Configuration via Spring Factories

**Location:** No `spring.factories` files found
**Purpose:** Spring Boot auto-configuration registration
**Implementation:** Currently missing -- SwaggerAutoConfiguration uses `@Configuration` + `@EnableSwagger2` but relies on component scanning, not auto-configuration registration
**Note:** This is a gap; proper starters should register via spring.factories (Boot 2.x) or META-INF/spring/AutoConfiguration.imports (Boot 3+)

### Extension Function Pattern

**Location:** `tapajos-starter-commons/.../extension/JacksonExtension.kt`
**Purpose:** Kotlin-idiomatic JSON serialization utilities
**Implementation:** Singleton ObjectMapper + top-level extension functions on String and generic T
**Example:** `String.jsonToObject()`, `T.objectToJson()`, `T.toJsonNode()`

### Custom Annotation Pattern

**Location:** `tapajos-starter-commons/.../annotation/SwaggerIgnore.kt`
**Purpose:** Cross-module annotation used by swagger module to exclude endpoints
**Implementation:** Runtime-retained annotation targeting CLASS and FUNCTION, used by SwaggerAutoConfiguration's Docket predicates

### Maven Resource Filtering

**Location:** `tapajos-starter-swagger/src/main/resources/application.properties`
**Purpose:** Auto-populate Swagger API metadata from consuming project's POM
**Implementation:** `@project.name@`, `@project.description@`, `@project.version@` tokens

## Data Flow

### API Documentation Flow

1. Consumer project inherits from starter-parent
2. Consumer adds `tapajos-starter-web` dependency
3. Web pulls in swagger, which pulls in commons
4. SwaggerAutoConfiguration creates Docket bean
5. Maven filtering injects project name/description/version into swagger properties
6. Springfox scans controllers, excluding @SwaggerIgnore-annotated ones

## Code Organization

**Approach:** Module-per-concern (each starter = one concern)
**Module boundaries:** Each starter is independently publishable as a JAR, with explicit inter-module dependencies declared in POMs
