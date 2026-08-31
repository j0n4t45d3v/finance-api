package com.jonatas.finance.wallet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.jonatas.finance.auth.User;
import com.jonatas.finance.common.exception.DomainException;
import com.jonatas.finance.wallet.Transaction.Amount;
import com.jonatas.finance.wallet.Transaction.Description;
import com.jonatas.finance.wallet.Transaction.Timestamp;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TransactionTest {

  @Test
  @DisplayName("should create a valid transaction")
  void shouldCreateAValidTransaction() {
    Timestamp now = Timestamp.now();
    Transaction transaction =
        new Transaction(
            new Description("test transaction"),
            new Amount(BigDecimal.ONE),
            now,
            Wallet.reference(1L),
            User.reference(1L),
            Category.reference(1L));

    assertEquals("test transaction", transaction.getDescriptionValue());
    assertEquals(BigDecimal.ONE.setScale(2, RoundingMode.HALF_UP), transaction.getAmountValue());
    assertEquals(now, transaction.getTransactionAt());
    assertNotNull(transaction.getWallet());
    assertNotNull(transaction.getUser());
    assertNotNull(transaction.getCategory());
  }

  @Test
  @DisplayName("should create transaction without description")
  void shouldCreateTransactionWithoutDescription() {
    Timestamp now = Timestamp.now();
    Transaction transaction =
        new Transaction(
            null,
            new Amount(BigDecimal.ONE),
            now,
            Wallet.reference(1L),
            User.reference(1L),
            Category.reference(1L));

    assertEquals("<without description>", transaction.getDescriptionValue());
    assertEquals(BigDecimal.ONE.setScale(2), transaction.getAmountValue());
    assertEquals(now, transaction.getTransactionAt());
    assertNotNull(transaction.getWallet());
    assertNotNull(transaction.getUser());
    assertNotNull(transaction.getCategory());
  }

  @Test
  @DisplayName("should not allowed create transaction without amount")
  void shouldNotAllowedCreateTransactionWithoutAmount() {
    assertThrows(
        NullPointerException.class,
        () ->
            new Transaction(
                new Description("test"),
                null,
                Timestamp.now(),
                Wallet.reference(1L),
                User.reference(1L),
                Category.reference(1L)));
  }

  @Test
  @DisplayName("should not allowed create transaction with amount less than zero")
  void shouldNotAllowedCreateTransactionWithAmountLessThanZero() {
    assertThrows(
        DomainException.class,
        () ->
            new Transaction(
                new Description("test"),
                new Amount(BigDecimal.valueOf(-1)),
                Timestamp.now(),
                Wallet.reference(1L),
                User.reference(1L),
                Category.reference(1L)));
  }

  @Test
  @DisplayName("should not allowed create transaction with zero amount")
  void shouldNotAllowedCreateTransactionWithZeroAmount() {
    assertThrows(
        DomainException.class,
        () ->
            new Transaction(
                new Description("test"),
                new Amount(BigDecimal.ZERO),
                Timestamp.now(),
                Wallet.reference(1L),
                User.reference(1L),
                Category.reference(1L)));
  }

  @Test
  @DisplayName("should not allowed create transaction without transactionAt")
  void shouldNotAllowedCreateTransactionWithoutTransactionAt() {
    assertThrows(
        NullPointerException.class,
        () ->
            new Transaction(
                new Description("test"),
                new Amount(BigDecimal.ONE),
                null,
                Wallet.reference(1L),
                User.reference(1L),
                Category.reference(1L)));
  }

  @Test
  @DisplayName("should not allowed create transaction without user wallet")
  void shouldNotAllowedCreateTransactionWithoutUserWallet() {
    assertThrows(
        NullPointerException.class,
        () ->
            new Transaction(
                new Description("test transaction"),
                new Amount(BigDecimal.ONE),
                Timestamp.now(),
                null,
                User.reference(1L),
                Category.reference(1L)));
  }

  @Test
  @DisplayName("should not allowed create transaction without user")
  void shouldNotAllowedCreateTransactionWithoutUser() {
    assertThrows(
        NullPointerException.class,
        () ->
            new Transaction(
                new Description("test transaction"),
                new Amount(BigDecimal.ONE),
                Timestamp.now(),
                Wallet.reference(1L),
                null,
                Category.reference(1L)));
  }

  @Test
  @DisplayName("should not allowed create transaction without category")
  void shouldNotAllowedCreateTransactionWithoutCategory() {
    assertThrows(
        NullPointerException.class,
        () ->
            new Transaction(
                new Description("test transaction"),
                new Amount(BigDecimal.ONE),
                Timestamp.now(),
                Wallet.reference(1L),
                User.reference(1L),
                null));
  }

  @Nested
  class DescriptionTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("validValues")
    void shouldInstanceDescription(String scenary, String value) {
      assertThatNoException().isThrownBy(() -> Description.of(value));
    }

    static Stream<Arguments> validValues() {
      return Stream.of(
          Arguments.of("empty value", ""),
          Arguments.of("blank value", " "),
          Arguments.of("null value", null),
          Arguments.of("one character", "a"),
          Arguments.of("below maximum length", "a".repeat(Description.MAX_LENGTH - 1)),
          Arguments.of("exactly maximum length", "a".repeat(Description.MAX_LENGTH)));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidValues")
    void shouldThrowExceptionWhenLengthExceedMaximun(String scenary, String value) {
      assertThatException()
          .isThrownBy(() -> Description.of(value))
          .isInstanceOf(DomainException.class);
    }

    static Stream<Arguments> invalidValues() {
      return Stream.of(
          Arguments.of("1 above maximum length", "a".repeat(Description.MAX_LENGTH + 1)),
          Arguments.of("50 above maximum length", "a".repeat(Description.MAX_LENGTH + 50)));
    }
  }

  @Nested
  class AmountTest {

    @MethodSource("validValues")
    @ParameterizedTest(name = "{0}")
    void shouldInstanceAmount(String scenary, BigDecimal value) {
      assertThat(Amount.of(value))
          .extracting(Amount::value)
          .satisfies(
              v -> {
                assertThat(v).isEqualTo(value.setScale(2, RoundingMode.HALF_UP));
              });
    }

    static Stream<Arguments> validValues() {
      return Stream.of(
          Arguments.of("1 value", BigDecimal.ONE),
          Arguments.of("10 value", BigDecimal.TEN),
          Arguments.of("fractional value 1.5", BigDecimal.valueOf(1.5)),
          Arguments.of("large number", new BigDecimal("999999999.99")));
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
      assertThatNullPointerException().isThrownBy(() -> Amount.of(null));
    }

    @MethodSource("invalidValues")
    @ParameterizedTest(name = "{0}")
    void shouldThrowExceptionWhenGivenInvalidInput(String scenary, BigDecimal value) {
      assertThatException().isThrownBy(() -> Amount.of(value)).isInstanceOf(DomainException.class);
    }

    static Stream<Arguments> invalidValues() {
      return Stream.of(
          Arguments.of("amount zero", BigDecimal.ZERO),
          Arguments.of("amount negative", BigDecimal.TEN.multiply(BigDecimal.ONE.negate())));
    }
  }
}
