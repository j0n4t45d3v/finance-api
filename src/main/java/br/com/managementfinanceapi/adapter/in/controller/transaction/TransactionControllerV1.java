package br.com.managementfinanceapi.adapter.in.controller.transaction;

import br.com.managementfinanceapi.application.port.in.transaction.AddUserTransactionPort;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.managementfinanceapi.adapter.in.dto.ResponseV0;
import br.com.managementfinanceapi.adapter.in.dto.transaction.AddTransaction;
import br.com.managementfinanceapi.application.core.domain.common.dvo.Page;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.dvo.SearchAllFilters;
import br.com.managementfinanceapi.application.port.in.transaction.SearchTransactionPort;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/transactions")
public class TransactionControllerV1 {

  private final AddUserTransactionPort addUserTransactionPort;
  private final SearchTransactionPort searchTransactionRepositoryPort;


  public TransactionControllerV1(
    AddUserTransactionPort addUserTransactionPort,
    SearchTransactionPort searchTransactionRepositoryPort
  ) {
    this.addUserTransactionPort = addUserTransactionPort;
    this.searchTransactionRepositoryPort = searchTransactionRepositoryPort;
  }

  @PostMapping("/add")
  public ResponseEntity<ResponseV0<String>> add(@RequestBody AddTransaction body) {
    this.addUserTransactionPort.execute(body.toDomain(), body.userId());
    ResponseV0<String> addMovementResponse = ResponseV0.ok("Transação salva com sucesso");
    return ResponseEntity.ok(addMovementResponse);
  }

  @GetMapping
  public ResponseEntity<ResponseV0<Page<TransactionDomain>>> getAll(
    @Valid @ModelAttribute SearchAllFilters filters,
    Pageable page
  ) {
    var result = this.searchTransactionRepositoryPort.all(filters);
    var addMovementResponse = ResponseV0.ok(result);
    return ResponseEntity.ok(addMovementResponse);
  }
}