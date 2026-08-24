package com.jonatas.finance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class FinanceApplication {

  public static void main(String[] args) {
    SpringApplication.run(FinanceApplication.class, args);
  }
}
