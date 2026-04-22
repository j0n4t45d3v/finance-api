package com.jonatas.finance.controller;

import com.jonatas.finance.domain.User;
import com.jonatas.finance.dto.Response;
import com.jonatas.finance.infra.swagger.annotation.UserTag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@UserTag
@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Schema(description = "Subject do token response")
    public record UserDetailsResponse(
        @Schema(example = "john@doe.example")
        String email,
        List<? extends GrantedAuthority> authorities
    ) {
    }

    @Operation(
        operationId = "me",
        summary = "Dados do usuário logado do token"
    )
    @GetMapping(value = "/me", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Response<UserDetailsResponse, Void>> userDetails(
        @AuthenticationPrincipal User userAuthenticated
    ) {
        UserDetailsResponse userDetailsResponse = new UserDetailsResponse(
            userAuthenticated.getEmailValue(),
            userAuthenticated.getAuthorities().stream().toList()
        );
        return ResponseEntity.ok(Response.of(userDetailsResponse));
    }

}
