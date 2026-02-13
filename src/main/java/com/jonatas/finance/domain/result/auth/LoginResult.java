package com.jonatas.finance.domain.result.auth;

import com.jonatas.finance.dto.Token;

public sealed interface LoginResult 
    permits LoginResult.Success,
            LoginResult.InvalidCredentials {

    record Success(Token access, Token refresh) implements LoginResult {}

    record InvalidCredentials() implements LoginResult {}
}
