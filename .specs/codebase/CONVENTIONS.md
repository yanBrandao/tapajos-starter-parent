# Code Conventions

## Naming Conventions

**Files:**
Kotlin files named after their primary class/object. PascalCase.
Examples: `SwaggerIgnore.kt`, `JacksonExtension.kt`, `SwaggerAutoConfiguration.kt`

**Packages:**
Lowercase dot-separated, following `tapajos.webservice.starter.<module>.<category>` pattern.
Examples: `tapajos.webservice.starter.commons.annotation`, `tapajos.webservice.starter.commons.extension`, `tapajos.webservice.starter.swagger.configuration`

**Functions/Methods:**
camelCase, Kotlin-idiomatic extension functions.
Examples: `jsonToObject()`, `objectToJson()`, `toJsonNode()`, `jsonToArrayListObject()`, `jsonToMap()`

**Classes:**
PascalCase with descriptive suffixes.
Examples: `SwaggerAutoConfiguration`, `JacksonExtension`, `SwaggerIgnore`

## Code Organization

**Import ordering:**
Standard Kotlin/Java ordering -- package declaration first, then imports grouped by domain (com, org, net, then project-internal). No explicit import ordering enforced.

**File structure:**
- Single primary class/object per file
- Extension functions defined at top level (outside classes) in `JacksonExtension.kt`
- Companion object `JacksonExtension` holds shared state (ObjectMapper), extensions use it

## Type Safety

**Approach:** Kotlin with `lazy` initialization for singletons. Uses `inline fun <reified T>` for type-safe JSON deserialization. `-Xjsr305=strict` compiler flag for null-safety interop with Java.

## Error Handling

**Pattern:** No explicit error handling observed in library code. Exceptions from Jackson propagate to consumers.

## Comments/Documentation

**Style:** Minimal. No KDoc. No inline comments in Kotlin sources. POM files have category-separating comments (`<!-- Trace -->`, `<!-- Database -->`, etc.).
