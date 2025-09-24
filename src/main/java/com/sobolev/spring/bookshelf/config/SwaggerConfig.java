package com.sobolev.spring.bookshelf.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("BookShelf API")
                                .description("Book Shelf API")
                                .contact(
                                        new Contact()
                                                .email("sobolevkirill0@gmail.com")
                                                .name("Sobolev Kirill")
                                )

                );
    }
}
