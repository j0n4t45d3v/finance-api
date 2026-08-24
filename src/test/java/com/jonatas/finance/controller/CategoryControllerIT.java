package com.jonatas.finance.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jonatas.finance.BaseIntegratioTest;
import com.jonatas.finance.auth.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class CategoryControllerIT extends BaseIntegratioTest {

  @Autowired private UserRepository userRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  @Test
  void shouldCreateAndListCategories() throws Exception {
    var email = "cat@controller.test";
    TestUtils.createUser(userRepository, passwordEncoder, email);
    var token = TestUtils.loginAndGetAccessToken(mockMvc, email);

    var name = "Alimentacao Test";
    var type = "EXPENSE";

    Long categoryId = TestUtils.createCategory(mockMvc, token, name, type);

    mockMvc
        .perform(get("/v1/categories").header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data[0].id").isNumber())
        .andExpect(jsonPath("$.data[0].id").value(categoryId))
        .andExpect(jsonPath("$.data[0].name").value(name))
        .andExpect(jsonPath("$.data[0].type").value(type));
  }
}
