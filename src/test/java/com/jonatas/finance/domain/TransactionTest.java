package com.jonatas.finance.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jonatas.finance.auth.User;
import com.jonatas.finance.domain.exception.DomainException;
import com.jonatas.finance.wallet.Category;
import com.jonatas.finance.wallet.Transaction;
import com.jonatas.finance.wallet.Transaction.Amount;
import com.jonatas.finance.wallet.Transaction.Description;
import com.jonatas.finance.wallet.Transaction.Timestamp;
import com.jonatas.finance.wallet.Wallet;

class TransactionTest {


    @Test
    @DisplayName("should create a valid transaction")
    void shouldCreateAValidTransaction() {
        Timestamp now = Timestamp.now();
        Transaction transaction = new Transaction(
            new Description("test transaction"),
            new Amount(BigDecimal.ONE),
            now,
            Wallet.reference(1L),
            User.reference(1L),
            Category.reference(1L)
        );

        assertEquals("test transaction", transaction.getDescriptionValue());
        assertEquals(BigDecimal.ONE, transaction.getAmountValue());
        assertEquals(now, transaction.getTransactionAt());
        assertNotNull(transaction.getWallet());
        assertNotNull(transaction.getUser());
        assertNotNull(transaction.getCategory());
    }

    @Test
    @DisplayName("should create transaction without description")
    void shouldCreateTransactionWithoutDescription() {
        Timestamp now = Timestamp.now();
        Transaction transaction = new Transaction(
            null,
            new Amount(BigDecimal.ONE),
            now,
            Wallet.reference(1L),
            User.reference(1L),
            Category.reference(1L)
        );

        assertEquals("<without description>", transaction.getDescriptionValue());
        assertEquals(BigDecimal.ONE, transaction.getAmountValue());
        assertEquals(now, transaction.getTransactionAt());
        assertNotNull(transaction.getWallet());
        assertNotNull(transaction.getUser());
        assertNotNull(transaction.getCategory());
    }

    @Test
    @DisplayName("should not allowed create transaction without amount")
    void shouldNotAllowedCreateTransactionWithoutAmount() {
        assertThrows(NullPointerException.class, () -> new Transaction(
            new Description("test"),
            null,
            Timestamp.now(),
            Wallet.reference(1L),
            User.reference(1L),
            Category.reference(1L)
        ));
    }

    @Test
    @DisplayName("should not allowed create transaction with amount less than zero")
    void shouldNotAllowedCreateTransactionWithAmountLessThanZero() {
        assertThrows(DomainException.class, () -> new Transaction(
            new Description("test"),
            new Amount(BigDecimal.valueOf(-1)),
            Timestamp.now(),
            Wallet.reference(1L),
            User.reference(1L),
            Category.reference(1L)
        ));
    }

    @Test
    @DisplayName("should not allowed create transaction with zero amount")
    void shouldNotAllowedCreateTransactionWithZeroAmount() {
        assertThrows(DomainException.class, () -> new Transaction(
            new Description("test"),
            new Amount(BigDecimal.ZERO),
            Timestamp.now(),
            Wallet.reference(1L),
            User.reference(1L),
            Category.reference(1L)
        ));
    }

    @Test
    @DisplayName("should not allowed create transaction without transactionAt")
    void shouldNotAllowedCreateTransactionWithoutTransactionAt() {
        assertThrows(NullPointerException.class, () -> new Transaction(
            new Description("test"),
            new Amount(BigDecimal.ONE),
            null,
            Wallet.reference(1L),
            User.reference(1L),
            Category.reference(1L)
        ));
    }

    @Test
    @DisplayName("should not allowed create transaction without user wallet")
    void shouldNotAllowedCreateTransactionWithoutUserWallet() {
        assertThrows(NullPointerException.class, () -> new Transaction(
            new Description("test transaction"),
            new Amount(BigDecimal.ONE),
            Timestamp.now(),
            null,
            User.reference(1L),
            Category.reference(1L)
        ));
    }

    @Test
    @DisplayName("should not allowed create transaction without user")
    void shouldNotAllowedCreateTransactionWithoutUser() {
        assertThrows(NullPointerException.class, () -> new Transaction(
            new Description("test transaction"),
            new Amount(BigDecimal.ONE),
            Timestamp.now(),
            Wallet.reference(1L),
            null,
            Category.reference(1L)
        ));
    }


    @Test
    @DisplayName("should not allowed create transaction without category")
    void shouldNotAllowedCreateTransactionWithoutCategory() {
        assertThrows(NullPointerException.class, () -> new Transaction(
            new Description("test transaction"),
            new Amount(BigDecimal.ONE),
            Timestamp.now(),
            Wallet.reference(1L),
            User.reference(1L),
            null
        ));
    }

}
