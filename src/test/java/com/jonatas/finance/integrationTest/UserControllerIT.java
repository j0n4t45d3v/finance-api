package com.jonatas.finance.integrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jonatas.finance.auth.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class UserControllerIT extends BaseIntegratioTest {

  @Autowired private UserRepository userRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  @Test
  void shouldReturnUserDetailsWhenAuthenticated() throws Exception {
    var email = "user@controller.test";
    TestUtils.createUser(userRepository, passwordEncoder, email);

    var token = TestUtils.loginAndGetAccessToken(mockMvc, email);

    mockMvc
        .perform(get("/v1/users/me").header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.email").value(email));
  }
}
