package com.jonatas.finance.wallet;

import com.jonatas.finance.auth.User;
import com.jonatas.finance.common.ErrorCode;
import com.jonatas.finance.common.Result;
import com.jonatas.finance.common.dto.Response;
import com.jonatas.finance.infra.error.Error;
import com.jonatas.finance.infra.swagger.annotation.DefaultErrorResponses;
import com.jonatas.finance.infra.swagger.annotation.WalletTag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@WalletTag
@RestController
@RequestMapping("/v1/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @Schema(description = "Request pra cadastrar uma nova carteira")
    public record CreateWalletRequest(@Schema(example = "Banco do Brasil (Agência:xxxxx-xx)") @NotNull String name, Boolean mainWallet) {}

    @PostMapping
    @Operation(summary = "Cadastrar uma carteira")
    @DefaultErrorResponses
    @ApiResponse(responseCode = "201", description = "Created", headers = @Header(name = "Location"))
    public ResponseEntity<?> create(@RequestBody @Valid CreateWalletRequest request,
                                    @AuthenticationPrincipal User user) {
        var result = this.walletService.create(request, user);
        if (result.isFailure()) {
            return resolveErrorCode(result.getError());
        }

        var walletCreated = result.get();
        var location = UriComponentsBuilder.fromPath("/{id}")
                                           .buildAndExpand(walletCreated.getId())
                                           .toUri();
        return ResponseEntity.created(location)
                             .build();
    }

    public record EditWalletRequest(@Schema(example = "Banco do Brasil (Agência:xxxxx-xx)") @NotNull String name,
                                    Boolean mainWallet) {}

    @PutMapping("/{id}")
    @Operation(summary = "Editar a carteira")
    @DefaultErrorResponses
    @ApiResponse(responseCode = "204", description = "No Content")
    public ResponseEntity<?> edit(@PathVariable("id") Long id,
                                  @RequestBody EditWalletRequest request,
                                  @AuthenticationPrincipal User user) {
        var result = this.walletService.update(id, request, user);
        if (result.isFailure()) {
            return resolveErrorCode(result.getError());
        }

        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<Response<?, Error<String>>> resolveErrorCode(ErrorCode errorCode) {
        var error = new Error<>(errorCode.code(), errorCode.message());
        var status = switch (errorCode) {
            case WalletErrorCode.WALLET_WITH_THIS_NAME_ALREADY_EXISTS, WalletErrorCode.MAIN_WALLET_ALREADY_EXISTS -> Response.Status.CONFLICT;
            case WalletErrorCode.WALLET_NOT_FOUND -> Response.Status.NOT_FOUND;
            default -> Response.Status.UNPROCESSABLE_ENTITY;
        };
        return ResponseEntity.status(status.getValue())
                             .body(Response.ofError(error, status));
    }

    @Schema(description = "Carteira resposta")
    public record WalletResponse(@Schema(example = "1") Long id,
                                 @Schema(example = "Banco do Brasil (Agência:xxxxx-xx)") String name,
                                 boolean mainWallet) {}

    @GetMapping
    @Operation(summary = "Listar as carteiras")
    public ResponseEntity<Response<List<WalletResponse>, Void>> all(
                                                                    @AuthenticationPrincipal User user) {
        var wallets = this.walletService.findAll(user)
                                        .stream()
                                        .map(a -> new WalletResponse(a.getId(), a.getDescriptionValue(), a.isMain()))
                                        .toList();
        return ResponseEntity.ok(Response.of(wallets));
    }
}
