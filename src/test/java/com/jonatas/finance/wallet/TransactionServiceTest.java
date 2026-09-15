package com.jonatas.finance.wallet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jonatas.finance.adapter.time.ClockProvider;
import com.jonatas.finance.faker.Faker;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ClockProvider clockProvider;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void shouldCreateATransaction() {
        var wallet = Faker.wallet().get();
        var user = wallet.getUser();
        var category = Faker.category()
                            .withUser(user)
                            .get();

        var request = this.getCreateTransactionRequest(wallet, category);

        when(this.categoryRepository.findByIdAndUser(eq(category.getId()),
                                                     eq(user))).thenReturn(Optional.of(category));
        when(this.walletRepository.findByIdAndUser(eq(wallet.getId()),
                                                   eq(user))).thenReturn(Optional.of(wallet));
        when(this.clockProvider.now()).thenReturn(LocalDateTime.now());

        var result = this.transactionService.create(request, user);

        assertThat(result.isFailure()).isFalse();

        var transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(this.transactionRepository, times(1)).save(transactionCaptor.capture());

        var saved = transactionCaptor.getValue();
        var expectedTransaction = Faker.transaction()
                                       .withDescription(request.description())
                                       .withAmount(request.amount())
                                       .withTransactionAt(request.datetime())
                                       .withWallet(wallet)
                                       .withCategory(category)
                                       .withUser(user)
                                       .get();

        assertThat(saved.getAmount()).isEqualTo(expectedTransaction.getAmount());
        assertThat(saved.getDescription()).isEqualTo(expectedTransaction.getDescription());
        assertThat(saved.getTransactionAt()).isEqualTo(expectedTransaction.getTransactionAt());
        assertThat(saved.getCategory()).isEqualTo(expectedTransaction.getCategory());
        assertThat(saved.getWallet()).isEqualTo(expectedTransaction.getWallet());
        assertThat(saved.getUser()).isEqualTo(expectedTransaction.getUser());
    }

    @Test
    void shouldNotAllowCreateTransactionWhenUserCategoryDoesNotExists() {
        var wallet = Faker.wallet().get();
        var user = wallet.getUser();
        var category = Faker.category()
                            .withUser(user)
                            .get();

        var request = this.getCreateTransactionRequest(wallet, category);

        when(this.categoryRepository.findByIdAndUser(eq(category.getId()),
                                                     eq(user))).thenReturn(Optional.empty());

        var result = this.transactionService.create(request, user);

        assertThat(result.isFailure()).isTrue();
        assertThatNoException().isThrownBy(() -> assertThat(result.getError()).isEqualTo(CategoryErrorCode.CATEGORY_NOT_FOUND));
        verify(this.transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void shouldNotAllowCreateTransactionWhenUserWalletDoesNotExists() {
        var wallet = Faker.wallet().get();
        var user = wallet.getUser();
        var category = Faker.category()
                            .withUser(user)
                            .get();

        var request = this.getCreateTransactionRequest(wallet, category);

        when(this.categoryRepository.findByIdAndUser(eq(category.getId()),
                                                     eq(user))).thenReturn(Optional.of(category));
        when(this.walletRepository.findByIdAndUser(eq(wallet.getId()),
                                                   eq(user))).thenReturn(Optional.empty());

        var result = this.transactionService.create(request, user);

        assertThat(result.isFailure()).isTrue();
        assertThatNoException().isThrownBy(() -> assertThat(result.getError()).isEqualTo(WalletErrorCode.WALLET_NOT_FOUND));

        verify(this.transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void shouldNotAllowCreateTransactionWhenTransactionAtIsInTheFuture() {
        var wallet = Faker.wallet().get();
        var user = wallet.getUser();
        var category = Faker.category()
                            .withUser(user)
                            .get();

        var request = this.getCreateTransactionRequestInFuture(wallet, category);

        when(this.categoryRepository.findByIdAndUser(eq(category.getId()),
                                                     eq(user))).thenReturn(Optional.of(category));
        when(this.walletRepository.findByIdAndUser(eq(wallet.getId()),
                                                   eq(user))).thenReturn(Optional.of(wallet));
        when(this.clockProvider.now()).thenReturn(LocalDateTime.now());

        var result = this.transactionService.create(request, user);

        assertThat(result.isFailure()).isTrue();
        assertThatNoException().isThrownBy(() -> assertThat(result.getError()).isEqualTo(TransactionErrorCode.TRANSACTION_CANNOT_BE_CREATED_IN_THE_FUTURE));

        verify(this.transactionRepository, never()).save(any(Transaction.class));
    }

    private CreateTransactionRequest getCreateTransactionRequest(Wallet wallet, Category category) {
        return new CreateTransactionRequest(Faker.text(100),
                                            BigDecimal.valueOf(Faker.numberDouble(1, 1000)),
                                            LocalDateTime.of(LocalDate.of(1999, 12, 1),
                                                             LocalTime.of(15, 12)),
                                            category.getId(),
                                            wallet.getId());
    }

    private CreateTransactionRequest getCreateTransactionRequestInFuture(Wallet wallet, Category category) {
        return new CreateTransactionRequest(Faker.text(100),
                                            BigDecimal.ONE,
                                            LocalDateTime.of(LocalDate.of(9999, 12, 31),
                                                             LocalTime.of(23, 59)),
                                            category.getId(),
                                            wallet.getId());
    }
}
