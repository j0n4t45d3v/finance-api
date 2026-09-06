package com.jonatas.finance.auth;

import com.jonatas.finance.faker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    @Test
    @DisplayName("should create a valid user")
    void shouldCreateAValidUser() {
        var user = new User(new Email("john@doe.com"), new Password("secret-password"));
        assertEquals("secret-password", user.getPasswordValue());
        assertEquals("john@doe.com", user.getEmailValue());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("providerNullRequiredField")
    @DisplayName("should throw exception when not provide a required field")
    void shouldThrowExceptionWhenARequiredFieldIsNull(String scenery, String email, String password) {
        var userFaker = Faker.user()
                             .withEmail(email)
                             .withPassword(password);

        assertThatNullPointerException()
                                        .isThrownBy(userFaker::get)
                                        .withMessageContaining("is required");
    }

    static Stream<Arguments> providerNullRequiredField() {
        return Stream.of(
                         Arguments.of("E-mail is null", null, Faker.text(10)),
                         Arguments.of("Password is null", Faker.email(), null)
        );
    }
}
