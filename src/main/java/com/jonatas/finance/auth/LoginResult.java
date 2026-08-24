package com.jonatas.finance.auth;

import com.jonatas.finance.common.dto.Token;

public sealed interface LoginResult permits LoginResult.Success, LoginResult.InvalidCredentials {

  record Success(Token access, Token refresh) implements LoginResult {}

  record InvalidCredentials() implements LoginResult {}
}
