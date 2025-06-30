package br.com.managementfinanceapi.adapter.in.controller.transaction;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.managementfinanceapi.adapter.in.dto.ResponseV0;
import br.com.managementfinanceapi.adapter.in.dto.transaction.CreateBalanceDto;
import br.com.managementfinanceapi.application.port.in.transaction.CreateInitialUserBalancePort;;

@RestController
@RequestMapping("/v1/balances")
public class BalanceControllerV1 {

  private final CreateInitialUserBalancePort createInitialUserBalancePort;

  public BalanceControllerV1(CreateInitialUserBalancePort createInitialUserBalancePort) {
    this.createInitialUserBalancePort = createInitialUserBalancePort;
  }

  @PostMapping("/{userId}/initial_balance")
  public ResponseEntity<ResponseV0<String>> createInitialUserBalance(
      @PathVariable("userId") Long userId,
      @RequestBody CreateBalanceDto balance
  ) {
    this.createInitialUserBalancePort.execute(userId, balance.toDomain());
    return ResponseEntity.ok(ResponseV0.ok("Saldo registrado com sucesso"));
  }
}