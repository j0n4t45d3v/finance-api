package com.jonatas.finance.auth;

import com.jonatas.finance.common.exception.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.jonatas.finance.faker.Faker;

import static org.assertj.core.api.Assertions.*;

class HashPasswordTest {

    @Test
    void shouldCreateHashPassword() {
        var hashPassword = HashPassword.of(Faker.text(20));
        assertThat(hashPassword).isNotNull()
                                .extracting(HashPassword::value)
                                .isNotNull();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  " })
    void shouldNotAllowCreateHashPasswordWhenValueIsBlank(String value) {
        assertThatExceptionOfType(DomainException.class).isThrownBy(() -> new HashPassword(value))
                                                        .withMessageContaining("Password is require");
    }

    @Test
    void shouldReturnNullWhenValueIsNull() {
        assertThat(HashPassword.of(null)).isNull();
    }
}