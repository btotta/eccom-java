package com.tota.eccom.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.servlet.ServletContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(ServletContext servletContext) {
        return new OpenAPI()
                .addServersItem(new Server().url(servletContext.getContextPath()))
                .info(new Info()
                        .title("Eccom - This is a project for a sample e-commerce")
                        .version("1.0.0")
                        .description("This project is a sample e-commerce project")
                        .termsOfService("http://swagger.io/terms/"));

    }
}
