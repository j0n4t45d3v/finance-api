package br.com.managementfinanceapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ManagementFinanceApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagementFinanceApiApplication.class, args);
    }

}