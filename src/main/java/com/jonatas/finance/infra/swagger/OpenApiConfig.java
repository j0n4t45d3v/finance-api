package com.jonatas.finance.infra.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    tags = {
      @Tag(
          name = "Autorização e Autenticação",
          description = "rotas de login, cadastro e renovação de token"),
      @Tag(name = "Usuário", description = "Dados do usuario"),
      @Tag(
          name = "Conta do Banco",
          description = "cadastro das contas de banco onde as transações foram feitas"),
      @Tag(name = "Categoria", description = "Categoria das transações financeiras"),
      @Tag(name = "Transações", description = "Transações financeiras"),
      @Tag(
          name = "Dashboard",
          description =
              """
                rotas para alimentar dashboard, com visão das ultimas transações feitas,
                rank de categorias, rank de transações, resumo do período com total de entrada
                e saida mais saldo calculado
                """)
    })
@Configuration
public class OpenApiConfig {

  private static final String SECURITY_SCHEME_NAME = "bearerAuth";

  @Bean
  public OpenAPI openApi(BuildProperties properties) {
    return new OpenAPI()
        .info(this.info(properties))
        .addSecurityItem(this.securityRequirement())
        .components(this.components());
  }

  private Info info(BuildProperties properties) {
    return new Info()
        .title(properties.getName())
        .description(
            """
                    REST Api para gerenciamento financeiro pessoal com autenticação JWT, cadastro e consulta de transações, categorias, contas bancárias e dashboards

                    Usuário de demonstração para testes:
                    - Email: john@doe.example
                    - Senha: 1234

                    Fluxo para usabilidade:
                    1. Criar conta de usuario
                    2. Fazer login
                    3. usar o token de acesso gerado pela rota de login para acessar as rotas privadas
                    4. Caso o token de acesso fique expirado fazer a renovação na rota de refresh token
                """)
        .version(properties.getVersion());
  }

  private SecurityRequirement securityRequirement() {
    return new SecurityRequirement().addList(SECURITY_SCHEME_NAME);
  }

  private Components components() {
    return new Components().addSecuritySchemes(SECURITY_SCHEME_NAME, this.securityScheme());
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
