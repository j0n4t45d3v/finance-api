package br.com.managementfinanceapi.transaction.controller;

import br.com.managementfinanceapi.transaction.domain.dtos.TransactionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.managementfinanceapi.infra.http.dto.ResponseV0;
import br.com.managementfinanceapi.transaction.domain.dtos.AddTransaction;
import br.com.managementfinanceapi.transaction.domain.dtos.SearchTransaction;
import br.com.managementfinanceapi.transaction.gateways.AddTransactionGateway;
import br.com.managementfinanceapi.transaction.gateways.FindAllTransactionGateway;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/transactions")
public class TransactionControllerV1 {

  private static final Logger log = LoggerFactory.getLogger(TransactionControllerV1.class);

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
    log.info("userId: {}", filters.userId());
    log.info("startDate: {}", filters.startDate());
    log.info("endDate: {}", filters.endDate());
    return ResponseEntity.ok(addMovementResponse);
  }
}