package com.jonatas.finance.wallet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

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
import com.jonatas.finance.wallet.WalletController.EditWalletRequest;

@ExtendWith({ MockitoExtension.class })
public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    @Nested
    class Create {

        @ParameterizedTest
        @ValueSource(booleans = { false, true })
        void shouldCreateAWallet(boolean mainWallet) {

            var request = makeRequest(mainWallet);
            var wallet = Faker.wallet()
                              .withDescription(request.name())
                              .withMain(mainWallet)
                              .get();
            var user = wallet.getUser();

            if (mainWallet) {
                when(walletRepository.existsMainWalletForUser(user)).thenReturn(false);
            }

            when(walletRepository.existsByDescriptionAndUser(eq(wallet.getDescription()),
                                                             eq(user))).thenReturn(false);

            when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

            var result = walletService.create(request, user);

            assertThat(result.isFailure()).isFalse();
            assertThat(result.get()).isNotNull()
                                    .isNotNull()
                                    .isInstanceOf(Wallet.class)
                                    .isEqualTo(wallet);

            verify(walletRepository, times(1)).save(any(Wallet.class));
        }

        @Test
        void shouldNotAllowCreateMainWalletWhenUserAlreadyHasAMainWallet() {
            var user = Faker.user().get();

            when(walletRepository.existsMainWalletForUser(user)).thenReturn(true);

            var result = walletService.create(makeRequest(true), user);

            assertThat(result.isFailure()).isTrue();
            assertThat(result.getError()).isNotNull()
                                         .isEqualTo(WalletErrorCode.MAIN_WALLET_ALREADY_EXISTS);

            verify(walletRepository, never()).save(any(Wallet.class));
        }

        @Test
        void shouldNotAllowCreateWalletWhenUserAlreadyHasAWalletWithSameName() {
            var user = Faker.user().get();

            when(walletRepository.existsByDescriptionAndUser(any(Description.class), eq(user))).thenReturn(true);

            var result = walletService.create(makeRequest(false), user);

            assertThat(result.isFailure()).isTrue();
            assertThat(result.getError()).isNotNull()
                                         .isEqualTo(WalletErrorCode.WALLET_WITH_THIS_NAME_ALREADY_EXISTS);

            verify(walletRepository, never()).save(any(Wallet.class));
        }

        CreateWalletRequest makeRequest(boolean mainWallet) {
            return new CreateWalletRequest("WalletName", mainWallet);
        }
    }

    @Nested
    class Edit {
        @ParameterizedTest
        @ValueSource(booleans = { true, false })
        void shouldEditAWallet(boolean mainWallet) {
            var wallet = Faker.wallet().isMainWallet().get();
            var oldDescription = wallet.getDescription();

            when(walletRepository.findByIdAndUser(wallet.getId(),
                                                  wallet.getUser())).thenReturn(Optional.of(wallet));
            if (mainWallet) {
                when(walletRepository.existsMainWalletForUser(wallet.getUser(),
                                                              wallet.getId())).thenReturn(false);
            }
            when(walletRepository.existsByDescriptionAndUserNotAndId(any(Wallet.Description.class),
                                                                     eq(wallet.getUser()),
                                                                     eq(wallet.getId()))).thenReturn(false);

            when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

            var result = walletService.update(wallet.getId(), makeRequest(mainWallet), wallet.getUser());

            assertThat(result.isFailure()).isFalse();
            assertThat(result.get()).isNotNull()
                                    .extracting(Wallet::getDescription)
                                    .isNotNull()
                                    .isNotEqualTo(oldDescription);

            verify(walletRepository, times(1)).save(result.get());
        }

        @Test
        void shouldNotAllowEditWhenWalletDoesNotExist() {
            var user = Faker.user().get();
            var id = Faker.numberLong();

            when(walletRepository.findByIdAndUser(eq(id), eq(user))).thenReturn(Optional.empty());

            var result = walletService.update(id, makeRequest(true), user);

            assertThat(result.isFailure()).isTrue();
            assertThat(result.getError()).isNotNull()
                                         .isEqualTo(WalletErrorCode.WALLET_NOT_FOUND);

            verify(walletRepository, never()).save(any(Wallet.class));
        }

        @Test
        void shouldNotAllowChangeToMainWalletWhenAlreadyExistOtherMainWallet() {
            var wallet = Faker.wallet().isNotMainWallet().get();

            when(walletRepository.findByIdAndUser(wallet.getId(), wallet.getUser())).thenReturn(Optional.of(wallet));
            when(walletRepository.existsMainWalletForUser(wallet.getUser(), wallet.getId())).thenReturn(true);

            var result = walletService.update(wallet.getId(), makeRequest(true), wallet.getUser());

            assertThat(result.isFailure()).isTrue();
            assertThat(result.getError()).isNotNull()
                                         .isEqualTo(WalletErrorCode.MAIN_WALLET_ALREADY_EXISTS);

            verify(walletRepository, never()).save(any(Wallet.class));
        }

        @Test
        void shouldNotAllowEditWhenUserAlreadyHasOtherWalletWithSameDescription() {
            var wallet = Faker.wallet().isNotMainWallet().get();

            when(walletRepository.findByIdAndUser(wallet.getId(),
                                                  wallet.getUser())).thenReturn(Optional.of(wallet));
            when(walletRepository.existsMainWalletForUser(wallet.getUser(),
                                                          wallet.getId())).thenReturn(false);
            when(walletRepository.existsByDescriptionAndUserNotAndId(any(Description.class),
                                                                     eq(wallet.getUser()),
                                                                     eq(wallet.getId()))).thenReturn(true);

            var result = walletService.update(wallet.getId(), makeRequest(true), wallet.getUser());

            assertThat(result.isFailure()).isTrue();
            assertThat(result.getError()).isNotNull()
                                         .isEqualTo(WalletErrorCode.WALLET_WITH_THIS_NAME_ALREADY_EXISTS);

            verify(walletRepository, never()).save(any(Wallet.class));
        }

        EditWalletRequest makeRequest(boolean mainWallet) {
            return new EditWalletRequest("WalletName", mainWallet);
        }
    }
}
