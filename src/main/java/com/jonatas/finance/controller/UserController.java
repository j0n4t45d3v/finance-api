package com.jonatas.finance.controller;

import com.jonatas.finance.domain.User;
import com.jonatas.finance.domain.dvo.user.Email;
import com.jonatas.finance.domain.dvo.user.Password;
import com.jonatas.finance.domain.exception.DomainException;
import com.jonatas.finance.infra.swagger.annotation.DefaultErrorResponses;
import com.jonatas.finance.infra.swagger.annotation.UserTag;
import com.jonatas.finance.service.CreateUserService;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;


@UserTag
@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUserService createUserService;

    public UserController(CreateUserService createUserService) {
        this.createUserService = createUserService;
    }

    public record CreateUser(String email, String password, String confirmPassword) {

        public User toDomain() {
            if (!Objects.equals(password, confirmPassword)) {
                throw new DomainException("password is diferente to confirm password");
            }
            return new User(new Email(this.email), new Password(this.password));
        }

    }

    @PostMapping
    @DefaultErrorResponses
    @ApiResponse(responseCode = "201", description = "Created", headers = {@Header(name = "Location")})
    public ResponseEntity<Void> create(@RequestBody CreateUser createUser) {
        User userCreated = this.createUserService.execute(createUser.toDomain());
        URI location = UriComponentsBuilder
                .fromPath("/users/{id}")
                .buildAndExpand(userCreated.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
