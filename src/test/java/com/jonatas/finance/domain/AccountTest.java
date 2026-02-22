package com.jonatas.finance.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    @DisplayName("should create valid account instance")
    void shouldCreateValidAccountInstance() {
        Account account = new Account(
            new Account.Description("test create account"),
            new User(),
            true
        );

        assertTrue(account.isMain());
        assertEquals("test create account", account.getDescriptionValue());
        assertNotNull(account.getUser());
    }

    @Test
    @DisplayName("should not allowed create account without user")
    void shouldNotAllowedCreateAccountWithoutUser() {
        assertThrows(NullPointerException.class, () -> new Account(
            new Account.Description("test create account"),
            null,
            true
        ));
    }

    @Test
    @DisplayName("should not allowed create account without description")
    void shouldNotAllowedCreateAccountWithoutDescription() {
        assertThrows(NullPointerException.class, () -> new Account(
            null,
            new User(),
            true
        ));
    }


    @Test
    @DisplayName("should not allowed create account with empty description")
    void shouldNotAllowedCreateAccountWithEmptyDescription() {
        assertThrows(IllegalArgumentException.class, () -> new Account(
            new Account.Description(""),
            new User(),
            true
        ));
    }

    @Test
    @DisplayName("should not allowed create account with blank description")
    void shouldNotAllowedCreateAccountWithBlankDescription() {
        assertThrows(IllegalArgumentException.class, () -> new Account(
            new Account.Description(""),
            new User(),
            true
        ));
    }


}
