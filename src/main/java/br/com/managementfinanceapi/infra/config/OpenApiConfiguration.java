package br.com.managementfinanceapi.infra.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

  @Value("${server.port}")
  private int portApplication;

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(this.openApiInfo())
        .servers(this.openApiServers())
        .components(this.openApiSecurityComponent())
        .addSecurityItem(new SecurityRequirement().addList("jwt-security"));
  }

  private Components openApiSecurityComponent() {
    return new Components()
        .addSecuritySchemes("jwt-security", new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
        );
  }

  private Info openApiInfo() {
    return new Info()
        .title("Easy Finance Api")
        .version("0.1-BETA")
        .description("""
    Easy Finance Api é uma aplicação backend voltada para organização financeira pessoal,
    permitindo que o usuário tenha maior controle e visibilidade sobre sua vida financeira.
    Com ela, é possível entender de forma simples e objetiva de onde vem e para onde vai seu dinheiro.
    """);

  }

  private List<Server> openApiServers() {
    return List.of(
        new Server().url("http://localhost/api").description("Development Nginx Server"),
        new Server().url("http://localhost:"+this.portApplication+"/api").description("Development Server")
    );
  }

}
