package com.jonatas.finance.auth;

import java.util.Optional;

import com.jonatas.finance.adapter.security.TokenInfo;
import com.jonatas.finance.common.Result;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jonatas.finance.adapter.security.DecodedToken;
import com.jonatas.finance.adapter.security.TokenProvider;
import com.jonatas.finance.auth.AuthController.RefreshTokenRequest;
import com.jonatas.finance.auth.AuthController.RegisterUserRequest;
import com.jonatas.finance.common.dto.Token;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public LoginResult login(Email email, String password) {
        Optional<User> userFound = this.userRepository.findByEmail(email);
        if (userFound.isEmpty()) {
            return new LoginResult.InvalidCredentials();
        }

        User user = userFound.get();
        if (!this.passwordEncoder.matches(password, user.getPassword())) {
            return new LoginResult.InvalidCredentials();
        }

        var pairToken = this.tokenProvider.generatePairToken(user);
        Token accessToken = makeToken(pairToken.access());
        Token refreshToken = makeToken(pairToken.refresh());
        return new LoginResult.Success(accessToken, refreshToken);
    }

    public RefreshTokenResult refresh(RefreshTokenRequest request) {
        DecodedToken decodedToken = this.tokenProvider.validateRefreshToken(request.refreshToken());

        Optional<User> subjectFound = this.userRepository.findByEmail(Email.of(decodedToken.subject()));
        if (subjectFound.isEmpty()) {
            return new RefreshTokenResult.InvalidSubject();
        }
        var pairToken = this.tokenProvider.generatePairToken(subjectFound.get());
        Token newAccessToken = makeToken(pairToken.access());
        Token newRefreshToken = makeToken(pairToken.refresh());
        return new RefreshTokenResult.Success(newAccessToken, newRefreshToken);
    }

    private Token makeToken(TokenInfo tokenInfo) {
        return new Token(tokenInfo.value(),
                         tokenInfo.expiration()
                                  .getEpochSecond());
    }

    public Result<Void> register(RegisterUserRequest request) {
        if (!request.password().equals(request.confirmPassword())) {
            return Result.failure(AuthErrorCode.PASSWORD_MISMATCH);
        }

        Email email = new Email(request.email());
        Optional<User> userFound = this.userRepository.findByEmail(email);
        if (userFound.isPresent()) {
            return Result.failure(AuthErrorCode.FAIL_REGISTER);
        }
        Password passwordEncoded = new Password(this.passwordEncoder.encode(request.password()));
        this.userRepository.save(new User(email, passwordEncoded));
        return Result.successVoid();
    }
}
