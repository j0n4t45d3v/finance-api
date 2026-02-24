package com.jonatas.finance.service.impl.account;

import com.jonatas.finance.domain.Account;
import com.jonatas.finance.domain.Category;
import com.jonatas.finance.domain.Transaction;
import com.jonatas.finance.domain.User;
import com.jonatas.finance.domain.result.account.CreateTransactionResult;
import com.jonatas.finance.dto.account.CreateTransactionRequest;
import com.jonatas.finance.infra.provider.ClockProvider;
import com.jonatas.finance.repository.AccountRepository;
import com.jonatas.finance.repository.CategoryRepository;
import com.jonatas.finance.repository.TransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ClockProvider clockProvider;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    @DisplayName("should create a transaction")
    void shouldCreateATransaction() {
        CreateTransactionRequest request = this.getCreateTransactionRequest();
        User userMock = mock(User.class);
        Category categoryMock = mock(Category.class);
        Account accountMock = mock(Account.class);

        when(this.categoryRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(categoryMock));
        when(this.accountRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(accountMock));
        when(this.clockProvider.now()).thenReturn(LocalDateTime.now());

        CreateTransactionResult result = this.transactionService.create(request, userMock);

        assertInstanceOf(CreateTransactionResult.Success.class, result);

        verify(this.categoryRepository, times(1)).findByIdAndUser(anyLong(), any(User.class));
        verify(this.accountRepository, times(1)).findByIdAndUser(anyLong(), any(User.class));
        verify(this.transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("should not allowed create transaction when user category not exists")
    void shouldNotAllowedCreateTransactionWhenUserCategoryNotExists() {
        CreateTransactionRequest request = this.getCreateTransactionRequest();
        User userMock = mock(User.class);

        when(this.categoryRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.empty());

        CreateTransactionResult result = this.transactionService.create(request, userMock);

        assertInstanceOf(CreateTransactionResult.CategoryNotFound.class, result);

        verify(this.categoryRepository, times(1)).findByIdAndUser(anyLong(), any(User.class));
        verify(this.accountRepository, never()).findByIdAndUser(anyLong(), any(User.class));
        verify(this.transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    @DisplayName("should not allowed create transaction when user account not exists")
    void shouldNotAllowedCreateTransactionWhenUserAccountNotExists() {
        CreateTransactionRequest request = this.getCreateTransactionRequest();
        User userMock = mock(User.class);
        Category categoryMock = mock(Category.class);

        when(this.categoryRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(categoryMock));
        when(this.accountRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.empty());

        CreateTransactionResult result = this.transactionService.create(request, userMock);

        assertInstanceOf(CreateTransactionResult.AccountNotFound.class, result);

        verify(this.categoryRepository, times(1)).findByIdAndUser(anyLong(), any(User.class));
        verify(this.accountRepository, times(1)).findByIdAndUser(anyLong(), any(User.class));
        verify(this.transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    @DisplayName("should not allowed create transaction when transactionAt is in the future")
    void shouldNotAllowedCreateTransactionWhenTransactionAtIsInTheFuture() {
        CreateTransactionRequest request = this.getCreateTransactionRequestInFuture();
        User userMock = mock(User.class);
        Category categoryMock = mock(Category.class);
        Account accountMock = mock(Account.class);

        when(this.categoryRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(categoryMock));
        when(this.accountRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(accountMock));
        when(this.clockProvider.now()).thenReturn(LocalDateTime.now());

        CreateTransactionResult result = this.transactionService.create(request, userMock);

        assertInstanceOf(CreateTransactionResult.TransactionCannotBeIsInTheFuture.class, result);

        verify(this.categoryRepository, times(1)).findByIdAndUser(anyLong(), any(User.class));
        verify(this.accountRepository, times(1)).findByIdAndUser(anyLong(), any(User.class));
        verify(this.transactionRepository, never()).save(any(Transaction.class));
    }


    private CreateTransactionRequest getCreateTransactionRequest() {
        return new CreateTransactionRequest(
            "test transaction",
            BigDecimal.ONE,
            LocalDateTime.of(LocalDate.of(1999, 12, 1), LocalTime.of(15, 12)),
            1L,
            1L
        );
    }

    private CreateTransactionRequest getCreateTransactionRequestInFuture() {
        return new CreateTransactionRequest(
            "test transaction",
            BigDecimal.ONE,
            LocalDateTime.of(LocalDate.of(9999, 12, 31), LocalTime.of(23, 59)),
            1L,
            1L
        );
    }


}
