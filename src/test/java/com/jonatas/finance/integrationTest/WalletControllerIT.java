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
class WalletControllerIT extends BaseIntegratioTest {

  @Autowired private UserRepository userRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  @Test
  void shouldCreateAndListWallets() throws Exception {
    var email = "wallet@controller.test";
    TestUtils.createUser(userRepository, passwordEncoder, email);
    var token = TestUtils.loginAndGetAccessToken(mockMvc, email);

    var name = "Banco Teste";

    Long walletId = TestUtils.createWallet(mockMvc, token, name, true);

    mockMvc
        .perform(get("/v1/wallets").header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data[0].id").isNumber())
        .andExpect(jsonPath("$.data[0].name").value(name))
        .andExpect(jsonPath("$.data[0].mainWallet").value(true));
  }
}
