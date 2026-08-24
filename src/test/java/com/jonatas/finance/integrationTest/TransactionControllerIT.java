package com.jonatas.finance.integrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jonatas.finance.auth.UserRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class TransactionControllerIT extends BaseIntegratioTest {

  @Autowired private UserRepository userRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  @Test
  void shouldCreateTransactionWhenDataIsValid() throws Exception {
    var email = "tx@controller.test";
    TestUtils.createUser(userRepository, passwordEncoder, email);
    var token = TestUtils.loginAndGetAccessToken(mockMvc, email);

    Long categoryId = TestUtils.createCategory(mockMvc, token, "Alim TX Test", "EXPENSE");
    Long walletId = TestUtils.createWallet(mockMvc, token, "Wallet TX Test", false);

    var datetime =
        LocalDateTime.now()
            .minusDays(1)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

    var payload =
        "{"
            + "\"description\": \"Compra teste\","
            + "\"amount\": 10.00,"
            + "\"datetime\": \""
            + datetime
            + "\","
            + "\"categoryId\": "
            + categoryId
            + ","
            + "\"walletId\": "
            + walletId
            + "}";

    mockMvc
        .perform(
            post("/v1/transactions")
                .contentType("application/json")
                .content(payload)
                .header("Authorization", "Bearer " + token))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/")));
  }

  @Test
  void shouldGetPageWhenExistsTransactions() throws Exception {
    var email = "txpage@controller.test";
    TestUtils.createUser(userRepository, passwordEncoder, email);
    var token = TestUtils.loginAndGetAccessToken(mockMvc, email);

    Long categoryId = TestUtils.createCategory(mockMvc, token, "Alim Page Test", "EXPENSE");
    Long walletId = TestUtils.createWallet(mockMvc, token, "Wallet Page Test", false);

    var datetime =
        LocalDateTime.now()
            .minusDays(1)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

    var payload =
        "{"
            + "\"description\": \"Compra pagina\","
            + "\"amount\": 5.50,"
            + "\"datetime\": \""
            + datetime
            + "\","
            + "\"categoryId\": "
            + categoryId
            + ","
            + "\"walletId\": "
            + walletId
            + "}";

    mockMvc
        .perform(
            post("/v1/transactions")
                .contentType("application/json")
                .content(payload)
                .header("Authorization", "Bearer " + token))
        .andExpect(status().isCreated());

    mockMvc
        .perform(get("/v1/transactions").header("Authorization", "Bearer " + token))
        .andExpect(status().isOk());
  }
}
