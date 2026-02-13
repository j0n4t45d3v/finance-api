package com.jonatas.finance.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jonatas.finance.domain.User;
import com.jonatas.finance.domain.result.account.CreateTransactionResult;
import com.jonatas.finance.infra.dto.PageResponse;
import com.jonatas.finance.infra.dto.Response;
import com.jonatas.finance.infra.error.Error;
import com.jonatas.finance.infra.swagger.annotation.AccountTag;
import com.jonatas.finance.infra.swagger.annotation.TransactionTag;
import com.jonatas.finance.service.TransactionService;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@TransactionTag
@RestController
@RequestMapping("/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public record AddTransactionRequest(
            @NotNull
            @DecimalMin("0.1")
            BigDecimal amount,

            @NotNull
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime datetime,

            @NotNull
            Long categoryId,

            @NotNull
            Long accountId
    ) {
    }

    @PostMapping
    public ResponseEntity<?> add(
        @RequestBody @Valid AddTransactionRequest request,
        @AuthenticationPrincipal User user
    ) {
        var result = this.transactionService.create(request, user);
        if (result instanceof CreateTransactionResult.CategoryNotFound) {
            Error<String> error = new Error<>("category_not_found", "Category not found");
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Response.ofError(error, Response.Status.NOT_FOUND));
        }

        if (result instanceof CreateTransactionResult.AccountNotFound) {
            Error<String> error = new Error<>("account_not_foun", "Account not found");
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Response.ofError(error, Response.Status.NOT_FOUND));
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    public record TransactionResponse(
        Long id,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        BigDecimal amount,

        @JsonProperty("transaction_at")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime transactionAt,

        String type,

        Long accountId

    ) {
        public TransactionResponse {
            amount = amount.setScale(2, RoundingMode.HALF_UP);
        }
    }

   @GetMapping
    public ResponseEntity<PageResponse<TransactionResponse>> getPage(Pageable pageable, @AuthenticationPrincipal User user) {
       var page = this.transactionService.getPage(user, pageable)
           .map(t -> new TransactionResponse(
               t.getId(),
               t.getAmountValue(),
               t.getTransactionAtValue(),
               t.getType().name(),
               t.getAccountId()
           ));
       return ResponseEntity.ok(PageResponse.from(page));
    }

}
