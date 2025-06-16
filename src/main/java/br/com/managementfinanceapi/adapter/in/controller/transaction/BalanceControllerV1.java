package br.com.managementfinanceapi.adapter.in.controller.transaction;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.managementfinanceapi.adapter.in.dto.ResponseV0;
import br.com.managementfinanceapi.adapter.in.dto.transaction.CreateBalanceDto;
import br.com.managementfinanceapi.application.port.in.transaction.AddCurrentAccountBalancePort;;

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
      @RequestBody CreateBalanceDto balance
  ) {
    this.addCurrentAccountBalancePort.execute(userId, balance.toDomain());
    return ResponseEntity.ok(ResponseV0.ok("Saldo registrado com sucesso"));
  }
}