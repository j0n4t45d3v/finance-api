package com.jonatas.finance.controller;

import com.jonatas.finance.domain.User;
import com.jonatas.finance.domain.result.account.CreateAccountResult;
import com.jonatas.finance.domain.result.account.EditAccountResult;
import com.jonatas.finance.dto.Response;
import com.jonatas.finance.infra.error.Error;
import com.jonatas.finance.infra.swagger.annotation.AccountTag;
import com.jonatas.finance.infra.swagger.annotation.DefaultErrorResponses;
import com.jonatas.finance.service.AccountService;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@AccountTag
@RestController
@RequestMapping("/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Schema(description = "Request pra cadastrar uma nova conta bancária")
    public record CreateAccountRequest(
        @Schema(example = "Banco do Brasil (Agência:xxxxx-xx)")
        @NotNull String name, 
        Boolean mainAccount
    ) {
    }

    @PostMapping
    @DefaultErrorResponses
    @ApiResponse(
        responseCode = "201",
        description = "Created",
        headers = @Header(name = "Location")
    )
    public ResponseEntity<?> create(
        @RequestBody @Valid CreateAccountRequest request,
        @AuthenticationPrincipal User user
    ) {
        var result = this.accountService.create(request, user);
        if (result instanceof CreateAccountResult.AlreadyExistsAccountWithThisName) {
            var error = new Error<>(
                "account_already_exists",
                "Already exists an account register with same name"
            );
            return ResponseEntity
                .badRequest()
                .body(Response.ofError(error, Response.Status.BAD_REQUEST));
        }

        if (result instanceof CreateAccountResult.AlreadyExistsMainAccountForUser) {
            var error = new Error<>(
                "main_account_already_exists",
                "Already exists an main account register for this user"
            );
            return ResponseEntity
                .badRequest()
                .body(Response.ofError(error, Response.Status.BAD_REQUEST));
        }

        var value = (CreateAccountResult.Success) result;
        var location = UriComponentsBuilder
            .fromPath("/{id}")
            .buildAndExpand(value.account().getId())
            .toUri();
        return ResponseEntity.created(location).build();
    }

    public record EditAccountRequest(
        @Schema(example = "Banco do Brasil (Agência:xxxxx-xx)")
        @NotNull String name,
        Boolean mainAccount
    ) {
    }

    @PutMapping("/{id}")
    @DefaultErrorResponses
    @ApiResponse(
        responseCode = "204",
        description = "No Content",
        headers = {@Header(name = "Location")},
        content = {}
    )
    public ResponseEntity<?> edit(
        @PathVariable("id") Long id,
        @RequestBody EditAccountRequest request,
        @AuthenticationPrincipal User user
    ) {
        var result = this.accountService.update(id, request, user);

        if (result instanceof EditAccountResult.AccountNotFound) {
            Error<String> error = new Error<>("account_not_found", "Account not found");
            return ResponseEntity
                .status(404)
                .body(Response.ofError(error, Response.Status.NOT_FOUND));
        }

        if (result instanceof EditAccountResult.AlreadyExistsAccountWithThisName) {
            Error<String> error = new Error<>(
                "account_already_exists",
                "Already exists an account register with same name"
            );
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Response.ofError(error, Response.Status.CONFLICT));
        }
        if (result instanceof EditAccountResult.AlreadyExistsMainAccountForUser) {
            Error<String> error = new Error<>(
                "main_account_already_exists",
                "Already exists an main account register for this user"
            );
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Response.ofError(error, Response.Status.CONFLICT));
        }

        return ResponseEntity.noContent().build();
    }

    @Schema(description = "Conta do banco resposta")
    public record AccountResponse(
        @Schema(example = "1")
        Long id,
        @Schema(example = "Banco do Brasil (Agência:xxxxx-xx)")
        String name,
        boolean mainAccount
    ) {

    }

    @GetMapping
    public ResponseEntity<Response<List<AccountResponse>, Void>> all(@AuthenticationPrincipal User user) {
        var accounts = this.accountService
            .findAll(user)
            .stream()
            .map(a -> new AccountResponse(
                a.getId(),
                a.getDescriptionValue(),
                a.isMain()
            ))
            .toList();
        return ResponseEntity.ok(Response.of(accounts));
    }


}
