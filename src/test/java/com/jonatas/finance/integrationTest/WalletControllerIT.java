package com.jonatas.finance.integrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jonatas.finance.auth.UserRepository;
import com.jonatas.finance.wallet.Wallet;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class WalletControllerIT extends BaseIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldCreateAndListWallets() throws Exception {
        var email = "wallet@controller.test";
        TestUtils.createUser(userRepository, passwordEncoder, email);
        var token = TestUtils.loginAndGetAccessToken(mockMvc, email);

        var name = "Banco Teste";

        Long walletId = TestUtils.createWallet(mockMvc, token, name, true);

        mockMvc.perform(get("/v1/wallets").header("Authorization", "Bearer " + token))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.data[0].id").isNumber())
               .andExpect(jsonPath("$.data[0].id").value(walletId))
               .andExpect(jsonPath("$.data[0].name").value(name))
               .andExpect(jsonPath("$.data[0].mainWallet").value(true));
    }

    @Test
    void shouldEditWallet() throws Exception {
        var email = "wallet@controller.test";
        TestUtils.createUser(userRepository, passwordEncoder, email);
        var token = TestUtils.loginAndGetAccessToken(mockMvc, email);
        var name = "Banco Teste";
        Long walletId = TestUtils.createWallet(mockMvc, token, name, true);

        Wallet wallet = entityManager.find(Wallet.class, walletId);
        assertThat(wallet).isNotNull();
        assertThat(wallet.getId()).isEqualTo(walletId);
        assertThat(wallet.getDescriptionValue()).isEqualTo(name);
        assertThat(wallet.isMain()).isTrue();

        String editedName = "WalletEdited";
        mockMvc.perform(put("/v1/wallets/{id}", walletId)
                                                         .header("Authorization", "Bearer " + token)
                                                         .contentType(MediaType.APPLICATION_JSON)
                                                         .content("""
                                                                  {
                                                                      "name": "%s",
                                                                      "mainWallet": false
                                                                  }
                                                                  """.formatted(editedName)))
               .andExpect(status().isNoContent());

        Wallet walletEdited = entityManager.find(Wallet.class, walletId);
        assertThat(walletEdited).isNotNull();
        assertThat(walletEdited.getId()).isEqualTo(walletId);
        assertThat(walletEdited.getDescriptionValue()).isEqualTo(editedName);
        assertThat(walletEdited.isMain()).isFalse();
    }

}
