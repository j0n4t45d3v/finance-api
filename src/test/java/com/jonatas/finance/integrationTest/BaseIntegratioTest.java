package com.jonatas.finance.integrationTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest
@Import(PostgresSQLContainerConfig.class)
public abstract class BaseIntegratioTest {

  @Autowired protected MockMvc mockMvc;
}
