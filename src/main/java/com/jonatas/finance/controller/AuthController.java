package com.jonatas.finance.controller;

import com.jonatas.finance.domain.dvo.user.Email;
import com.jonatas.finance.domain.result.auth.LoginResult;
import com.jonatas.finance.domain.result.auth.RefreshTokenResult;
import com.jonatas.finance.domain.result.auth.RegisterResult;
import com.jonatas.finance.dto.Response;
import com.jonatas.finance.dto.Token;
import com.jonatas.finance.infra.error.Error;
import com.jonatas.finance.infra.swagger.annotation.AuthTag;
import com.jonatas.finance.infra.swagger.annotation.DefaultErrorResponses;
import com.jonatas.finance.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@AuthTag
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public record LoginRequest(String email, String password) {
    }

    public record LoginResponse(Token access, Token refresh) {
    }

    @Operation(
        summary = "Login do usuario",
        description = "Retorna o token de acesso e refresh token quando logado com sucesso"
    )
    @PostMapping("/login")
    @DefaultErrorResponses
    @ApiResponse(responseCode = "200", description = "Ok")
    public ResponseEntity<Response<?, ?>> login(@RequestBody LoginRequest loginRequest) {
        var loginResult = this.authService.login(new Email(loginRequest.email()), loginRequest.password());
        if (loginResult instanceof LoginResult.InvalidCredentials) {
            var errorCredentials = new Error<>("fail_authentication", "e-mail or password invalid");
            return ResponseEntity.badRequest().body(Response.ofError(errorCredentials, Response.Status.BAD_REQUEST));
        }
        LoginResult.Success successResult = (LoginResult.Success) loginResult;
        LoginResponse loginResponse = new LoginResponse(successResult.access(), successResult.refresh());
        return ResponseEntity.ok(Response.of(loginResponse));
    }

    public record RefreshTokenRequest(String refreshToken) {
    }

    @Operation(
        summary = "Atualiza Token",
        description = "Realiza a atualização do token de acesso expirado para um novo válido retornando o novo token de acesso e novo refresh token"
    )
    @PostMapping("/refresh")
    @DefaultErrorResponses
    @ApiResponse(responseCode = "200", description = "Ok")
    public ResponseEntity<Response<?, ?>> refreshToken(@RequestBody RefreshTokenRequest request) {
        var result = this.authService.refresh(request);
        if (result instanceof RefreshTokenResult.InvalidRefreshToken) {
            var invalidToken = new Error<>("invalid_token", "invalid refresh token");
            return ResponseEntity
                .badRequest()
                .body(Response.ofError(invalidToken, Response.Status.BAD_REQUEST));
        }
        if (result instanceof RefreshTokenResult.InvalidSubject) {
            var invalidSubject = new Error<>("invalid_subject_token", "invalid subject in refresh token");
            return ResponseEntity
                .badRequest()
                .body(Response.ofError(invalidSubject, Response.Status.BAD_REQUEST));
        }
        RefreshTokenResult.Success successResult = (RefreshTokenResult.Success) result;
        LoginResponse loginResponse = new LoginResponse(successResult.access(), successResult.refresh());
        return ResponseEntity.ok(Response.of(loginResponse));
    }

    public record RegisterUserRequest(
        String email,
        String password,
        String confirmPassword
    ) {
    }

    @Operation(
        summary = "Cadastra novo usuário",
        description = "Realiza o cadastro o usuario na base dados"
    )
    @PostMapping("/register")
    @DefaultErrorResponses
    @ApiResponse(
        responseCode = "201",
        description = "Created",
        headers = {@Header(name = "Location")}
    )
    public ResponseEntity<?> register(@RequestBody RegisterUserRequest request) {
        var registerResult = this.authService.register(request);

        if (registerResult instanceof RegisterResult.NotMatchPasswords) {
            Error<String> error = new Error<>("not_match_passwords", "not match passwords");
            return ResponseEntity.badRequest().body(Response.ofError(error, Response.Status.BAD_REQUEST));
        }

        if (registerResult instanceof RegisterResult.FailRegister) {
            Error<String> error = new Error<>("fail_register_user", "Fail register user try later");
            return ResponseEntity.badRequest().body(Response.ofError(error, Response.Status.BAD_REQUEST));
        }

        URI location = UriComponentsBuilder
            .fromPath("/v1/users/me")
            .buildAndExpand()
            .toUri();
        return ResponseEntity.created(location).build();
    }

}
