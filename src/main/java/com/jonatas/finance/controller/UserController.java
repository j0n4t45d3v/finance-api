package com.jonatas.finance.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jonatas.finance.domain.User;
import com.jonatas.finance.dto.Response;
import com.jonatas.finance.infra.swagger.annotation.UserTag;


@UserTag
@RestController
@RequestMapping("/v1/users")
public class UserController {


    public record UserDetailsResponse(
        String email,
        List<? extends GrantedAuthority> authorities
    ) {
    }

    @GetMapping("/details")
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
