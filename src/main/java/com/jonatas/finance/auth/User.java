package com.jonatas.finance.auth;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Entity
@Table(name = "tb_users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email"))
    private Email email;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "password"))
    private HashPassword password;

    protected User() {}

    private User(Long id) {
        this.id = id;
    }

    public User(Email email, HashPassword password) {
        this(null, email, password);
    }

    public User(Long id, Email email, HashPassword password) {
        this.id = id;
        this.email = Objects.requireNonNull(email, "email is required");
        this.password = Objects.requireNonNull(password, "password is required");
    }

    public static User reference(@Nonnull Long userId) {
        return new User(userId);
    }

    public Long getId() {
        return id;
    }

    public HashPassword getHashPassword() {
        return this.password;
    }

    public String getPasswordValue() {
        return this.password.value();
    }

    public void setPassword(String password) {
        this.password = HashPassword.of(password);
    }

    public Email getEmail() {
        return email;
    }

    public String getEmailValue() {
        return this.email.value();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public @Nullable String getPassword() {
        return this.getPasswordValue();
    }

    @Override
    public String getUsername() {
        return this.getEmailValue();
    }
}
