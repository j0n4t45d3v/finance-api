package com.jonatas.finance.controller;

import com.jonatas.finance.domain.User;
import com.jonatas.finance.domain.result.wallet.CreateWalletResult;
import com.jonatas.finance.domain.result.wallet.EditWalletResult;
import com.jonatas.finance.dto.Response;
import com.jonatas.finance.infra.error.Error;
import com.jonatas.finance.infra.swagger.annotation.WalletTag;
import com.jonatas.finance.infra.swagger.annotation.DefaultErrorResponses;
import com.jonatas.finance.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
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

@WalletTag
@RestController
@RequestMapping("/v1/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @Schema(description = "Request pra cadastrar uma nova carteira")
    public record CreateWalletRequest(
        @Schema(example = "Banco do Brasil (Agência:xxxxx-xx)")
        @NotNull String name, 
        Boolean mainWallet
    ) {
    }

    @PostMapping
    @Operation(summary = "Cadastrar uma carteira")
    @DefaultErrorResponses
    @ApiResponse(
        responseCode = "201",
        description = "Created",
        headers = @Header(name = "Location")
    )
    public ResponseEntity<?> create(
        @RequestBody @Valid CreateWalletRequest request,
        @AuthenticationPrincipal User user
    ) {
        var result = this.walletService.create(request, user);
        if (result instanceof CreateWalletResult.AlreadyExistsWalletWithThisName) {
            var error = new Error<>(
                "wallet_already_exists",
                "Already exists an wallet register with same name"
            );
            return ResponseEntity
                .badRequest()
                .body(Response.ofError(error, Response.Status.BAD_REQUEST));
        }

        if (result instanceof CreateWalletResult.AlreadyExistsMainWalletForUser) {
            var error = new Error<>(
                "main_wallet_already_exists",
                "Already exists an main wallet register for this user"
            );
            return ResponseEntity
                .badRequest()
                .body(Response.ofError(error, Response.Status.BAD_REQUEST));
        }

        var value = (CreateWalletResult.Success) result;
        var location = UriComponentsBuilder
            .fromPath("/{id}")
            .buildAndExpand(value.wallet().getId())
            .toUri();
        return ResponseEntity.created(location).build();
    }

    public record EditWalletRequest(
        @Schema(example = "Banco do Brasil (Agência:xxxxx-xx)")
        @NotNull String name,
        Boolean mainWallet
    ) {
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar a carteira")
    @DefaultErrorResponses
    @ApiResponse(
        responseCode = "204",
        description = "No Content",
        headers = {@Header(name = "Location")},
        content = {}
    )
    public ResponseEntity<?> edit(
        @PathVariable("id") Long id,
        @RequestBody EditWalletRequest request,
        @AuthenticationPrincipal User user
    ) {
        var result = this.walletService.update(id, request, user);

        if (result instanceof EditWalletResult.WalletNotFound) {
            Error<String> error = new Error<>("wallet_not_found", "Wallet not found");
            return ResponseEntity
                .status(404)
                .body(Response.ofError(error, Response.Status.NOT_FOUND));
        }

        if (result instanceof EditWalletResult.AlreadyExistsWalletWithThisName) {
            Error<String> error = new Error<>(
                "wallet_already_exists",
                "Already exists an wallet register with same name"
            );
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Response.ofError(error, Response.Status.CONFLICT));
        }
        if (result instanceof EditWalletResult.AlreadyExistsMainWalletForUser) {
            Error<String> error = new Error<>(
                "main_wallet_already_exists",
                "Already exists an main wallet register for this user"
            );
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Response.ofError(error, Response.Status.CONFLICT));
        }

        return ResponseEntity.noContent().build();
    }

    @Schema(description = "Carteira resposta")
    public record WalletResponse(
        @Schema(example = "1")
        Long id,
        @Schema(example = "Banco do Brasil (Agência:xxxxx-xx)")
        String name,
        boolean mainWallet
    ) {

    }

    @GetMapping
    @Operation(summary = "Listar as carteiras")
    public ResponseEntity<Response<List<WalletResponse>, Void>> all(@AuthenticationPrincipal User user) {
        var wallets = this.walletService
            .findAll(user)
            .stream()
            .map(a -> new WalletResponse(
                a.getId(),
                a.getDescriptionValue(),
                a.isMain()
            ))
            .toList();
        return ResponseEntity.ok(Response.of(wallets));
    }


}
