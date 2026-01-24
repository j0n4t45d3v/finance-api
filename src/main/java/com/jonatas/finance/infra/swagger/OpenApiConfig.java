package com.jonatas.finance.infra.swagger;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(this.info())
                .addSecurityItem(this.securityRequirement())
                .components(this.components());
    }

    private Info info() {
        return new Info()
                .title("Finance Api")
                .description("Pessoal manager finance api")
                .version("v1");
    }

    private SecurityRequirement securityRequirement() {
        return new SecurityRequirement()
                .addList(SECURITY_SCHEME_NAME);
    }

    private Components components() {
        return new Components()
                .addSecuritySchemes(SECURITY_SCHEME_NAME, this.securityScheme());
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER);
    }

}
