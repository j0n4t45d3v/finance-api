package br.com.managementfinanceapi.adapter.in.controller.user;

import br.com.managementfinanceapi.application.core.domain.user.dto.auth.Login;
import br.com.managementfinanceapi.application.core.domain.user.dto.auth.RefreshBody;
import br.com.managementfinanceapi.application.core.domain.user.dto.auth.TokenResponse;
import br.com.managementfinanceapi.application.port.in.user.GenerateTokenGateway;
import br.com.managementfinanceapi.application.port.in.user.LoginGateway;
import br.com.managementfinanceapi.infra.http.dto.ResponseV0;
import br.com.managementfinanceapi.application.core.domain.user.dto.CreateUser;
import br.com.managementfinanceapi.application.port.in.user.RegisterUser;
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

  @PostMapping("/refresh")
  public ResponseEntity<ResponseV0<TokenResponse>> refresh(@RequestBody RefreshBody body) {
    var response = this.generateToken.refresh(body.token());
    return ResponseEntity.ok(ResponseV0.ok(response));
  }
}