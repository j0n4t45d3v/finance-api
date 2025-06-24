package br.com.managementfinanceapi.adapter.in.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.managementfinanceapi.adapter.in.dto.ResponseV0;
import br.com.managementfinanceapi.adapter.in.dto.auth.Login;
import br.com.managementfinanceapi.adapter.in.dto.auth.RefreshBody;
import br.com.managementfinanceapi.adapter.in.dto.auth.TokenResponse;
import br.com.managementfinanceapi.adapter.in.dto.user.CreateUser;
import br.com.managementfinanceapi.application.port.in.user.LoginPort;
import br.com.managementfinanceapi.application.port.in.user.RegisterUserPort;
import br.com.managementfinanceapi.application.port.out.security.jwt.GenerateTokenPort;
import br.com.managementfinanceapi.application.port.out.security.jwt.TokenReaderPort;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/auth")
public class AuthControllerV1 {

  private final RegisterUserPort registerUserPort;
  private final LoginPort loginGateway;
  private final GenerateTokenPort generateToken;
  private final TokenReaderPort TokenReader;

  public AuthControllerV1(
      RegisterUserPort registerUserPort,
      LoginPort loginGateway,
      GenerateTokenPort generateToken,
      TokenReaderPort tokenReader
  ) {
    this.registerUserPort = registerUserPort;
    this.loginGateway = loginGateway;
    this.generateToken = generateToken;
    this.TokenReader = tokenReader;
  }

  @PostMapping("/register")
  public ResponseEntity<ResponseV0<TokenResponse>> register(@Valid @RequestBody CreateUser body) {
    var userCreated = this.registerUserPort.execute(body.toDomain(), body.confirmPassword());
    var uri = UriComponentsBuilder
        .fromPath("/v1/users/{id}")
        .buildAndExpand(userCreated.getId())
        .toUri();

    return ResponseEntity.created(uri).body(ResponseV0.created(this.generateTokens(userCreated.getEmail())));
  }

  @PostMapping("/login")
  public ResponseEntity<ResponseV0<TokenResponse>> login(@Valid @RequestBody Login body) {
    var login = this.loginGateway.execute(body.tDomain());
    return ResponseEntity.ok(ResponseV0.ok(this.generateTokens(login.getEmail())));
  }

  @PostMapping("/refresh")
  public ResponseEntity<ResponseV0<TokenResponse>> refresh(@RequestBody RefreshBody body) {
    String subject = this.TokenReader.getSubject(body.token());
    return ResponseEntity.ok(ResponseV0.ok(this.generateTokens(subject)));
  }

  private TokenResponse generateTokens(String subject) {
    var accessToken = this.generateToken.access(subject);
    var refreshToken = this.generateToken.refresh(subject);
    return new TokenResponse(accessToken, refreshToken);
  }

}