package ua.shortener;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.oas.annotations.EnableOpenApi;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
//@OpenAPIDefinition(
//        info = @Info(
//                title = "ShortURL",
//                version = "1.0",
//                description = "API documentation for ShortUrl application",
//                contact = @Contact(
//                        name = "ua.patronum"
//                )
//        ),
//        security = {
//                @SecurityRequirement(name = "bearer-key")
//        }
//)
//@SecurityScheme(
//        name = "bearer-key",
//        type = SecuritySchemeType.HTTP,
//        scheme = "bearer",
//        bearerFormat = "JWT",
//        description = "Authorization header using the Bearer scheme. Example: 'Bearer {token}'"
//)

//@EnableOpenApi
//@EnableSwagger2
public class UrlShortenerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrlShortenerApplication.class, args);
    }
//    @Bean
//    public Docket productApi() {
//        return new Docket(DocumentationType.SWAGGER_2).select()
//                .apis(RequestHandlerSelectors.basePackage("com.tutorialspoint.swaggerdemo")).build();
//    }
}
