package br.com.managementfinanceapi.adapter.in.controller.transaction;

import br.com.managementfinanceapi.infra.http.dto.ResponseV0;
import br.com.managementfinanceapi.application.core.domain.user.dto.UserBalanceDto;
import br.com.managementfinanceapi.application.port.in.transaction.AddCurrentAccountBalancePort;

import org.springframework.http.ResponseEntity; import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/balances")
public class BalanceControllerV1 {

  private final AddCurrentAccountBalancePort addCurrentAccountBalancePort;

  public BalanceControllerV1(AddCurrentAccountBalancePort addCurrentAccountBalancePort) {
    this.addCurrentAccountBalancePort = addCurrentAccountBalancePort;
  }

  @PostMapping("/{userId}/initial_balance")
  public ResponseEntity<ResponseV0<String>> addCurrentAccountBalance(
      @PathVariable("userId") Long userId,
      @RequestBody UserBalanceDto balance
  ) {
    this.addCurrentAccountBalancePort.execute(userId, balance.toDomain());
    return ResponseEntity.ok(ResponseV0.ok("Saldo registrado com sucesso"));
  }
}