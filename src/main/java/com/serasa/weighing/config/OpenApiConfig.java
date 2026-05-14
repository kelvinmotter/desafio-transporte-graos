package com.serasa.weighing.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Pesagem de Grãos")
                        .version("1.0.0")
                        .description("API para ingestão, estabilização e consulta de pesagens de balanças de grãos"));
    }
}
