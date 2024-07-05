package com.bjet.aki.lms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${lms.openapi.dev-url}")
    private String devUrl;

    @Bean
    public OpenAPI openAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Contact contact = new Contact();
        contact.setEmail("smiraj2507@gmail.com");
        contact.setName("Shahriar Miraj");
        contact.setUrl("https://github.com/SyedMiraj");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("LMS (Auth Service)")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage lms auth service.")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(devServer));
    }
}

