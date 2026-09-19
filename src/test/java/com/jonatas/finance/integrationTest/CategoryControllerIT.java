package com.jonatas.finance.integrationTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import com.jonatas.finance.faker.Faker;
import com.jonatas.finance.wallet.CategoryErrorCode;

@Transactional
class CategoryControllerIT extends BaseIntegrationTest {

    private static final String BASE_URI = "/v1/categories";

    private String accessToken;

    @BeforeEach
    void setUp() throws Exception {
        this.accessToken = registerAndLogin();
    }

    @Test
    void shouldCreateAndListCategories() throws Exception {
        var name = Faker.text(20);
        var type = Faker.options("EXPENSE", "INCOME");

        Long categoryId = TestUtils.createCategory(mockMvc, this.accessToken, name, type);

        apiClient().get(BASE_URI, this.accessToken)
                   .isOk()
                   .jsonPathStatus(200)
                   .jsonPathEquals("$.data[0].id", categoryId)
                   .jsonPathEquals("$.data[0].name", name)
                   .jsonPathEquals("$.data[0].type", type);
    }

    @Test
    void shouldFailAndReturn409WhenHasDuplicateCategory() throws Exception {
        var name = Faker.text(20);
        var type = Faker.options("EXPENSE", "INCOME");

        TestUtils.createCategory(mockMvc, this.accessToken, name, type);

        String payload = """
                         {
                            "name": "%s",
                            "type": "%s"
                         }
                         """.formatted(name, type);

        apiClient().post(BASE_URI, payload, this.accessToken)
                   .isConflict()
                   .jsonPathStatus(409)
                   .hasErrorCode(CategoryErrorCode.ALREADY_EXISTS_CATEGORY_WITH_NAME);

    }

}
