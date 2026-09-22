package com.jonatas.finance.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.jonatas.finance.adapter.security.DecodedToken;
import com.jonatas.finance.adapter.security.PairToken;
import com.jonatas.finance.adapter.security.TokenProvider;
import com.jonatas.finance.faker.Faker;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Nested
    class Register {

        @Test
        void shouldRegisterUserWihSuccess() {
            var passwd = Faker.text(10);
            var passwdEncoded = Faker.text(20);
            var command = new AuthController.RegisterUserRequest(Faker.email(),
                                                                 passwd,
                                                                 passwd);
            var email = new Email(command.email());

            when(userRepository.findByEmail(eq(email))).thenReturn(Optional.empty());

            when(passwordEncoder.encode(eq(passwd))).thenReturn(passwdEncoded);

            var result = authService.register(command);

            assertThat(result).isInstanceOf(RegisterResult.Success.class);

            var userCapture = ArgumentCaptor.forClass(User.class);
            verify(userRepository, times(1)).save(userCapture.capture());

            var userSaved = userCapture.getValue();
            assertThat(userSaved).isNotNull();
            assertThat(userSaved.getEmail()).isNotNull()
                                            .isEqualTo(email);
            assertThat(userSaved.getPassword()).isNotNull()
                                               .isEqualTo(passwdEncoded);
        }

        @Test
        void shouldNotAllowRegisterUserWhenDoesNotMatchPasswords() {
            var command = new AuthController.RegisterUserRequest(Faker.email(),
                                                                 Faker.text(10),
                                                                 Faker.text(10));

            var result = authService.register(command);

            assertThat(result).isInstanceOf(RegisterResult.NotMatchPasswords.class);

            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        void shouldNotAllowRegisterUserWhenEmailIsAlreadyBeingUsed() {
            var passwd = Faker.text(10);
            var command = new AuthController.RegisterUserRequest(Faker.email(),
                                                                 passwd,
                                                                 passwd);
            var email = new Email(command.email());

            when(userRepository.findByEmail(eq(email))).thenReturn(Optional.of(Faker.user().get()));

            var result = authService.register(command);

            assertThat(result).isInstanceOf(RegisterResult.FailRegister.class);

            verify(userRepository, never()).save(any(User.class));
        }

    }

    @Nested
    class Login {
        @Test
        void shouldLoginWithSuccess() {
            var user = Faker.user().get();

            when(tokenProvider.generatePairToken(user)).thenReturn(new PairToken(new com.jonatas.finance.adapter.security.Token(Faker.text(10),
                                                                                                                                Faker.instant()),
                                                                                 new com.jonatas.finance.adapter.security.Token(Faker.text(10),
                                                                                                                                Faker.instant())));

            when(userRepository.findByEmail(eq(user.getEmail()))).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(eq(user.getPasswordValue()),
                                         eq(user.getPassword()))).thenReturn(true);

            var result = authService.login(user.getEmail(), user.getPasswordValue());
            assertThat(result).isInstanceOf(LoginResult.Success.class)
                              .extracting("access", "refresh")
                              .doesNotContainNull();
        }

        @Test
        void shouldResultInvalidCredentialsWhenEmailDoesNotExists() {
            when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.empty());

            var result = authService.login(Email.of(Faker.email()), Faker.text(10));

            assertThat(result).isInstanceOf(LoginResult.InvalidCredentials.class);
        }

        @Test
        void shouldResultInvalidCredentialsWhenPasswordNotMatchWithUserFound() {
            when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(Faker.user().get()));
            when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

            var result = authService.login(Email.of(Faker.email()), Faker.text(10));

            assertThat(result).isInstanceOf(LoginResult.InvalidCredentials.class);
        }

    }

    @Nested
    class Refresh {

        @Test
        void shouldRefreshTokenWithSuccess() {
            var validatedToken = new DecodedToken(UUID.randomUUID().toString(),
                                                  Faker.email(),
                                                  "refresh",
                                                  Faker.instant(),
                                                  Faker.instant());

            var user = Faker.user()
                            .withEmail(validatedToken.subject())
                            .get();
            when(tokenProvider.validateRefreshToken(anyString())).thenReturn(validatedToken);

            when(userRepository.findByEmail(eq(user.getEmail()))).thenReturn(Optional.of(user));
            when(tokenProvider.generatePairToken(user)).thenReturn(new PairToken(new com.jonatas.finance.adapter.security.Token(Faker.text(10),
                                                                                                                                Faker.instant()),
                                                                                 new com.jonatas.finance.adapter.security.Token(Faker.text(10),
                                                                                                                                Faker.instant())));

            var result = authService.refresh(new AuthController.RefreshTokenRequest(Faker.text(20)));

            assertThat(result).isInstanceOf(RefreshTokenResult.Success.class)
                              .extracting("access", "refresh")
                              .doesNotContainNull();
        }

        @Test
        void shouldAllowRefreshTokenWhenNotFoundUserToSubject() {
            var validatedToken = new DecodedToken(UUID.randomUUID().toString(),
                                                  Faker.email(),
                                                  "refresh",
                                                  Faker.instant(),
                                                  Faker.instant());

            when(tokenProvider.validateRefreshToken(anyString())).thenReturn(validatedToken);

            when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.empty());

            var result = authService.refresh(new AuthController.RefreshTokenRequest(Faker.text(20)));

            assertThat(result).isInstanceOf(RefreshTokenResult.InvalidSubject.class);
        }

    }

}