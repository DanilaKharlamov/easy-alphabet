package com.paragraph.ea.main.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.media.Schema
import org.springdoc.core.customizers.OpenApiCustomiser
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Consumer

@Configuration
@EnableConfigurationProperties(OpenApiProperties::class)
class OpenApiConfig(
) {
    @Bean
    fun openAPI(openApiProperties: OpenApiProperties): OpenAPI {
        return openApiProperties
    }

    @Bean
    fun openApiCustomiser(): OpenApiCustomiser? {
        return OpenApiCustomiser { openApi: OpenAPI ->
            val components = openApi.components
            if (components != null) {
                val schemas = components.schemas
                schemas?.values?.forEach(Consumer { s: Schema<*> ->
                    s.setAdditionalProperties(false)
                })
            }
        }
    }
}
