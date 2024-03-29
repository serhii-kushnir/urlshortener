package ua.shortener.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

import org.springdoc.core.models.GroupedOpenApi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Shortify",
                version = "1.0",
                description = "API documentation for Shortify application",
                contact = @Contact(
                        name = "MySwagger"
                )
        ),
        security = {
                @SecurityRequirement(name = "MyBearer-key")
        }
)
@SecurityScheme(
        name = "MyBearer-key",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Authorization header using the Bearer scheme. Example: 'Bearer {token}'"
)
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    public GroupedOpenApi customApi() {
        return GroupedOpenApi.builder()
                .group("shortify")
                .packagesToScan("ua.shortener")
                .pathsToMatch("/api/v1/**", "/sh/**")
                .build();
    }
}
