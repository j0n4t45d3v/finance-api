package com.jonatas.finance.integrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import com.jonatas.finance.auth.Email;
import com.jonatas.finance.auth.Password;
import com.jonatas.finance.auth.User;
import com.jonatas.finance.auth.UserRepository;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

public final class TestUtils {

  public static final String DEFAULT_PASSWORD = "secret";

  private TestUtils() {}

  public static User createUser(
      UserRepository userRepository, PasswordEncoder passwordEncoder, String email) {
    var user = new User(new Email(email), new Password(passwordEncoder.encode(DEFAULT_PASSWORD)));
    return userRepository.save(user);
  }

  public static String loginAndGetAccessToken(MockMvc mockMvc, String email) throws Exception {
    var payload =
        "{" + "\"email\": \"" + email + "\"," + "\"password\": \"" + DEFAULT_PASSWORD + "\"" + "}";

    MvcResult result =
        mockMvc
            .perform(
                post("/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(payload))
            .andExpect(status().isOk())
            .andReturn();

    var json = result.getResponse().getContentAsString();
    return (String) JsonPath.read(json, "$.data.access.token");
  }

  public static Long createCategory(MockMvc mockMvc, String token, String name, String type)
      throws Exception {
    var payload = "{" + "\"name\": \"" + name + "\"," + "\"type\": \"" + type + "\"" + "}";

    MvcResult result =
        mockMvc
            .perform(
                post("/v1/categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload)
                    .header("Authorization", "Bearer " + token))
            .andExpect(status().isCreated())
            .andReturn();

    String location = result.getResponse().getHeader("Location");
    String[] parts = location.split("/");
    return Long.valueOf(parts[parts.length - 1]);
  }

  public static Long createWallet(MockMvc mockMvc, String token, String name, boolean mainWallet)
      throws Exception {
    var payload = "{" + "\"name\": \"" + name + "\"," + "\"mainWallet\": " + mainWallet + "}";

    MvcResult result =
        mockMvc
            .perform(
                post("/v1/wallets")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload)
                    .header("Authorization", "Bearer " + token))
            .andExpect(status().isCreated())
            .andReturn();

    String location = result.getResponse().getHeader("Location");
    String[] parts = location.split("/");
    return Long.valueOf(parts[parts.length - 1]);
  }
}
