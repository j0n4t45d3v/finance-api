package br.com.managementfinanceapi.transaction.controller;

import br.com.managementfinanceapi.infra.http.dto.ResponseV0;
import br.com.managementfinanceapi.transaction.domain.dtos.AddTransaction;
import br.com.managementfinanceapi.transaction.domain.dtos.SearchTransaction;
import br.com.managementfinanceapi.transaction.domain.dtos.TransactionDto;
import br.com.managementfinanceapi.transaction.gateways.AddTransactionGateway;
import br.com.managementfinanceapi.transaction.gateways.FindAllTransactionGateway;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/transactions")
public class TransactionControllerV1 {

  private final AddTransactionGateway addTransactionGateway;
  private final FindAllTransactionGateway findAllTransactionGateway;

  public TransactionControllerV1(AddTransactionGateway addTransactionGateway, FindAllTransactionGateway findAllTransactionGateway) {
    this.addTransactionGateway = addTransactionGateway;
    this.findAllTransactionGateway = findAllTransactionGateway;
  }

  @PostMapping("/add")
  public ResponseEntity<ResponseV0<String>> add(@RequestBody AddTransaction body) {
    this.addTransactionGateway.add(body);
    ResponseV0<String> addMovementResponse = ResponseV0.ok("Transação salva com sucesso");
    return ResponseEntity.ok(addMovementResponse);
  }

  @GetMapping
  public ResponseEntity<ResponseV0<Page<TransactionDto>>> getAll(
    @Valid @ModelAttribute SearchTransaction filters,
    Pageable page
  ) {
    var result = this.findAllTransactionGateway.findAll(filters, page);
    var addMovementResponse = ResponseV0.ok(result);
    return ResponseEntity.ok(addMovementResponse);
  }
}