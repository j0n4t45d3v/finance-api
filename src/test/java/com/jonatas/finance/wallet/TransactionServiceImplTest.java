package com.jonatas.finance.wallet;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jonatas.finance.auth.User;
import com.jonatas.finance.infra.provider.ClockProvider;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ClockProvider clockProvider;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void shouldCreateATransaction() {
        CreateTransactionRequest request = this.getCreateTransactionRequest();
        User userMock = mock(User.class);
        Category categoryMock = mock(Category.class);
        Wallet walletMock = mock(Wallet.class);

        when(this.categoryRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(categoryMock));
        when(this.walletRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(walletMock));
        when(this.clockProvider.now()).thenReturn(LocalDateTime.now());

        CreateTransactionResult result = this.transactionService.create(request, userMock);

        assertInstanceOf(CreateTransactionResult.Success.class, result);

        verify(this.categoryRepository, times(1)).findByIdAndUser(anyLong(), any(User.class));
        verify(this.walletRepository, times(1)).findByIdAndUser(anyLong(), any(User.class));
        verify(this.transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void shouldNotAllowCreateTransactionWhenUserCategoryDoesNotExists() {
        CreateTransactionRequest request = this.getCreateTransactionRequest();
        User userMock = mock(User.class);

        when(this.categoryRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.empty());

        CreateTransactionResult result = this.transactionService.create(request, userMock);

        assertInstanceOf(CreateTransactionResult.CategoryNotFound.class, result);

        verify(this.categoryRepository, times(1)).findByIdAndUser(anyLong(), any(User.class));
        verify(this.walletRepository, never()).findByIdAndUser(anyLong(), any(User.class));
        verify(this.transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void shouldNotAllowCreateTransactionWhenUserWalletDoesNotExists() {
        CreateTransactionRequest request = this.getCreateTransactionRequest();
        User userMock = mock(User.class);
        Category categoryMock = mock(Category.class);

        when(this.categoryRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(categoryMock));
        when(this.walletRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.empty());

        CreateTransactionResult result = this.transactionService.create(request, userMock);

        assertInstanceOf(CreateTransactionResult.WalletNotFound.class, result);

        verify(this.categoryRepository, times(1)).findByIdAndUser(anyLong(), any(User.class));
        verify(this.walletRepository, times(1)).findByIdAndUser(anyLong(), any(User.class));
        verify(this.transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void shouldNotAllowCreateTransactionWhenTransactionAtIsInTheFuture() {
        CreateTransactionRequest request = this.getCreateTransactionRequestInFuture();
        User userMock = mock(User.class);
        Category categoryMock = mock(Category.class);
        Wallet walletMock = mock(Wallet.class);

        when(this.categoryRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(categoryMock));
        when(this.walletRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(walletMock));
        when(this.clockProvider.now()).thenReturn(LocalDateTime.now());

        CreateTransactionResult result = this.transactionService.create(request, userMock);

        assertInstanceOf(CreateTransactionResult.TransactionCannotBeIsInTheFuture.class, result);

        verify(this.categoryRepository, times(1)).findByIdAndUser(anyLong(), any(User.class));
        verify(this.walletRepository, times(1)).findByIdAndUser(anyLong(), any(User.class));
        verify(this.transactionRepository, never()).save(any(Transaction.class));
    }

    private CreateTransactionRequest getCreateTransactionRequest() {
        return new CreateTransactionRequest(
                                            "test transaction",
                                            BigDecimal.ONE,
                                            LocalDateTime.of(LocalDate.of(1999, 12, 1), LocalTime.of(15, 12)),
                                            1L,
                                            1L);
    }

    private CreateTransactionRequest getCreateTransactionRequestInFuture() {
        return new CreateTransactionRequest(
                                            "test transaction",
                                            BigDecimal.ONE,
                                            LocalDateTime.of(LocalDate.of(9999, 12, 31), LocalTime.of(23, 59)),
                                            1L,
                                            1L);
    }
}
