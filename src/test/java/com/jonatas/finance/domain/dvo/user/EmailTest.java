package com.jonatas.finance.domain.dvo.user;

import com.jonatas.finance.domain.exception.EmailInvalidException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @ParameterizedTest(name  = "valid e-mail = {0}")
    @DisplayName("should create a valid e-mail")
    @ValueSource(strings = {
            "foo.bar@gmail.com",
            "foo.bar@gmail.com.br",
            "foo.bar@gmail.com.br.br"
    })
    void shouldCreateAValidEMail(String email) {
        assertEquals(email, new Email(email).value());
    }


    @Test
    @DisplayName("should throw EmailInvalidException when creating with empty e-mail")
    void shouldThrowEmailInvalidExceptionWhenCreatingEmptyEMail() {
        assertThrows(EmailInvalidException.class, () -> new Email(""));
    }

    @Test
    @DisplayName("should throw EmailInvalidException when creating with null e-mail")
    void shouldThrowEmailInvalidExceptionWhenCreatingWithNullEMail() {
        assertThrows(EmailInvalidException.class, () -> new Email(null));
    }

    @ParameterizedTest(name = "invalid e-mail = {0}")
    @ValueSource(strings = {
            "foo.bar@gmail.",
            "foo.bar@gmailcom",
            "foo.bargmail.com",
            "@gmail.com"
    })
    @DisplayName("should throw EmailInvalidException when creating e-mail with invalid pattern")
    void shouldThrowEmailInvalidExceptionWhenCreatingEMailWithInvalidPattern(String invalidEmail) {
        assertThrows(EmailInvalidException.class, () -> new Email(invalidEmail));
    }


}