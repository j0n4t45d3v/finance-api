package com.jonatas.finance.infra.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.jonatas.finance.auth.HashPassword;
import com.jonatas.finance.auth.RawPassword;
import com.jonatas.finance.faker.Faker;

import java.util.Objects;

@ExtendWith(MockitoExtension.class)
class SpringPasswordHasherTest {

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private SpringPasswordHasher passwordHasher;

    @Test
    void shouldHashRawPassword() {
        var raw = RawPassword.of(Faker.text(10));

        assert raw != null;
        String expectedHash = Faker.text(100);
        when(this.encoder.encode(eq(raw.value()))).thenReturn(expectedHash);

        var hashed = this.passwordHasher.hash(raw);
        assertThat(hashed).isNotNull()
                          .extracting(HashPassword::value)
                          .isNotNull()
                          .isSameAs(expectedHash);
    }

    @Test
    void shouldReturnTrueWhenRawPasswordIsEqualsToHashPassword() {
        var raw = RawPassword.of(Faker.text(10));
        String expectedHash = Faker.text(100);

        assert raw != null;
        when(this.encoder.matches(eq(raw.value()), eq(expectedHash))).thenReturn(true);

        var isMatch = this.passwordHasher.matches(raw, Objects.requireNonNull(HashPassword.of(expectedHash)));
        assertThat(isMatch).isTrue();
    }

    @Test
    void shouldReturnFalseWhenRawPasswordIsNotEqualsToHashPassword() {
        var raw = RawPassword.of(Faker.text(10));
        String expectedHash = Faker.text(100);

        assert raw != null;
        when(this.encoder.matches(eq(raw.value()), eq(expectedHash))).thenReturn(false);

        var isMatch = this.passwordHasher.matches(raw, Objects.requireNonNull(HashPassword.of(expectedHash)));
        assertThat(isMatch).isFalse();
    }

}