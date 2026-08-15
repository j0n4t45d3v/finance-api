package com.jonatas.finance.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {

    @Test
    @DisplayName("should create valid wallet instance")
    void shouldCreateValidWalletInstance() {
        Wallet wallet = new Wallet(
            new Wallet.Description("test create wallet"),
            new User(),
            true
        );

        assertTrue(wallet.isMain());
        assertEquals("test create wallet", wallet.getDescriptionValue());
        assertNotNull(wallet.getUser());
    }

    @Test
    @DisplayName("should not allowed create wallet without user")
    void shouldNotAllowedCreateWalletWithoutUser() {
        assertThrows(NullPointerException.class, () -> new Wallet(
            new Wallet.Description("test create wallet"),
            null,
            true
        ));
    }

    @Test
    @DisplayName("should not allowed create wallet without description")
    void shouldNotAllowedCreateWalletWithoutDescription() {
        assertThrows(NullPointerException.class, () -> new Wallet(
            null,
            new User(),
            true
        ));
    }


    @Test
    @DisplayName("should not allowed create wallet with empty description")
    void shouldNotAllowedCreateWalletWithEmptyDescription() {
        assertThrows(IllegalArgumentException.class, () -> new Wallet(
            new Wallet.Description(""),
            new User(),
            true
        ));
    }

    @Test
    @DisplayName("should not allowed create wallet with blank description")
    void shouldNotAllowedCreateWalletWithBlankDescription() {
        assertThrows(IllegalArgumentException.class, () -> new Wallet(
            new Wallet.Description(""),
            new User(),
            true
        ));
    }


}
