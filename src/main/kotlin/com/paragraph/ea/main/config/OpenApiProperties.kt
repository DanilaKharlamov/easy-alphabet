package com.paragraph.ea.main.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "open-api.properties")
class OpenApiProperties : OpenAPI() {
    init {
        info(
            Info()
                .title("title")
                .version("version")
                .description("description")
                .contact(
                    Contact()
                        .email("email")
                        .name("name")
                        .url("url")
                )
        )
    }
}
