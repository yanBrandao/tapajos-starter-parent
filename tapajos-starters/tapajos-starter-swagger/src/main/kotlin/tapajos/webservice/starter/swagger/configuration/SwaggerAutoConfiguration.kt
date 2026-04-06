package tapajos.webservice.starter.swagger.configuration

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.PropertySource
import tapajos.webservice.starter.commons.annotation.SwaggerIgnore

@AutoConfiguration
@PropertySource("classpath:application.properties")
class SwaggerAutoConfiguration {

    @Value("\${swagger.api.info.title}")
    lateinit var title: String

    @Value("\${swagger.api.info.description}")
    lateinit var description: String

    @Value("\${swagger.api.info.version}")
    lateinit var version: String

    @Bean
    fun openApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title(title)
                    .description(description)
                    .version(version)
            )
    }

    @Bean
    fun publicApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("public")
            .packagesToExclude("org.springframework.boot")
            .addOpenApiCustomizer(swaggerIgnoreCustomizer())
            .build()
    }

    private fun swaggerIgnoreCustomizer(): OpenApiCustomizer {
        return OpenApiCustomizer { openApi ->
            openApi.paths?.entries?.removeIf { (_, pathItem) ->
                pathItem.readOperations().any { operation ->
                    operation.extensions?.containsKey("x-swagger-ignore") == true
                }
            }
        }
    }
}
