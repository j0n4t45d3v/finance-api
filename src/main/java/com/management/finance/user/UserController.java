package com.management.finance.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.management.finance.user.dto.RegisterUser;
import com.management.finance.user.dto.ReturnUser;
import com.management.finance.user.dto.UpdateUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ReturnUser> register(
            @RequestBody RegisterUser body,
            UriComponentsBuilder uriBuilder
    ) {
        var user = this.userService.register(body);
        var uri = uriBuilder
                .path("/v1/user/{id}")
                .buildAndExpand(user.id())
                .toUri();
        return ResponseEntity.created(uri).body(user);
    }

    @GetMapping
    public ResponseEntity<List<ReturnUser>> getAll() {
        var users = this.userService.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnUser> getById(@PathVariable Long id) {
        var user = this.userService.getById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ReturnUser> getByEmail(@PathVariable String email) {
        var user = this.userService.getByEmail(email);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        this.userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @RequestBody UpdateUser body,
            @PathVariable Long id
    ) {
        this.userService.update(body, id);
        return ResponseEntity.noContent().build();
    }

}
