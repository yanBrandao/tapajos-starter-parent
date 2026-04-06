# Project Structure

**Root:** `/mnt/d/Developer/tapajos-starter-parent`

## Directory Tree

```
tapajos-starter-parent/
├── pom.xml                          # Root parent POM (inherits spring-boot-starter-parent)
├── Dockerfile
├── CLAUDE.md
├── LICENSE
├── README.md
└── tapajos-starters/
    ├── pom.xml                      # Aggregator POM
    ├── tapajos-starter-commons/
    │   ├── pom.xml
    │   └── src/main/kotlin/tapajos/webservice/starter/commons/
    │       ├── annotation/
    │       │   └── SwaggerIgnore.kt
    │       └── extension/
    │           └── JacksonExtension.kt
    ├── tapajos-starter-swagger/
    │   ├── pom.xml
    │   └── src/main/
    │       ├── kotlin/tapajos/webservice/starter/swagger/configuration/
    │       │   └── SwaggerAutoConfiguration.kt
    │       └── resources/
    │           └── application.properties
    ├── tapajos-starter-web/
    │   └── pom.xml                  # No source code, dependency-only module
    ├── tapajos-starter-logging/
    │   └── pom.xml                  # No source code, dependency-only module
    └── tapajos-starters-parent/
        └── pom.xml                  # Mostly commented out consumer parent POM
```

## Module Organization

### tapajos-starter-commons
**Purpose:** Shared utilities and annotations used across other starters
**Location:** `tapajos-starters/tapajos-starter-commons/`
**Key files:** `SwaggerIgnore.kt`, `JacksonExtension.kt`

### tapajos-starter-swagger
**Purpose:** Auto-configured Swagger 2 API documentation
**Location:** `tapajos-starters/tapajos-starter-swagger/`
**Key files:** `SwaggerAutoConfiguration.kt`, `application.properties`

### tapajos-starter-web
**Purpose:** Dependency aggregator for web services (Web + Actuator + Consul + Swagger)
**Location:** `tapajos-starters/tapajos-starter-web/`
**Key files:** `pom.xml` only (no source code)

### tapajos-starter-logging
**Purpose:** Logstash/Logback JSON logging integration
**Location:** `tapajos-starters/tapajos-starter-logging/`
**Key files:** `pom.xml` only (no source code)

## Where Things Live

**Annotations:** `tapajos-starter-commons/src/main/kotlin/.../annotation/`
**Extensions:** `tapajos-starter-commons/src/main/kotlin/.../extension/`
**Configuration:** `tapajos-starter-swagger/src/main/kotlin/.../configuration/`
**Resources:** `tapajos-starter-swagger/src/main/resources/`
