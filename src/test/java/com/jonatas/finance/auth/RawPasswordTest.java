package com.jonatas.finance.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.jonatas.finance.common.exception.DomainException;
import com.jonatas.finance.faker.Faker;

import java.util.stream.IntStream;

class RawPasswordTest {

    @Test
    void shouldCreateRawPassword() {
        var rawPassword = RawPassword.of(Faker.text(20));
        assertThat(rawPassword).isNotNull()
                               .extracting(RawPassword::value)
                               .isNotNull();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  " })
    void shouldNotAllowCreateRawPasswordWhenValueIsBlank(String value) {
        assertThatExceptionOfType(DomainException.class).isThrownBy(() -> new RawPassword(value))
                                                        .withMessageContaining("Password is required");
    }

    @ParameterizedTest
    @MethodSource("provideLessThanMinRequiredLength")
    void shouldThrowDomainExceptionWhenValueIsLessThanMinRequiredLength(int length) {
        assertThatExceptionOfType(DomainException.class).isThrownBy(() -> new RawPassword(Faker.text(length)))
                                                        .withMessageContaining("Password must be at least 10 characters long");
    }

    static IntStream provideLessThanMinRequiredLength() {
        return IntStream.range(1, 9);
    }

    @Test
    void shouldReturnNullWhenValueIsNull() {
        assertThat(RawPassword.of(null)).isNull();
    }
}