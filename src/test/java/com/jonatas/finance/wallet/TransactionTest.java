package com.jonatas.finance.wallet;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.jonatas.finance.auth.User;
import com.jonatas.finance.common.exception.DomainException;
import com.jonatas.finance.faker.Faker;
import com.jonatas.finance.wallet.Transaction.Amount;
import com.jonatas.finance.wallet.Transaction.Description;
import com.jonatas.finance.wallet.Transaction.Timestamp;

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
        Transaction transaction = new Transaction(null,
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

    @ParameterizedTest(name="{0}")
    @MethodSource("providerNullRequiredFields")
    void shouldNotAllowCreateTransactionWhenRequiredFieldIsNull(String scenery,
                                                                String description,
                                                                BigDecimal amount,
                                                                LocalDateTime transactionAt,
                                                                Wallet wallet,
                                                                User user,
                                                                Category category) {

        var transactionFaker = Faker.transaction()
                                    .withDescription(description)
                                    .withAmount(amount)
                                    .withTransactionAt(transactionAt)
                                    .withWallet(wallet)
                                    .withUser(user)
                                    .withCategory(category);

        assertThatNullPointerException().isThrownBy(transactionFaker::get)
                                        .withMessageContaining("is required");

    }

    static Stream<Arguments> providerNullRequiredFields() {
        var description = Faker.text(4);
        var now = LocalDateTime.now();
        var wallet = Faker.wallet().get();
        var category = Faker.category().get();
        return Stream.of(
                Arguments.of("amount is null", description, null, now, wallet, wallet.getUser(), category),
                Arguments.of("transactionAt is null", description, BigDecimal.ONE, null, wallet, wallet.getUser(), category),
                Arguments.of("wallet is null", description, BigDecimal.ONE, now, null, wallet.getUser(), category),
                Arguments.of("user is null", description, BigDecimal.ONE, now, wallet, null, category),
                Arguments.of("category is null", description, BigDecimal.ONE, now, wallet, wallet.getUser(), null)
        );

    }


    @Nested
    class DescriptionTest {

        @ParameterizedTest(name = "{0}")
        @MethodSource("validValues")
        void shouldInstanceDescription(String scenery, String value) {
            assertThatNoException().isThrownBy(() -> Description.of(value));
        }

        static Stream<Arguments> validValues() {
            return Stream.of(Arguments.of("empty value", ""),
                             Arguments.of("blank value", " "),
                             Arguments.of("null value", null),
                             Arguments.of("one character", "a"),
                             Arguments.of("below maximum length", "a".repeat(Description.MAX_LENGTH - 1)),
                             Arguments.of("exactly maximum length", "a".repeat(Description.MAX_LENGTH)));
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidValues")
        void shouldThrowExceptionWhenLengthExceedMaximum(String scenery, String value) {
            assertThatException().isThrownBy(() -> Description.of(value)).isInstanceOf(DomainException.class);
        }

        static Stream<Arguments> invalidValues() {
            return Stream.of(Arguments.of("1 above maximum length", "a".repeat(Description.MAX_LENGTH + 1)),
                             Arguments.of("50 above maximum length", "a".repeat(Description.MAX_LENGTH + 50)));
        }
    }

    @Nested
    class AmountTest {

        @MethodSource("validValues")
        @ParameterizedTest(name = "{0}")
        void shouldInstanceAmount(String scenery, BigDecimal value) {
            assertThat(Amount.of(value)).extracting(Amount::value)
                                        .isEqualTo(value.setScale(2, RoundingMode.HALF_UP));
        }

        static Stream<Arguments> validValues() {
            return Stream.of(Arguments.of("1 value", BigDecimal.ONE),
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
        void shouldThrowExceptionWhenGivenInvalidInput(String scenery, BigDecimal value) {
            assertThatException().isThrownBy(() -> Amount.of(value))
                                 .isInstanceOf(DomainException.class);
        }

        static Stream<Arguments> invalidValues() {
            return Stream.of(Arguments.of("amount zero", BigDecimal.ZERO),
                             Arguments.of("amount negative", BigDecimal.TEN.multiply(BigDecimal.ONE.negate())));
        }
    }
}
