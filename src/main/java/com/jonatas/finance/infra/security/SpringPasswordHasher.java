package com.jonatas.finance.infra.security;

import com.jonatas.finance.adapter.security.PasswordHasher;
import com.jonatas.finance.auth.HashPassword;
import com.jonatas.finance.auth.RawPassword;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SpringPasswordHasher implements PasswordHasher {

    private final PasswordEncoder encoder;

    public SpringPasswordHasher(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public HashPassword hash(RawPassword rawPassword) {
        var hashedPassword = this.encoder.encode(rawPassword.value());
        return HashPassword.of(hashedPassword);
    }

    @Override
    public boolean matches(RawPassword rawPassword, HashPassword hashPassword) {
        return this.encoder.matches(rawPassword.value(),
                                    hashPassword.value());
    }
}
