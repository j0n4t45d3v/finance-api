package com.jonatas.finance.infra.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfigurer implements WebMvcConfigurer{

  private static final Logger log = LoggerFactory.getLogger(CorsConfigurer.class);

  private final CorsConfig corsConfig;

  public CorsConfigurer(CorsConfig corsConfig) {
    this.corsConfig = corsConfig;
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    log.info("Cors configuration:\n{}", this.corsConfig);
    registry
    .addMapping(this.corsConfig.mapping())
    .allowedOrigins(this.corsConfig.allowedOrigins().toArray(String[]::new))
    .allowedMethods(this.corsConfig.allowedMethods().toArray(String[]::new))
    .allowedHeaders(this.corsConfig.allowedHeaders().toArray(String[]::new))
    .allowCredentials(this.corsConfig.allowCredentials());
  }

}
