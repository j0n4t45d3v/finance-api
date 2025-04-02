package br.com.managementfinanceapi.auth.controller;

import br.com.managementfinanceapi.auth.domain.dto.Login;
import br.com.managementfinanceapi.auth.gateway.LoginGateway;
import br.com.managementfinanceapi.infra.http.dto.ResponseV0;
import br.com.managementfinanceapi.user.domain.dto.CreateUser;
import br.com.managementfinanceapi.user.gateways.RegisterUser;
import br.com.managementfinanceapi.utils.JWTUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/v1/auth")
public class AuthControllerV1 {

  private final RegisterUser registerUser;
  private final LoginGateway loginGateway;
  private final JWTUtils jwtUtils;

  public AuthControllerV1(RegisterUser registerUser, LoginGateway loginGateway, JWTUtils jwtUtils) {
    this.registerUser = registerUser;
    this.loginGateway = loginGateway;
    this.jwtUtils = jwtUtils;
  }

  public record Token(String token, Long expiresIn) {}
  public record TokenResponse(Token accessToken, Token refreshToken) {}

  @PostMapping("/register")
  public ResponseEntity<ResponseV0<Token>> register(@Valid @RequestBody CreateUser body) {
    var userCreated = this.registerUser.execute(body);
    var tokenGenerated = this.jwtUtils.generateToken(userCreated);
    var tokenResponse = new Token(tokenGenerated, tokenGenerated, 0L);
    var response = ResponseV0.created(tokenResponse);
    var uri = UriComponentsBuilder
        .fromPath("/v1/users/{id}")
        .buildAndExpand(userCreated.getId())
        .toUri();
    return ResponseEntity.created(uri).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<ResponseV0<Token>> login(@Valid @RequestBody Login body) {
    var token = this.loginGateway.execute(body);
    var response = ResponseV0.ok(token);
    return ResponseEntity.ok(response);
  }
}