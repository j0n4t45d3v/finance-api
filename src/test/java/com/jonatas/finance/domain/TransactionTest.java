package com.jonatas.finance.domain;

import com.jonatas.finance.domain.Transaction.Amount;
import com.jonatas.finance.domain.Transaction.Description;
import com.jonatas.finance.domain.Transaction.Timestamp;
import com.jonatas.finance.domain.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {


    @Test
    @DisplayName("should create a valid transaction")
    void shouldCreateAValidTransaction() {
        Timestamp now = Timestamp.now();
        Transaction transaction = new Transaction(
            new Description("test transaction"),
            new Amount(BigDecimal.ONE),
            now,
            new Wallet(),
            new User(),
            new Category()
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
            new Wallet(),
            new User(),
            new Category()
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
            new Wallet(),
            new User(),
            new Category()
        ));
    }

    @Test
    @DisplayName("should not allowed create transaction with amount less than zero")
    void shouldNotAllowedCreateTransactionWithAmountLessThanZero() {
        assertThrows(DomainException.class, () -> new Transaction(
            new Description("test"),
            new Amount(BigDecimal.valueOf(-1)),
            Timestamp.now(),
            new Wallet(),
            new User(),
            new Category()
        ));
    }

    @Test
    @DisplayName("should not allowed create transaction with zero amount")
    void shouldNotAllowedCreateTransactionWithZeroAmount() {
        assertThrows(DomainException.class, () -> new Transaction(
            new Description("test"),
            new Amount(BigDecimal.ZERO),
            Timestamp.now(),
            new Wallet(),
            new User(),
            new Category()
        ));
    }

    @Test
    @DisplayName("should not allowed create transaction without transactionAt")
    void shouldNotAllowedCreateTransactionWithoutTransactionAt() {
        assertThrows(NullPointerException.class, () -> new Transaction(
            new Description("test"),
            new Amount(BigDecimal.ONE),
            null,
            new Wallet(),
            new User(),
            new Category()
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
            new User(),
            new Category()
        ));
    }

    @Test
    @DisplayName("should not allowed create transaction without user")
    void shouldNotAllowedCreateTransactionWithoutUser() {
        assertThrows(NullPointerException.class, () -> new Transaction(
            new Description("test transaction"),
            new Amount(BigDecimal.ONE),
            Timestamp.now(),
            new Wallet(),
            null,
            new Category()
        ));
    }


    @Test
    @DisplayName("should not allowed create transaction without category")
    void shouldNotAllowedCreateTransactionWithoutCategory() {
        assertThrows(NullPointerException.class, () -> new Transaction(
            new Description("test transaction"),
            new Amount(BigDecimal.ONE),
            Timestamp.now(),
            new Wallet(),
            new User(),
            null
        ));
    }

}
