package com.jonatas.finance.service.impl.user;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jonatas.finance.controller.AuthController.RegisterUserRequest;
import com.jonatas.finance.domain.User;
import com.jonatas.finance.domain.dvo.user.Email;
import com.jonatas.finance.domain.dvo.user.Password;
import com.jonatas.finance.domain.exception.DomainException;
import com.jonatas.finance.domain.result.auth.LoginResult;
import com.jonatas.finance.domain.result.auth.RegisterResult;
import com.jonatas.finance.infra.dto.Token;
import com.jonatas.finance.infra.security.JwtService;
import com.jonatas.finance.repository.UserRepository;
import com.jonatas.finance.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(
            UserRepository userRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResult login(Email email, String password) {
        Optional<User> userFound = this.userRepository.findByEmail(email);
        if (userFound.isEmpty()) {
            return new LoginResult.InvalidCredentials();
        }

        User user = userFound.get();
        if (!this.passwordEncoder.matches(password, user.getPassword())) {
            return new LoginResult.InvalidCredentials();
        }

        Token accessToken = this.jwtService.generateToken(user);
        Token refreshToken = this.jwtService.generateRefreshToken(user);
        return new LoginResult.Success(accessToken, refreshToken);
    }

    @Override
    public RegisterResult register(RegisterUserRequest request) {
        if (!request.password().equals(request.confirmPassword())) {
            return new RegisterResult.NotMatchPasswords();
        }

        Email email = new Email(request.email());
        Optional<User> userFound = this.userRepository.findByEmail(email);
        if (userFound.isPresent()) {
            return new RegisterResult.FailRegister();
        }
        Password passwordEncoded = new Password(this.passwordEncoder.encode(request.password()));
        this.userRepository.save(new User(email, passwordEncoded));
        return new RegisterResult.Success();
    }

}
