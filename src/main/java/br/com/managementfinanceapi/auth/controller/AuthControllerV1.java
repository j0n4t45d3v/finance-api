package br.com.managementfinanceapi.auth.controller;

import br.com.managementfinanceapi.auth.domain.dto.Login;
import br.com.managementfinanceapi.auth.domain.dto.TokenResponse;
import br.com.managementfinanceapi.auth.gateway.GenerateTokenGateway;
import br.com.managementfinanceapi.auth.gateway.LoginGateway;
import br.com.managementfinanceapi.infra.http.dto.ResponseV0;
import br.com.managementfinanceapi.user.domain.dto.CreateUser;
import br.com.managementfinanceapi.user.gateways.RegisterUser;
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

  private final RegisterUser registerUser;
  private final LoginGateway loginGateway;
  private final GenerateTokenGateway generateToken;

  public AuthControllerV1(
      RegisterUser registerUser,
      LoginGateway loginGateway,
      GenerateTokenGateway generateToken
  ) {
    this.registerUser = registerUser;
    this.loginGateway = loginGateway;
    this.generateToken = generateToken;
  }

  @PostMapping("/register")
  public ResponseEntity<ResponseV0<TokenResponse>> register(@Valid @RequestBody CreateUser body) {
    var userCreated = this.registerUser.execute(body);
    var response =this.generateToken.all(userCreated);
    var uri = UriComponentsBuilder
        .fromPath("/v1/users/{id}")
        .buildAndExpand(userCreated.getId())
        .toUri();
    return ResponseEntity.created(uri).body(ResponseV0.created(response));
  }

  @PostMapping("/login")
  public ResponseEntity<ResponseV0<TokenResponse>> login(@Valid @RequestBody Login body) {
    var login = this.loginGateway.execute(body);
    var response = this.generateToken.all(login);
    return ResponseEntity.ok(ResponseV0.ok(response));
  }

  public record RefreshBody (String token) {}
  @PostMapping("/refresh")
  public ResponseEntity<ResponseV0<TokenResponse>> refresh(@RequestBody RefreshBody body) {
    var response = this.generateToken.refresh(body.token());
    return ResponseEntity.ok(ResponseV0.ok(response));
  }
}