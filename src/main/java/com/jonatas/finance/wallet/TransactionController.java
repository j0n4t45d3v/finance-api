package com.jonatas.finance.wallet;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jonatas.finance.auth.User;
import com.jonatas.finance.common.dto.PageResponse;
import com.jonatas.finance.common.dto.Response;
import com.jonatas.finance.infra.swagger.annotation.DefaultErrorResponses;
import com.jonatas.finance.infra.swagger.annotation.TransactionTag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@TransactionTag
@RestController
@RequestMapping(value = "/v1/transactions", produces = { MediaType.APPLICATION_JSON_VALUE })
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @DefaultErrorResponses
    @Operation(summary = "Adiciona uma transação financeira")
    @ApiResponse(responseCode = "201", description = "Created", headers = { @Header(name = "Location") })
    public ResponseEntity<?> add(
                                 @RequestBody @Valid CreateTransactionRequest request,
                                 @AuthenticationPrincipal User user) {
        var result = this.transactionService.create(request, user);
        if (result.isFailure()) {
            var error = result.getError();
            var status = switch (error) {
                case WalletErrorCode.WALLET_NOT_FOUND, CategoryErrorCode.CATEGORY_NOT_FOUND -> Response.Status.NOT_FOUND;
                default -> Response.Status.UNPROCESSABLE_ENTITY;
            };
            return ResponseEntity.status(status.getValue()).body(Response.ofError(error, status));
        }

        var transaction = result.get();
        var location = UriComponentsBuilder.fromPath("/{id}").buildAndExpand(transaction.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @Schema(description = "Transação financeira")
    public record TransactionResponse(
                                      @Schema(example = "1") Long id,
                                      @Schema(example = "10.00") @JsonFormat(shape = JsonFormat.Shape.STRING) BigDecimal amount,
                                      @JsonProperty("transaction_at") @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime transactionAt,
                                      @Schema(example = "EXPENSE") String type,
                                      @Schema(example = "1") Long walletId) {

        public TransactionResponse {
            amount = amount.setScale(2, RoundingMode.HALF_UP);
        }
    }

    @GetMapping
    @Operation(summary = "Lista transações financeiras paginado")
    public ResponseEntity<PageResponse<TransactionResponse>> getPage(
                                                                     @ParameterObject Pageable pageable,
                                                                     @AuthenticationPrincipal User user) {
        var page = this.transactionService.getPage(user, pageable)
                                          .map(
                                               t -> new TransactionResponse(
                                                                            t.getId(),
                                                                            t.getAmountValue(),
                                                                            t.getTransactionAtValue(),
                                                                            t.getType().name(),
                                                                            t.getWalletId()));
        return ResponseEntity.ok(PageResponse.from(page));
    }
}
