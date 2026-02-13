package com.jonatas.finance.controller;

import com.jonatas.finance.domain.User;
import com.jonatas.finance.domain.result.account.CreateAccountResult;
import com.jonatas.finance.infra.dto.Response;
import com.jonatas.finance.infra.error.Error;
import com.jonatas.finance.infra.swagger.annotation.AccountTag;
import com.jonatas.finance.infra.swagger.annotation.DefaultErrorResponses;
import com.jonatas.finance.service.AccountService;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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

    public record CreateAccountRequest(@NotNull String name, Boolean mainAccount) {
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

    public record AccountResponse(
        String name,
        boolean mainAccount
    ) {

    }

    @GetMapping
    public ResponseEntity<Response<List<AccountResponse>, Void>> all(@AuthenticationPrincipal User user) {
        var accounts = this.accountService
            .findAll(user)
            .stream()
            .map(a-> new AccountResponse(
                a.getDescriptionValue(),
                a.isMain()
            ))
            .toList();
        return ResponseEntity.ok(Response.of(accounts));
    }


}
