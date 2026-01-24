package com.jonatas.finance.domain;

import com.jonatas.finance.domain.dvo.user.Email;
import com.jonatas.finance.domain.dvo.user.Password;
import com.jonatas.finance.domain.exception.FieldRequiredException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {

    @Test
    @DisplayName("should create a valid user")
    void shouldCreateAValidUser() {
        var user = new User(new Email("john@doe.com"), new Password("secret-password"));
        assertEquals("secret-password", user.getPasswordValue());
        assertEquals("john@doe.com", user.getEmailValue());
    }


    @Test
    @DisplayName("should throw FieldRequiredException when creating user without e-mail")
    void shouldThrowFieldRequiredExceptionInCreateUserWhenEmailIsNull() {
        assertThrows(FieldRequiredException.class, () -> new User(null, new Password("John Doe")));
    }

    @Test
    @DisplayName("should throw FieldRequiredException when creating user without password")
    void shouldThrowFieldRequiredExceptionWhenCreatingUserWithoutPassword() {
        assertThrows(FieldRequiredException.class, () -> new User(new Email("john@doe.com"), null));
    }

}