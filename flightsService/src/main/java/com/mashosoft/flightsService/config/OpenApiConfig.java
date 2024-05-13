package com.mashosoft.flightsService.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
            .group("WebFlux example")
            .pathsToMatch("/**")
            .addOpenApiMethodFilter( method -> method.isAnnotationPresent( Operation.class) )
            .build();
    }

    @Bean
    public OpenAPI apiDescription() {
        return new OpenAPI()
            .info(new Info().title("WebFlux example")
                .description( "Flights api example" )
                .version( "1.0" ));
    }
}
