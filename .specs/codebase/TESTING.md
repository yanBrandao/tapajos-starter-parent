# Testing Infrastructure

## Test Frameworks

**Unit/Integration:** kotlin-test (Kotlin 1.4.10), spring-boot-starter-test
**Mocking:** mockito-kotlin 1.6.0
**Coverage:** JaCoCo 0.8.5 (configured but commented out in tapajos-starters-parent)

## Test Organization

**Location:** `src/test/kotlin` (configured in root POM)
**Naming:** No test files found in the repository
**Structure:** No tests exist currently

## Testing Patterns

### Unit Tests
**Approach:** None observed
**Location:** N/A

### Integration Tests
**Approach:** None observed
**Location:** N/A

## Test Execution

**Commands:**
```bash
mvn test                              # Run all tests
mvn test -pl tapajos-starters/tapajos-starter-commons  # Run tests for specific module
```

## Coverage Targets

**Current:** 0% (no tests exist)
**Goals:** Not documented
**Enforcement:** JaCoCo plugin configured but commented out

## Test Coverage Matrix

| Code Layer                | Required Test Type | Location Pattern         | Run Command |
| ------------------------- | ------------------ | ------------------------ | ----------- |
| JacksonExtension          | unit               | `**/extension/*Test.kt`  | `mvn test`  |
| SwaggerAutoConfiguration  | integration        | `**/configuration/*Test.kt` | `mvn test` |
| SwaggerIgnore annotation  | none               | N/A                      | N/A         |

## Gate Check Commands

| Gate Level | When to Use              | Command            |
| ---------- | ------------------------ | ------------------ |
| Quick      | After code changes       | `mvn test`         |
| Build      | After phase completion   | `mvn clean install`|
