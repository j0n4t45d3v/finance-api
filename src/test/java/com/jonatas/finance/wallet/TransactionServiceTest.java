package com.jonatas.finance.wallet;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import com.jonatas.finance.faker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jonatas.finance.adapter.time.ClockProvider;

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

        assertInstanceOf(CreateTransactionResult.Success.class, result);

        verify(this.transactionRepository, times(1)).save(any(Transaction.class));
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

        assertInstanceOf(CreateTransactionResult.CategoryNotFound.class, result);

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

        assertInstanceOf(CreateTransactionResult.WalletNotFound.class, result);

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

        assertInstanceOf(CreateTransactionResult.TransactionCannotBeIsInTheFuture.class, result);

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
