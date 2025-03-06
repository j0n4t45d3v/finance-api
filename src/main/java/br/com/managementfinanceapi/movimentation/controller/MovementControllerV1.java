package br.com.managementfinanceapi.movimentation.controller;

import br.com.managementfinanceapi.movimentation.domain.dtos.TransactionDto;
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
import br.com.managementfinanceapi.movimentation.domain.Transaction;
import br.com.managementfinanceapi.movimentation.domain.dtos.AddMovement;
import br.com.managementfinanceapi.movimentation.domain.dtos.SearchMovement;
import br.com.managementfinanceapi.movimentation.gateways.AddMovementGateway;
import br.com.managementfinanceapi.movimentation.gateways.FindAllMovementGateway;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/movements")
public class MovementControllerV1 {

  private static final Logger log = LoggerFactory.getLogger(MovementControllerV1.class);

  private final AddMovementGateway addMovementGateway;
  private final FindAllMovementGateway findAllMovementGateway;

  public MovementControllerV1(AddMovementGateway addMovementGateway, FindAllMovementGateway findAllMovementGateway) {
    this.addMovementGateway = addMovementGateway;
    this.findAllMovementGateway = findAllMovementGateway;
  }

  @PostMapping("/add")
  public ResponseEntity<ResponseV0<String>> add(@RequestBody AddMovement body) {
    this.addMovementGateway.add(body);
    ResponseV0<String> addMovementResponse = ResponseV0.ok("Transação salva com sucesso");
    return ResponseEntity.ok(addMovementResponse);
  }

  @GetMapping
  public ResponseEntity<ResponseV0<Page<TransactionDto>>> getAll(
    @Valid @ModelAttribute SearchMovement filters,
    Pageable page
  ) {
    var result = this.findAllMovementGateway.findAll(filters, page);
    var addMovementResponse = ResponseV0.ok(result);
    log.info("userId: {}", filters.userId());
    log.info("startDate: {}", filters.startDate());
    log.info("endDate: {}", filters.endDate());
    return ResponseEntity.ok(addMovementResponse);
  }
}