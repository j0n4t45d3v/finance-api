package br.com.managementfinanceapi.adapter.in.controller.user;

import br.com.managementfinanceapi.adapter.in.dto.auth.Login;
import br.com.managementfinanceapi.adapter.in.dto.auth.RefreshBody;
import br.com.managementfinanceapi.adapter.in.dto.auth.TokenResponse;
import br.com.managementfinanceapi.application.port.in.user.GenerateTokenPort;
import br.com.managementfinanceapi.application.port.in.user.LoginPort;
import br.com.managementfinanceapi.infra.http.dto.ResponseV0;
import br.com.managementfinanceapi.adapter.in.dto.user.CreateUser;
import br.com.managementfinanceapi.application.port.in.user.RegisterUserPort;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/v1/auth")
public class AuthControllerV1 {

  private final RegisterUserPort registerUserPort;
  private final LoginPort loginGateway;
  private final GenerateTokenPort generateToken;

  public AuthControllerV1(
      RegisterUserPort registerUserPort,
      LoginPort loginGateway,
      GenerateTokenPort generateToken
  ) {
    this.registerUserPort = registerUserPort;
    this.loginGateway = loginGateway;
    this.generateToken = generateToken;
  }

  @PostMapping("/register")
  public ResponseEntity<ResponseV0<TokenResponse>> register(@Valid @RequestBody CreateUser body) {
    var userCreated = this.registerUserPort.execute(body.toDomain(), body.confirmPassword());
    var response = this.generateToken.all(userCreated);
    var uri = UriComponentsBuilder
        .fromPath("/v1/users/{id}")
        .buildAndExpand(userCreated.getId())
        .toUri();
    return ResponseEntity.created(uri).body(ResponseV0.created(response));
  }

  @PostMapping("/login")
  public ResponseEntity<ResponseV0<TokenResponse>> login(@Valid @RequestBody Login body) {
    var login = this.loginGateway.execute(body.tDomain());
    var response = this.generateToken.all(login);
    return ResponseEntity.ok(ResponseV0.ok(response));
  }

  @PostMapping("/refresh")
  public ResponseEntity<ResponseV0<TokenResponse>> refresh(@RequestBody RefreshBody body) {
    var response = this.generateToken.refresh(body.token());
    return ResponseEntity.ok(ResponseV0.ok(response));
  }
}