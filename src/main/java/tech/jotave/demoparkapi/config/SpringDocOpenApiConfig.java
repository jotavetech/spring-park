package tech.jotave.demoparkapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocOpenApiConfig {
    @Bean
    public OpenAPI openApi() {
        return new OpenAPI().info(
                new Info()
                        .title("REST API - Spring Park")
                        .description("API para gestão de estacionamento de veículos")
                        .version("v1")
                        .license(new License().name("MIT").url("https://opensource.org/license/mit"))
                        .contact(new Contact().name("Jotavetech").email("jotavetech@gmail.com"))
        );
    }
}
