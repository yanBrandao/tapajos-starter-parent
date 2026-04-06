# Codebase Concerns

**Analysis Date:** 2026-04-06

## Tech Debt

**Severely outdated dependency stack:**

- Issue: Spring Boot 2.3.3 (released Aug 2020) is EOL. Kotlin 1.4.10 is 4 major versions behind. Java 11 baseline is 3 LTS behind.
- Files: `pom.xml` (root)
- Impact: No security patches, missing 6 years of framework improvements, incompatible with modern libraries
- Fix approach: Upgrade to Spring Boot 4.0.x + Kotlin 2.2+ + Java 17+

**Missing auto-configuration registration:**

- Issue: `SwaggerAutoConfiguration` uses `@Configuration` + `@EnableSwagger2` but no `spring.factories` or `AutoConfiguration.imports` file exists
- Files: `tapajos-starter-swagger/src/main/kotlin/.../SwaggerAutoConfiguration.kt`
- Impact: Starter only works if consumer's component scan reaches the package; breaks the "just add dependency" starter contract
- Fix approach: Add proper auto-configuration registration during upgrade

**Hardcoded jackson-module-kotlin version:**

- Issue: `jackson-module-kotlin` version `2.11.2` is hardcoded in commons POM instead of using Spring Boot's managed Jackson BOM version
- Files: `tapajos-starters/tapajos-starter-commons/pom.xml:29`
- Impact: Could cause Jackson version conflicts in consumers
- Fix approach: Remove explicit version, let Spring Boot BOM manage it

**Deprecated KotlinModule() constructor:**

- Issue: `KotlinModule()` no-arg constructor is deprecated in newer Jackson versions; should use `KotlinModule.Builder`
- Files: `tapajos-starters/tapajos-starter-commons/src/main/kotlin/.../extension/JacksonExtension.kt:18`
- Impact: Will produce deprecation warnings or fail to compile on newer Jackson
- Fix approach: Use `kotlinModule {}` builder DSL or `jacksonObjectMapper()` from jackson-module-kotlin

## Dependencies at Risk

**Springfox Swagger 2.9.2:**

- Risk: Abandoned (last release June 2018), incompatible with Spring Boot 3+
- Impact: Entire `tapajos-starter-swagger` module is blocked from upgrading
- Migration plan: Replace with springdoc-openapi-starter-webmvc-ui; rewrite SwaggerAutoConfiguration to use `GroupedOpenApi` instead of `Docket`

**Google Guava (transitive via Springfox):**

- Risk: `Predicates.not()` from Guava used in SwaggerAutoConfiguration will be removed with Springfox
- Impact: `SwaggerAutoConfiguration.kt:4` imports `com.google.common.base.Predicates`
- Migration plan: springdoc-openapi uses different filtering mechanisms; Guava dependency will be eliminated

**Spring Cloud Hoxton:**

- Risk: EOL, incompatible with Spring Boot 3+
- Impact: `spring-cloud-consul-config` in web module
- Migration plan: Upgrade to Spring Cloud 2025.1.x

**AWS Java SDK v1 (1.11.x):**

- Risk: AWS SDK v1 is in maintenance mode; v2 is the supported path
- Impact: Managed versions for DynamoDB, SNS, SQS, S3 in root POM
- Migration plan: Deferred -- migrate to `software.amazon.awssdk` in future milestone

**mockito-kotlin 1.6.0 (com.nhaarman):**

- Risk: Original `com.nhaarman.mockitokotlin2` is unmaintained; succeeded by `org.mockito.kotlin`
- Impact: Test mocking dependency
- Migration plan: Switch to `org.mockito.kotlin:mockito-kotlin:5.x`

## Test Coverage Gaps

**Zero test coverage:**

- What's not tested: JacksonExtension functions, SwaggerAutoConfiguration, SwaggerIgnore behavior
- Risk: Upgrade could break serialization or API doc generation without detection
- Priority: High (especially before major upgrade)
- Difficulty to test: Low -- JacksonExtension is pure functions; SwaggerAutoConfiguration needs Spring context

## Fragile Areas

**SwaggerAutoConfiguration:**

- Files: `tapajos-starter-swagger/src/main/kotlin/.../SwaggerAutoConfiguration.kt`
- Why fragile: Tightly coupled to Springfox API (Docket, DocumentationType.SWAGGER_2, EnableSwagger2, Predicates); uses Maven resource filtering; relies on `@Value` injection from classpath application.properties
- Common failures: Missing application.properties in consumer, Springfox version conflicts
- Safe modification: Must be fully rewritten for springdoc-openapi; no incremental path
- Test coverage: None

## Missing Critical Features

**Auto-configuration registration:**

- Problem: No `spring.factories` or `AutoConfiguration.imports` file
- Current workaround: Consumer must component-scan the starter packages
- Blocks: Proper Spring Boot starter behavior
- Implementation complexity: Low -- single file addition per module
