package com.jonatas.finance.wallet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.jonatas.finance.auth.User;
import com.jonatas.finance.faker.Faker;

class WalletTest {

    @Test
    @DisplayName("should create valid wallet instance")
    void shouldCreateValidWalletInstance() {
        Wallet wallet = new Wallet(new Wallet.Description("test create wallet"), User.reference(1L), true);

        assertTrue(wallet.isMain());
        assertEquals("test create wallet", wallet.getDescriptionValue());
        assertNotNull(wallet.getUser());
    }

    @Test
    @DisplayName("should not allowed create wallet without user")
    void shouldNotAllowedCreateWalletWithoutUser() {
        assertThrows(
                     NullPointerException.class,
                     () -> new Wallet(new Wallet.Description("test create wallet"), null, true));
    }

    @Test
    @DisplayName("should not allowed create wallet without description")
    void shouldNotAllowedCreateWalletWithoutDescription() {
        assertThrows(NullPointerException.class, () -> new Wallet(null, User.reference(1L), true));
    }

    @Test
    @DisplayName("should not allowed create wallet with empty description")
    void shouldNotAllowedCreateWalletWithEmptyDescription() {
        assertThrows(
                     IllegalArgumentException.class,
                     () -> new Wallet(new Wallet.Description(""), User.reference(1L), true));
    }

    @Test
    @DisplayName("should not allowed create wallet with blank description")
    void shouldNotAllowedCreateWalletWithBlankDescription() {
        assertThrows(
                     IllegalArgumentException.class,
                     () -> new Wallet(new Wallet.Description(""), User.reference(1L), true));
    }

    @Nested
    class Change {
        @Test
        void shouldChangeOnlyTheFieldsDescriptionAndMainInWallet() {
            Wallet walletToEdit = Faker.wallet().get();
            Wallet wallet = Faker.wallet().get();
            Wallet updatedWallet = wallet.change(walletToEdit);

            assertThat(updatedWallet).isNotNull();
            assertThat(updatedWallet.getId()).isEqualTo(wallet.getId());
            assertThat(updatedWallet.getUser()).isEqualTo(wallet.getUser());

            assertThat(updatedWallet.getDescription()).isEqualTo(walletToEdit.getDescription());
            assertThat(updatedWallet.isMain()).isEqualTo(walletToEdit.isMain());

        }

    }
}
