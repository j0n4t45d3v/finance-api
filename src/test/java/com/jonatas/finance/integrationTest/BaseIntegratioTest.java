package com.jonatas.finance.integrationTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.EnumNamingStrategies;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.jayway.jsonpath.JsonPath;
import com.jonatas.finance.auth.AuthController;
import com.jonatas.finance.common.ErrorCode;
import com.jonatas.finance.faker.Faker;

@ActiveProfiles("test")
@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest
@Import(PostgresSQLContainerConfig.class)
public abstract class BaseIntegratioTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected String registerAndLogin() throws Exception {
        var email = Faker.email();
        var password = Faker.text(10);

        register(email, password);
        return login(email, password);
    }

    private void register(String email, String password) throws Exception {
        var registerRequest = new AuthController.RegisterUserRequest(email, password, password);
        apiClient().post("/v1/auth/register", registerRequest)
                   .isCreated();
    }

    private String login(String email, String password) throws Exception {
        var loginRequest = new AuthController.LoginRequest(email, password);
        var loginResponse = apiClient().post("/v1/auth/login", loginRequest)
                                       .isOk()
                                       .jsonPathStatus(200)
                                       .result()
                                       .getResponse()
                                       .getContentAsString();

        return JsonPath.read(loginResponse, "$.data.access.token");
    }

    protected ApiTestClient apiClient() {
        return ApiTestClient.of(mockMvc, objectMapper);
    }

    protected static class ApiTestClient {

        private final MockMvc mockMvc;
        private final ObjectMapper objectMapper;

        private ApiTestClient(MockMvc mockMvc, ObjectMapper objectMapper) {
            this.mockMvc = mockMvc;
            this.objectMapper = objectMapper;
        }

        private static ApiTestClient of(MockMvc mockMvc, ObjectMapper objectMapper) {
            return new ApiTestClient(mockMvc, objectMapper);
        }

        public Assertions post(String uri, Object payload) throws Exception {
            String content = payload instanceof String raw ? raw
                                                           : objectMapper.writeValueAsString(payload);

            var request = MockMvcRequestBuilders.post(uri)
                                                .contentType("application/json")
                                                .content(content);
            return Assertions.that(mockMvc.perform(request));
        }

        public Assertions post(String uri, Object payload, String token) throws Exception {
            String content = payload instanceof String raw ? raw
                                                           : objectMapper.writeValueAsString(payload);
            var request = MockMvcRequestBuilders.post(uri)
                                                .contentType("application/json")
                                                .content(content)
                                                .header("Authorization", "Bearer " + token);
            return Assertions.that(mockMvc.perform(request));
        }

        public Assertions get(String uri) throws Exception {
            var request = MockMvcRequestBuilders.get(uri)
                                                .contentType("application/json");
            return Assertions.that(mockMvc.perform(request));
        }

        public Assertions get(String uri, String token) throws Exception {
            var request = MockMvcRequestBuilders.get(uri)
                                                .contentType("application/json")
                                                .header("Authorization", "Bearer " + token);
            return Assertions.that(mockMvc.perform(request));
        }

    }

    protected static class Assertions {

        private static final String JSON_PATH_STATUS = "$.status";
        private static final String JSON_PATH_ERROR_CODE = "$.error.code";
        private static final String JSON_PATH_ERROR_MESSAGE = "$.error.message";

        private final ResultActions resultActions;

        private Assertions(ResultActions resultActions) {
            this.resultActions = resultActions;
        }

        public static Assertions that(ResultActions actions) {
            return new Assertions(actions);
        }

        public Assertions isCreated() throws Exception {
            resultActions.andExpect(status().isCreated());
            return this;
        }

        public Assertions isOk() throws Exception {
            resultActions.andExpect(status().isOk());
            return this;
        }

        public Assertions isNotFound() throws Exception {
            resultActions.andExpect(status().isNotFound());

            return this;
        }

        public Assertions isConflict() throws Exception {
            resultActions.andExpect(status().isConflict());
            return this;
        }

        public Assertions isUnprocessableContent() throws Exception {
            resultActions.andExpect(status().isUnprocessableContent());
            return this;
        }

        public void hasErrorCode(ErrorCode errorCode) throws Exception {
            this.jsonPathEquals(JSON_PATH_ERROR_CODE, errorCode.code())
                .jsonPathEquals(JSON_PATH_ERROR_MESSAGE, errorCode.message());
        }

        public Assertions jsonPathStatus(int status) throws Exception {
            return jsonPathEquals(JSON_PATH_STATUS, status);
        }

        public Assertions jsonPathEquals(String path, Object expectedValue) throws Exception {
            resultActions.andExpect(jsonPath(path).value(expectedValue));
            return this;
        }

        public Assertions jsonPathExists(String path) throws Exception {
            resultActions.andExpect(jsonPath(path).exists());
            return this;
        }

        public Assertions jsonPathDoesNotExists(String path) throws Exception {
            resultActions.andExpect(jsonPath(path).doesNotExist());
            return this;
        }

        public MvcResult result() {
            return resultActions.andReturn();
        }

    }

    @TestConfiguration
    static class IntegrationTestConfiguration {

        @Bean
        public ObjectMapper objectMapper() {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setEnumNamingStrategy(EnumNamingStrategies.SNAKE_CASE);
            JavaTimeModule module = new JavaTimeModule();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
            module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
            objectMapper.registerModule(module);
            return objectMapper;
        }

    }
}
