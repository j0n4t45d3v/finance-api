package com.jonatas.finance.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.jonatas.finance.faker.Faker;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authService;

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

}