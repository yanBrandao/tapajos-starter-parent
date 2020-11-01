package tapajos.webservice.starter.swagger.configuration

import com.google.common.base.Predicates
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import tapajos.webservice.starter.commons.annotation.SwaggerIgnore

@Configuration
@EnableSwagger2
@PropertySource("classpath:application.properties")
class SwaggerAutoConfiguration {

    @Value("\${swagger.api.info.title}")
    lateinit var title: String

    @Value("\${swagger.api.info.description}")
    lateinit var description: String

    @Value("\${swagger.api.info.version}")
    lateinit var version: String

    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                .apis(Predicates.not(RequestHandlerSelectors.withClassAnnotation(SwaggerIgnore::class.java)))
                .apis(Predicates.not(RequestHandlerSelectors.withMethodAnnotation(SwaggerIgnore::class.java)))
                .build().apiInfo(apiEndPointsInfo())
    }

    private fun apiEndPointsInfo(): ApiInfo =
            ApiInfoBuilder()
                    .title(title)
                    .description(description)
                    .version(version)
                    .build()
}