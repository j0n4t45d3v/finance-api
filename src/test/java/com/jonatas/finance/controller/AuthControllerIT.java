package com.jonatas.finance.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import com.jayway.jsonpath.JsonPath;
import com.jonatas.finance.BaseIntegratioTest;
import com.jonatas.finance.domain.User;
import com.jonatas.finance.domain.dvo.user.Email;
import com.jonatas.finance.domain.dvo.user.Password;
import com.jonatas.finance.repository.UserRepository;

@Transactional
class AuthControllerIT extends BaseIntegratioTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String LOGIN_ENDPOINT = "/v1/auth/login";
    private static final String DEFAULT_PASSWORD = "secret";

    private User createUser(String email) {
        var validUser = new User(
            new Email(email),
            new Password(this.passwordEncoder.encode(DEFAULT_PASSWORD))
        );
        return this.userRepository.save(validUser);
    }

    @Nested
    class ApiV1Register {

        private static final String REGISTER_ENDPOINT = "/v1/auth/register";
        private static final String JSON_PATH_ERROR_TYPE = "$.error.type";

        private static final String ERROR_NOT_MATCH_PASSWORD  = "not_match_passwords";
        private static final String ERROR_FAIL_REGISTER  = "fail_register_user";

        @Test
        void shouldReturnCreatedWhenBodyIsValid() throws Exception {
            mockMvc.perform(this.makeRegisterUserRequest(
                "jonh@doe.test",
                "jonh123",
                "jonh123"
            ))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/v1/users/me"));

            assertEquals(1, userRepository.count());
        }

        @Test
        void shouldReturnBadRequestWhenPasswordsNotMatches() throws Exception {
            mockMvc.perform( this.makeRegisterUserRequest(
                "jonh@doe.test",
                "jonh123",
                "jonh12"
            ))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath(JSON_PATH_ERROR_TYPE)
                        .value(ERROR_NOT_MATCH_PASSWORD));
        }

        @Test
        void shouldReturnBadRequestWhenEmailAlreadyExists() throws Exception {
            createUser("conflict@email.test");
            mockMvc.perform(this.makeRegisterUserRequest(
                "conflict@email.test",
                "jonh123",
                "jonh123"
            ))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath(JSON_PATH_ERROR_TYPE)
                        .value(ERROR_FAIL_REGISTER));
        }

        private RequestBuilder makeRegisterUserRequest(
            String email,
            String password,
            String confirmPassword
        ) {
            var payload = this.registerPayload(
                email,
                password,
                confirmPassword
            );
            return post(REGISTER_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload);
        }

        private String registerPayload(String email, String password, String confirmPassword) {
            return """
            {
                "email": "%s",
                "password": "%s",
                "confirmPassword": "%s"
            }
            """.formatted(email, password, confirmPassword);
        }
    }

    @Nested
    class ApiV1Login {

        private static final String JSON_PATH_ACCESS_TOKEN = "$.data.access.token";
        private static final String JSON_PATH_REFRESH_TOKEN = "$.data.refresh.token";

        private static final String JSON_PATH_ERROR_TYPE = "$.error.type";
        private static final String ERROR_INVALID_CREDENTIALS = "fail_authentication";

        @Test
        void shouldReturnOKWhenBodyCredentialsIsValid() throws Exception{
            var user = createUser("john@doe.test");
            mockMvc.perform(makeLoginRequest(
                user.getEmailValue(),
                DEFAULT_PASSWORD
            ))
            .andExpect(status().isOk())
            .andExpect(jsonPath(JSON_PATH_ACCESS_TOKEN).isNotEmpty())
            .andExpect(jsonPath(JSON_PATH_REFRESH_TOKEN).isNotEmpty());
        }

        @Test
        void shouldReturnBadRequestWhenNotExistsUserWithEmailProvided() throws Exception{
            mockMvc.perform(makeLoginRequest(
                "john@doe.test",
                "john123"
            ))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath(JSON_PATH_ERROR_TYPE).value(ERROR_INVALID_CREDENTIALS));
        }

        @Test
        void shouldReturnHttpCodeBadRequestWhenPasswordNotMatchsWithFoundInUser() throws Exception{
            var user = createUser("john@doe.test");
            mockMvc.perform(makeLoginRequest(
                user.getEmailValue(),
                user.getPasswordValue() + "Teste"
            ))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath(JSON_PATH_ERROR_TYPE).value(ERROR_INVALID_CREDENTIALS));
        }

    }

    @Nested
    class ApiV1RefreshToken {
        private static final String REFRESH_ENDPOINT = "/v1/auth/refresh";

        private static final String JSON_PATH_ACCESS_TOKEN = "$.data.access.token";
        private static final String JSON_PATH_REFRESH_TOKEN = "$.data.refresh.token";

        private static final String JSON_PATH_ERROR_TYPE = "$.error.type";

        private static final String ERROR_INVALID_TOKEN = "invalid_token";
        private static final String ERROR_INVALID_SUBJECT = "invalid_subject_token";

        @Test
        void shouldReturnOKWhenRefreshTokenProviderIsValid() throws Exception {
            var user = createUser("john@doe.test");

            var loginResponse = mockMvc.perform(makeLoginRequest(
                    user.getEmailValue(),
                    DEFAULT_PASSWORD
            ))
            .andExpect(status().isOk())
            .andReturn();

            var json = loginResponse.getResponse().getContentAsString();
            var refreshToken = (String) JsonPath.read(json, JSON_PATH_REFRESH_TOKEN);

            mockMvc.perform(makeRefreshRequest(refreshToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_ACCESS_TOKEN).isNotEmpty())
                .andExpect(jsonPath(JSON_PATH_REFRESH_TOKEN).isNotEmpty());
        }

        @Test
        void shouldReturnBadRequestWhenRefreshTokenIsInvalid() throws Exception {
            mockMvc.perform(makeRefreshRequest("invalid-token"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(JSON_PATH_ERROR_TYPE).value(ERROR_INVALID_TOKEN));
        }

        @Test
        void shouldReturnBadRequestWhenSubjectIsInvalid() throws Exception {
            var user = createUser("john@doe.test");

            var loginResponse = mockMvc.perform(makeLoginRequest(
                    user.getEmailValue(),
                    DEFAULT_PASSWORD
            ))
            .andExpect(status().isOk())
            .andReturn();

            userRepository.deleteAll();

            var json = loginResponse.getResponse().getContentAsString();
            var invalidSubjectToken = (String) JsonPath.read(json, JSON_PATH_REFRESH_TOKEN);

            mockMvc.perform(makeRefreshRequest(invalidSubjectToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(JSON_PATH_ERROR_TYPE).value(ERROR_INVALID_SUBJECT));
        }

        private RequestBuilder makeRefreshRequest(String refreshToken) {
            var payload = """
            {
                "refreshToken": "%s"
            }
            """.formatted(refreshToken);

            return post(REFRESH_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload);
        }

    }

    private RequestBuilder makeLoginRequest(String email, String password) {
        var payload = """
        {
            "email": "%s",
            "password": "%s"
        }
        """.formatted(email, password);
        return post(LOGIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload);
    }
}
