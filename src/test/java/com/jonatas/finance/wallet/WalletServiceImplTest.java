package com.jonatas.finance.wallet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jonatas.finance.faker.Faker;
import com.jonatas.finance.wallet.Wallet.Description;
import com.jonatas.finance.wallet.WalletController.CreateWalletRequest;

@ExtendWith({ MockitoExtension.class })
public class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletServiceImpl walletService;

    @Nested
    class Create {

        @ParameterizedTest
        @ValueSource(booleans = {false, true})
        void shouldCreateAWallet(boolean mainWallet) {
            var user = Faker.user().get();

            if (mainWallet) {
                when(walletRepository.existsMainWalletForUser(user)).thenReturn(!mainWallet);
            }

            when(walletRepository.existsByDescriptionAndUser(any(Wallet.Description.class), eq(user)))
                    .thenReturn(false);

            when(walletRepository.save(any(Wallet.class)))
                    .thenReturn(mock(Wallet.class));

            var result = walletService.create(makeRequest(mainWallet), user);

            assertThat(result).isNotNull().isInstanceOfSatisfying(CreateWalletResult.Success.class, s -> {
                assertThat(s.wallet()).isNotNull();
            });

            verify(walletRepository, times(1)).save(any(Wallet.class));
        }

        @Test
        void shouldNotAllowCreateMainWalletWhenUserAlreadyHasAMainWallet() {
            var user = Faker.user().get();

            when(walletRepository.existsMainWalletForUser(user)).thenReturn(true);

            var result = walletService.create(makeRequest(true), user);

            assertThat(result).isNotNull().isInstanceOf(CreateWalletResult.AlreadyExistsMainWalletForUser.class);

            verify(walletRepository, never()).save(any(Wallet.class));
        }

        @Test
        void shouldNotAllowCreateMainWalletWhenUserAlreadyHasAWalletWithSameName() {
            var user = Faker.user().get();

            when(walletRepository.existsByDescriptionAndUser(any(Description.class), eq(user))).thenReturn(true);

            var result = walletService.create(makeRequest(false), user);

            assertThat(result).isNotNull().isInstanceOf(CreateWalletResult.AlreadyExistsWalletWithThisName.class);

            verify(walletRepository, never()).save(any(Wallet.class));
        }

        CreateWalletRequest makeRequest(boolean mainWallet) {
            return new CreateWalletRequest("WalletName", mainWallet);
        }

    }
}
