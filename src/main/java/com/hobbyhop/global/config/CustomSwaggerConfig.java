package com.hobbyhop.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition(
        info = @Info(title = "Hobby Hop App",
                description = "Hobby Hop App API명세",
                version = "v1"))

@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)

@Configuration
public class CustomSwaggerConfig {

    @Bean
    public GroupedOpenApi restOpenApi() {

        return GroupedOpenApi.builder()
                .group("Hobby Hop API")
                .pathsToMatch("/api/**")
                .build();
    }
}