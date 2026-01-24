package com.jonatas.finance.service.impl;

import com.jonatas.finance.domain.User;
import com.jonatas.finance.domain.dvo.user.Email;
import com.jonatas.finance.domain.dvo.user.Password;
import com.jonatas.finance.domain.exception.UnsuccessfulCreateUserException;
import com.jonatas.finance.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateUserServiceImpl createUserService;

    @Test
    @DisplayName("should create valid user")
    void shouldCreateValidUser() {
        var user = new User(new Email("john@doe.test"), new Password("secret password"));
        when(this.userRepository.findByEmail(any(Email.class))).thenReturn(Optional.empty());
        this.createUserService.execute(user);
        verify(this.userRepository, times(1)).findByEmail(any(Email.class));
        verify(this.userRepository, times(1)).save(user);
    }


    @Test
    @DisplayName("should not allow create user when email already used by other user")
    void shouldNotAllowCreateUserWhenEmailAlreadyUsedByOtherUser() {
        var user = new User(new Email("john@doe.test"), new Password("secret password"));
        User userMock = mock(User.class);
        when(this.userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(userMock));
        assertThrows(UnsuccessfulCreateUserException.class, () -> this.createUserService.execute(user));
        verify(this.userRepository, times(1)).findByEmail(any(Email.class));
        verify(this.userRepository, times(0)).save(user);
    }


}