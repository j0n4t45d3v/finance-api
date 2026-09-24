package com.jonatas.finance.adapter.security;

import com.jonatas.finance.auth.HashPassword;
import com.jonatas.finance.auth.RawPassword;

public interface PasswordHasher {

    HashPassword hash(RawPassword rawPassword);

    boolean matches(RawPassword rawPassword, HashPassword hashPassword);

}
